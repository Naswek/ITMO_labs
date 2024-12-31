num = list(input())
s0 = ''
s = []
s.append(int(num[0]) + int(num[2]) + int(num[4]) + int(num[6]))
s.append(int(num[1]) + int(num[2]) + int(num[5]) + int(num[6]))
s.append(int(num[3]) + int(num[4]) + int(num[5]) + int(num[6]))
for i in range(0,3):
    if s[i] % 2 == 0:
        s0 = '0' + s0
    else:
        s0 = '1' + s0
err = int(s0, 2)
if err != 0:
    if num[err-1] == '0':
        num[err-1] = '1'
    else:
        num[err-1] = '0'
    print(*num, '- Правильное сообщение')
    print(f'Ошибка содержится в бите №{err}')
else:
    print('Супер! Ошибки нигде нет :)')


