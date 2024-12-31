<?php
header('Content-Type: text/html; charset=utf-8');

$link = mysqli_connect("localhost", "root", "");
if ($link) {
    echo "Соединение с сервером установлено", "<br>";
} else {
    echo "Нет соединения с сервером";
}

$query = "GRANT ALL PRIVILEGES ON *.* TO 'admin'@'localhost' IDENTIFIED BY 'admin' WITH GRANT OPTION";
$create_user = mysqli_query($link, $query);

if ($create_user) {
    echo "Пользователь создан";
} else {
    echo "Пользователь не создан";
}