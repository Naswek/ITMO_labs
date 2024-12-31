<?php
header('Content-Type: text/html; charset=utf-8');

$link = mysqli_connect("localhost", "admin", "admin");
$db = "MySiteDB";
$select = mysqli_select_db($link, $db);

$note_id = $_GET['note'];

$query_notes = "SELECT created, title, article FROM notes WHERE id = $note_id;";

$select_note = mysqli_query($link, $query_notes);
$note = mysqli_fetch_array($select_note);
echo $note ['created'], "<br>";
echo $note ['title'], "<br>";
echo $note ['article'], "<br>";


$query_comments = "SELECT * FROM comments WHERE art_id = $note_id;";
$select_comment = mysqli_query($link, $query_comments);

if ($select_comment > 0) {
    echo "<h3>Комментарии:</h3>";
    echo "<section>";
    while ($comments = mysqli_fetch_array($select_comment)) {
        echo $comments ['created'], "<br>";
        echo $comments ['author'], "<br>";
        echo $comments ['comment'], "<br>";
        echo "</section>";
    }
} else {
    echo "комментов нет", "<br>";
}
?>
