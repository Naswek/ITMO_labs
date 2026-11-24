#!/usr/bin/env bash
set -euo pipefail

die() {
    printf 'Ошибка: %s\n' "$*" >&2
    exit 1
}

mode=deploy
if [[ ${1:-} == --check ]]; then
    mode=check
    shift
fi
(( $# <= 1 )) || die 'Использование: bash deploy-glassfish.sh [--check] [путь/к/glassfish-server.conf]'

script_dir=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
config_file=${1:-"$script_dir/glassfish-server.conf"}
[[ -f $config_file && -r $config_file ]] || die "Не найден конфиг: $config_file"
deploy_dir=$(cd -- "$(dirname -- "$config_file")" && pwd -P)
config_file="$deploy_dir/$(basename -- "$config_file")"

# Конфиг содержит только настройки владельца приложения; пароль в нём не хранится.
# shellcheck disable=SC1090
source "$config_file"

[[ -w $deploy_dir ]] || die "Нет права записи в $deploy_dir. Укажи уже существующий доступный каталог."
[[ ${HTTP_PORT:-} =~ ^[1-9][0-9]*$ ]] || die 'Укажи числовой HTTP_PORT в конфиге.'
http_port=$((10#$HTTP_PORT))
port_base=$((http_port - 80))
(( port_base >= 1024 && port_base + 86 <= 65535 )) || die 'HTTP_PORT не оставляет допустимого диапазона портов GlassFish.'
admin_port=$((port_base + 48))

db_host=${DB_HOST:-pg}
db_port=${DB_PORT:-5432}
db_name=${DB_NAME:-studs}
db_user=${DB_USER:-$(id -un)}
db_schema=${DB_SCHEMA:-}
db_passfile=${DB_PASSFILE:-${PGPASSFILE:-$HOME/.pgpass}}
[[ $db_host =~ ^[A-Za-z0-9_.-]+$ ]] || die 'DB_HOST содержит недопустимые символы.'
[[ $db_port =~ ^[1-9][0-9]*$ ]] && (( 10#$db_port <= 65535 )) || die 'DB_PORT должен быть номером порта.'
[[ $db_name =~ ^[A-Za-z0-9_]+$ ]] || die 'DB_NAME содержит недопустимые символы.'
[[ $db_user =~ ^[A-Za-z0-9_.-]+$ ]] || die 'DB_USER содержит недопустимые символы.'
[[ -z $db_schema || $db_schema =~ ^[A-Za-z_][A-Za-z0-9_]*$ ]] || die 'DB_SCHEMA должно быть простым именем схемы PostgreSQL.'
[[ -f $db_passfile && -r $db_passfile ]] || die "Не найден доступный файл паролей PostgreSQL: $db_passfile"

append_db_alias_password() {
    local line host port database username encoded password='' char i
    while IFS= read -r line || [[ -n $line ]]; do
        line=${line%$'\r'}
        [[ -n $line && $line != \#* ]] || continue
        IFS=: read -r host port database username encoded <<< "$line"
        [[ $host == '*' || $host == "$db_host" ]] || continue
        [[ $port == '*' || $port == "$db_port" ]] || continue
        [[ $database == '*' || $database == "$db_name" ]] || continue
        [[ $username == '*' || $username == "$db_user" ]] || continue
        [[ -n $encoded ]] || die "Для $db_user в $db_passfile указан пустой пароль."
        for ((i=0; i<${#encoded}; i++)); do
            char=${encoded:i:1}
            if [[ $char == '\' ]]; then
                i=$((i + 1))
                (( i < ${#encoded} )) || die "Некорректное экранирование пароля в $db_passfile"
                char=${encoded:i:1}
            fi
            password+=$char
        done
        printf 'AS_ADMIN_ALIASPASSWORD=%s\n' "$password" >> "$admin_password_file"
        return 0
    done < "$db_passfile"
    die "В $db_passfile нет записи для $db_host:$db_port/$db_name и пользователя $db_user"
}

war="$deploy_dir/labworks-glassfish.war"
driver="$deploy_dir/postgresql.jar"
archive="$deploy_dir/glassfish-7.1.1.zip"
glassfish_home=${GLASSFISH_HOME:-"$deploy_dir/glassfish7"}
[[ $glassfish_home == /* ]] || die 'GLASSFISH_HOME должен быть абсолютным путём.'
domain_name=labworks
domain_dir="$deploy_dir/$domain_name"
asadmin="$glassfish_home/bin/asadmin"

[[ -f $war ]] || die "Не найден WAR: $war"
[[ -f $driver ]] || die "Не найден JDBC-драйвер: $driver"
command -v java >/dev/null || die 'Java не найдена. Для GlassFish 7.1.1 нужна Java 17 или 21.'
java_version=$(java -version 2>&1) || die 'Не удалось запустить Java.'
[[ $java_version =~ \"([0-9]+)(\.[0-9]+)*\" ]] || die 'Не удалось определить версию Java.'
(( BASH_REMATCH[1] >= 17 )) || die 'Для GlassFish 7.1.1 нужна Java 17 или новее.'
if [[ ! -x $asadmin ]]; then
    [[ $glassfish_home == "$deploy_dir/glassfish7" ]] || die "GlassFish не найден: $asadmin"
    [[ ! -e $glassfish_home ]] || die "Каталог $glassfish_home существует, но asadmin отсутствует. Проверь распаковку."
    [[ -f $archive ]] || die "Не найден архив GlassFish: $archive"
    command -v unzip >/dev/null || die 'На сервере нет unzip. Распакуй архив локально и загрузи каталог glassfish7.'
fi

printf 'Каталог: %s\nHTTP: %s, admin: %s, база портов: %s\nБД: %s:%s/%s, пользователь: %s, схема: %s\n' \
    "$deploy_dir" "$http_port" "$admin_port" "$port_base" \
    "$db_host" "$db_port" "$db_name" "$db_user" "${db_schema:-по умолчанию}"

if [[ $mode == check ]]; then
    printf 'Проверка файлов и прав пройдена. Доступность портов и БД проверится при запуске.\n'
    exit 0
fi

if [[ ! -x $asadmin ]]; then
    printf 'Распаковываю GlassFish в существующий каталог %s\n' "$deploy_dir"
    unzip -q "$archive" -d "$deploy_dir"
    [[ -x $asadmin ]] || die "После распаковки не найден $asadmin"
fi

# В архиве GlassFish некоторые скрипты требуют /bin/bash. На учебном сервере
# Bash может находиться в другом каталоге. Исправляем только эти shebang-строки.
if [[ ! -x /bin/bash ]]; then
    bash_path=$(command -v bash)
    [[ $bash_path == /* ]] || die 'Не удалось определить абсолютный путь к Bash.'
    for launcher in \
        "$glassfish_home/bin/asadmin" \
        "$glassfish_home/glassfish/bin/asadmin" \
        "$glassfish_home/bin/startserv" \
        "$glassfish_home/glassfish/bin/startserv" \
        "$glassfish_home/bin/stopserv" \
        "$glassfish_home/glassfish/bin/stopserv"; do
        [[ -f $launcher ]] || continue
        IFS= read -r first_line < "$launcher" || true
        [[ $first_line == '#!/bin/bash' ]] || continue
        [[ -w $launcher ]] || die "Нет права исправить путь к Bash в $launcher"
        temp_launcher="$launcher.labworks.$$"
        sed "1s|^#!/bin/bash$|#!$bash_path|" "$launcher" > "$temp_launcher"
        cat "$temp_launcher" > "$launcher"
        rm "$temp_launcher"
    done
fi

port_marker="$domain_dir/.labworks-port-base"
[[ -t 0 ]] || die 'Для настройки GlassFish нужна интерактивная SSH-сессия.'
if [[ ! -f $domain_dir/config/domain.xml ]]; then
    printf 'Придумай пароль администратора GlassFish (минимум 8 символов).\n'
else
    printf 'Введи пароль администратора GlassFish.\n'
fi
IFS= read -r -s -p 'Пароль admin: ' admin_password
printf '\n'
(( ${#admin_password} >= 8 )) || die 'Пароль администратора должен содержать минимум 8 символов.'
admin_password_file=$(mktemp "$deploy_dir/.labworks-admin.XXXXXX") || die 'Не удалось создать временный файл для пароля.'
trap 'rm -f "$admin_password_file"' EXIT
printf 'AS_ADMIN_PASSWORD=%s\n' "$admin_password" > "$admin_password_file"
unset admin_password
append_db_alias_password

if [[ ! -f $domain_dir/config/domain.xml ]]; then
    [[ ! -e $domain_dir ]] || die "Каталог $domain_dir существует, но не содержит готовый домен."
    "$asadmin" --user admin --passwordfile "$admin_password_file" create-domain \
        --domaindir "$deploy_dir" \
        --portbase "$port_base" \
        --nopassword=false \
        "$domain_name"
    printf '%s\n' "$port_base" > "$port_marker"
else
    [[ -r $port_marker ]] || die "Домен $domain_dir создан вне этого скрипта; его порты неизвестны."
    [[ $(<"$port_marker") == "$port_base" ]] || die 'HTTP_PORT изменён, а существующий домен использует прежние порты.'
fi

[[ -d $domain_dir/lib ]] || die "В домене не найден каталог lib: $domain_dir/lib"
domain_status=$("$asadmin" list-domains --domaindir "$deploy_dir")
running=
while IFS= read -r status_line; do
    case "$status_line" in
        "$domain_name not running"*|"Name: $domain_name Status: Not running"*) running=false ;;
        "$domain_name running"*|"Name: $domain_name Status: Running"*) running=true ;;
    esac
done <<< "$domain_status"
[[ -n $running ]] || die "Не удалось определить состояние домена: $domain_status"

if ! cmp -s "$driver" "$domain_dir/lib/postgresql.jar"; then
    if [[ $running == true ]]; then
        "$asadmin" --user admin stop-domain --domaindir "$deploy_dir" "$domain_name"
        running=false
    fi
    cp "$driver" "$domain_dir/lib/postgresql.jar"
fi
if [[ $running == false ]]; then
    "$asadmin" --user admin start-domain --domaindir "$deploy_dir" "$domain_name"
fi

admin=("$asadmin" --host localhost --user admin --port "$admin_port" --passwordfile "$admin_password_file")
if ! aliases=$("${admin[@]}" list-password-aliases 2>&1); then
    die "Не удалось выполнить команду администратора GlassFish: $aliases"
fi
if ! grep -Fiq labworksDb <<< "$aliases"; then
    alias_log=$(mktemp "$deploy_dir/.labworks-alias.XXXXXX") || die 'Не удалось создать временный файл для вывода GlassFish.'
    if "${admin[@]}" create-password-alias labworksDb 2>&1 | tee "$alias_log"; then
        :
    elif grep -Fq 'already exists' "$alias_log"; then
        printf 'Алиас labworksDb уже существует, продолжаю настройку.\n'
    else
        rm -f "$alias_log"
        die 'Не удалось создать алиас пароля БД.'
    fi
    rm -f "$alias_log"
fi

db_marker="$domain_dir/.labworks-db-settings"
db_settings=$(printf '%s\n' "$db_host" "$db_port" "$db_name" "$db_user" "$db_schema")
if [[ -f $db_marker && $(<"$db_marker") != "$db_settings" ]]; then
    die 'Параметры БД изменились после создания пула. Обнови пул вручную или используй новый домен.'
fi

pools=$("${admin[@]}" list-jdbc-connection-pools)
if ! grep -Fxq LabworksPool <<< "$pools"; then
    [[ ! -e $db_marker ]] || die 'Пул LabworksPool отсутствует, хотя настройки уже были сохранены.'
    # В --property знак '=' внутри значения надо экранировать для парсера asadmin.
    # Переменная передаётся одним аргументом, поэтому здесь нужен один символ '\'.
    properties="serverName=$db_host:portNumber=$db_port:databaseName=$db_name:user=$db_user:password=\${ALIAS\\=labworksDb}"
    if [[ -n $db_schema ]]; then
        properties+=":currentSchema=$db_schema"
    fi
    "${admin[@]}" create-jdbc-connection-pool \
        --datasourceclassname org.postgresql.ds.PGSimpleDataSource \
        --restype javax.sql.DataSource \
        --property "$properties" LabworksPool
    printf '%s\n' "$db_settings" > "$db_marker"
elif [[ ! -f $db_marker ]]; then
    die 'Пул LabworksPool уже существует, но его параметры не были созданы этим скриптом.'
fi

resources=$("${admin[@]}" list-jdbc-resources)
if ! grep -Fxq jdbc/LabworksDS <<< "$resources"; then
    "${admin[@]}" create-jdbc-resource --connectionpoolid LabworksPool jdbc/LabworksDS
fi

if ! ping_output=$("${admin[@]}" ping-connection-pool LabworksPool 2>&1); then
    printf '%s\n' "$ping_output" >&2
    if [[ $ping_output != *'password authentication failed'* ]]; then
        die 'Проверка подключения к БД не прошла. Причина указана выше.'
    fi
    if command -v psql >/dev/null; then
        if ! (unset PGPASSWORD; PGPASSFILE="$db_passfile" psql -h "$db_host" -p "$db_port" -U "$db_user" -d "$db_name" -w -Atqc 'SELECT 1' >/dev/null); then
            die "PostgreSQL не принимает пароль из $db_passfile. Проверь запись для $db_host:$db_port/$db_name и пользователя $db_user."
        fi
    fi
    printf 'Пароль БД в алиасе labworksDb не подошёл. Обновляю его из %s.\n' "$db_passfile"
    "${admin[@]}" update-password-alias labworksDb
    # Пул мог сохранить прежний пароль в памяти. Перезапуск домена заново читает алиас.
    "$asadmin" --user admin stop-domain --domaindir "$deploy_dir" "$domain_name"
    "$asadmin" --user admin start-domain --domaindir "$deploy_dir" "$domain_name"
    if ! ping_output=$("${admin[@]}" ping-connection-pool LabworksPool 2>&1); then
        printf '%s\n' "$ping_output" >&2
        die 'GlassFish по-прежнему не может подключиться к БД после обновления алиаса и перезапуска домена. Проверь настройку JDBC-пула и server.log.'
    fi
fi
printf '%s\n' "$ping_output"
"${admin[@]}" deploy --force=true --name labworks --contextroot labworks "$war"

printf 'Готово. Приложение: http://localhost:%s/labworks/\nЛог: %s/logs/server.log\n' \
    "$http_port" "$domain_dir"
