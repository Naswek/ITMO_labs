import re

print("задание 2, вариант - ", 407893%6)

stroka = input()
match = re.findall(r'\d{1,}', stroka)
new_match = []
for number in match:
    new_number = (int(number)**2)*3 + 5
    new_match.append(str(new_number))

for i in range(0, len(match)):
    stroka = re.sub(match[i], new_match[i], stroka)

print(stroka)