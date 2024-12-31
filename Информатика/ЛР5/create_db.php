<?php
header('Content-Type: text/html; charset=utf-8');

$link = mysqli_connect('localhost', 'admin', 'admin');
if ($link) {
    echo "Соединение с сервером установлено", "<br>";
} else {
    echo "Нет соединения с сервером";
}

$db = "MySiteDB";
$query = "CREATE DATABASE $db";

$create_db = mysqli_query($link, $query);
	if ($create_db) {
        echo "База данных $db успешно создана";
    } else {
        echo "База не создана";
    }
?>