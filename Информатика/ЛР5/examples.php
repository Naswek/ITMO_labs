<?php
header('Content-Type: text/html; charset=utf-8');

#1
echo "<h3>упражнение 1</h3>";
$a=10;
$b=20;
echo "значение а $a\nзначение b $b",  "<br/>";

#2
echo "<h3>упражнение 2</h3>";
$c = $a + $b;
echo "\nзначение с $c",  "<br/>";

#3
echo "<h3>упражнение 3</h3>";
$d = $c * 3;
echo "\nутроенное значение c $d",  "<br/>";

#4
echo "<h3>упражнение 4</h3>";
$d = $c / ($b - $a);
echo "\nс/(a-b) = $d",  "<br/>";

#5
$p = "Программа";
$b= "работает";

#6
echo "<h3>упражнение 5-6</h3>";
$result = $p . " " . $b;
echo $result,  "<br/>";

#7
echo "<h3>упражнение 7</h3>";
$result .= "хорошо";
echo $result,  "<br/>";

#8
echo "<h3>упражнение 8</h3>";
$q = 5;
$w = 7;
$q += $w;
$w = $q -$w;
$q -= $w;
echo "\n$q $w\n",  "<br/>";

#9
echo "<h3>упражнение 9</h3>";
for ($i = 22; $i < 79; $i++) {
    echo "Значение: $i\n",  "<br/>";
}

#10
echo "<h3>упражнение 10</h3>";
echo "<ul>";
for ($i = 1; $i < 11; $i++) {
    echo "<li>Значение: $i\n</li>",  "<br/>";
}
echo "</ul>";

#11
echo "<h3>упражнение 11</h3>";
$massive = array();
for ($i = 1; $i < 101; $i++) {
    array_push($massive, rand(1, 1000));
}
$i=0;
while ($i <100) {
    echo "while: $massive[$i]\n",  "<br/>";
    $i++;
}
foreach ($massive as $value) {
    echo "foreach: $value\n",  "<br/>";
}

#12
echo "<h3>упражнение 12</h3>";
$day = array("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье");
$date = date('N');
switch ($date) {
    case "1":
        echo $day[0] . "\n";
        break;
    case "2":
        echo $day[1] . "\n";
        break;
    case "3":
        echo $day[2] . "\n";
        break;
    case "4":
        echo $day[3] . "\n";
        break;
    case "5":
        echo $day[4] . "\n";
        break;
    case "6":
        echo $day[5] . "\n";
        break;
    case "7":
        echo $day[6] . "\n";
}

#13
echo "<h3>упражнение 13</h3>";
function getPlus10($k) {
    return $k += 10;
}
echo getPlus10(6);
?>


