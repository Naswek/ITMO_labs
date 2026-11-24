#!/usr/bin/env bash
set -euo pipefail

project_dir=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/../.." && pwd)
cd "$project_dir"

base_url=http://127.0.0.1:8080/labworks
users=2
if [[ ${1:-} =~ ^[0-9]+$ ]]; then
  users=$1
  base_url=${2:-$base_url}
else
  base_url=${1:-$base_url}
  users=${2:-2}
fi
if ! [[ $users =~ ^[0-9]+$ ]] || (( 10#$users < 2 || 10#$users > 100 )); then
  echo 'Укажите число пользователей от 2 до 100.' >&2
  exit 2
fi
users=$((10#$users))
timeout_ms=${JMETER_TIMEOUT_MS:-20000}
if ! [[ $timeout_ms =~ ^[0-9]+$ ]] || (( timeout_ms < 1000 )); then
  echo 'JMETER_TIMEOUT_MS должен быть целым числом не меньше 1000.' >&2
  exit 2
fi
stamp=$(date +%Y%m%d-%H%M%S)
results="tests/jmeter/results-${stamp}.jtl"
run_log="tests/jmeter/jmeter-${stamp}.log"

"${JMETER_BIN:-jmeter}" -n -t tests/jmeter/lab2.jmx \
  -l "$results" -j "$run_log" -JbaseUrl="$base_url" -Jusers="$users" \
  -JtimeoutMs="$timeout_ms"

python3 - "$results" "$users" <<'PY'
import csv
import sys

with open(sys.argv[1], newline='', encoding='utf-8') as file:
    rows = list(csv.DictReader(file))

expected = int(sys.argv[2])
phases = 13
failed = [row for row in rows if row.get('success', '').lower() != 'true']
if len(rows) != expected * phases or failed:
    print(f'JMeter: {len(rows)} steps (expected {expected * phases}), '
          f'{len(failed)} failed. Results: {sys.argv[1]}')
    for row in failed[:10]:
        print(f"- {row.get('threadName')} / {row.get('label')}: {row.get('responseMessage')}")
    if len(failed) > 10:
        print(f'... and {len(failed) - 10} more failures')
    sys.exit(1)

print(f'JMeter: все {expected} пользователей прошли {phases} этапов. Results: {sys.argv[1]}')
PY
