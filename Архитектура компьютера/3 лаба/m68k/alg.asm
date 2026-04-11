    .data

input_addr:      .word  0x80              
output_addr:     .word  0x84              
length_in:       .word  0x0
length_out:      .word 0x0

    .text

_start:
                                 ; вот тут надо еще прописать проверку типа not input_words
    move.l (input_addr) length   ;читаем кол-во слов
    beq if_length_0
                                 

if_not_input:
    ;тут выводим -1
    jpm stop

if_length_less_than_0:
    ;тут выводим -1
    jpm stop

if_length_0:
    ;тут выводим 0
    jpm stop

prepare:
    
    
loop:


stop:
    halt

