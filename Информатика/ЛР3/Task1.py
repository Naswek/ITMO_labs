
print("задание 1, вариант - ", 407893 % 5, 407893 % 4, 407893 % 7)

import re

def find_count(word):
    match = len(re.findall(r'8<\|', word))
    print(match)

find_count('lkdf8<|as.d,ma8<|')
find_count('he8<|llo!')
find_count('-(๑☆‿ ☆8<|#)ᕗ 8<|.°(ಗд8<|ಗ。)°.')
find_count('ылаот8<|ффваь888<|||фы8<|ыа !!! фжв<|лфьт 8<|')
find_count("丕且且8<|丒丧丟丗丵临asd118|||<8<||!0098234丢")

