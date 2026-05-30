#!/bin/bash

RED="RedUser <red@fanfic.com>"
BLUE="BlueUser <blue@fanfic.com>"

git init
echo "фанфик. часть 1" > fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r0"

git checkout -b branch_1  
echo "Событие 1" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r1"

git checkout -b branch_2
echo "Событие 2" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r2"

echo "Событие 3" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r3"

git checkout branch_1
echo "Событие 4" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r4"

git checkout -b branch_3
echo "Событие 5" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r5"

git checkout -b branch_4
echo "Событие 6" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r6"

git checkout master
echo "Событие 7" >> fanfic.txt 
git add fanfic.txt
git commit --author="$RED" -m "r7"

git checkout -b branch_5
echo "Событие 8" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r8"

git checkout master
echo "Событие 9" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r9"

git checkout branch_2
echo "Событие 10" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r10"

git checkout -b branch_6
echo "Событие 11" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r11"

git checkout -b branch_7
echo "Событие 12" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r12"

git checkout -b branch_8
echo "Событие 13" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r13"

git checkout branch_3
echo "Событие 14" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r14"

git checkout -b branch_9
echo "Событие 15" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r15"

git checkout branch_6
echo "Событие 16" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r16"

git checkout branch_7
echo "Событие 17" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r17"

echo "Событие 18" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r18"

git checkout branch_1
echo "Событие 19" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r19"

git checkout branch_7
echo "Событие 20" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r20"

git checkout branch_5
echo "Событие 21" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r21"

git checkout branch_8
echo "Событие 22" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r22"

git checkout -b branch_10
echo "Событие 23" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r23"

git checkout branch_9
echo "Событие 24" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r24"

git checkout branch_5
echo "Событие 25" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r25"

git checkout branch_6
echo "Событие 26" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r26"

git checkout master
echo "Событие 27" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r27"

echo "Событие 28" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r28"

echo "Событие 29" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r29"

git checkout branch_8
echo "Событие 30" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r30"

git checkout master
git merge branch_8 --no-commit -X ours
echo "Событие 31. Мерж" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r31"

git checkout branch_6
echo "Событие 32" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r32"

git checkout -b branch_11
echo "Событие 33" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r33"

git checkout branch_3
echo "Событие 34" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r34"

git checkout branch_4
echo "Событие 35" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r35"

git checkout branch_10
echo "Событие 36" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r36"

git checkout branch_5
git merge branch_10 --no-commit -X ours
echo "Событие 37. Мерж" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r37"

git checkout branch_3
git merge branch_5 --no-commit -X ours
echo "Событие 38. Мерж" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r38"

git checkout branch_11
git merge branch_3 --no-commit -X ours
echo "Событие 39. Мерж" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r39"

git checkout branch_9
git merge branch_11 --no-commit -X ours
echo "Событие 40. Мерж" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r40"

git checkout branch_6
echo "Событие 41" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r41"

git checkout branch_4
echo "Событие 42" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r42"

git checkout branch_7
echo "Событие 43" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r43"

git checkout master
echo "Событие 44" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r44"

git checkout branch_9
echo "Событие 45" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r45"

git checkout branch_1
echo "Событие 46" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r46"

echo "Событие 47" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r47"

git checkout branch_6
git merge branch_1 --no-commit -X ours
echo "Событие 48. Мерж" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r48"

git checkout branch_7
git merge branch_6 --no-commit -X ours
echo "Событие 49. Мерж" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r49"

git checkout branch_9
echo "Событие 50" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r50"

echo "Событие 51" >> fanfic.txt
git add fanfic.txt
git commit --author="$BLUE" -m "r51"

git checkout branch_7
git merge branch_9 --no-commit -X ours
echo "Событие 52. Мерж" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r52"

git checkout branch_4
git merge branch_7 --no-commit -X ours
echo "Событие 53. Мерж" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r53"

git checkout master
git merge branch_4 --no-commit -X ours
echo "Событие 54. Мерж" >> fanfic.txt
git add fanfic.txt
git commit --author="$RED" -m "r54" 

git --no-pager log --graph --oneline --all