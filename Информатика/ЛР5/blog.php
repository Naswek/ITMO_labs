<?php
header('Content-Type: text/html; charset=utf-8');

$link = mysqli_connect ("localhost", "admin", "admin");
$db = "MySiteDB";

$select = mysqli_select_db($link, $db);
$selecting_all_from_notes = "SELECT * FROM notes";

$select_note = mysqli_query($link, $selecting_all_from_notes);

while ($note = mysqli_fetch_array($select_note)){
    echo $note['created'], "<br>";
    ?>
    <a href="comments.php?note=<?php echo $note['id']; ?>">
        <?php echo $note ['title'], "<br>";?></a>

    <?php
    echo $note ['article'], "<br>";
}
?>



