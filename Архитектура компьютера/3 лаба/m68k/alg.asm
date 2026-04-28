.data
input_addr:       .word  0x80              
output_addr:      .word  0x84     
bytes_data:       .word  0x500             
compressed_data:  .word  0x800   
      
    .text
.org 0x100
_start:
    movea.l  input_addr, A0             ;инит ввода 
    clr.l    D0
    move.l   (A0), D0                  
    movea.l  D0, A0  
                  
    movea.l  output_addr, A1            ;инит вывода
    clr.l    D1
    move.l   (A1), D1                  
    movea.l  D1, A1  
  
    move.l   (A0), D0
                       
    cmp.l    0, D0                      ;длина? <0 / == 0 ?
    blt      out_minus_1
    beq      out_0

    movea.l  bytes_data, A2             ;указатель на массив в адрес А2
    move.l   (A2), D2
    movea.l  D2, A2                    
    move.l   D0, D3
                        
extract_loop:
    cmp.l    0, D3                      ;сколько еще остлось прочитать
    beq      prepare_compress
    
    move.l   (A0), D4                   ;читаем
    move.l   4, D5                      ;счетчик на 4 итерации

extract_byte_inner:                     ;вытаскием байты, лень писать
    move.l   D4, D6
    lsr.l    24, D6                    
    and.l    255, D6                   
    move.b   D6, (A2)+                 
    lsl.l    8, D4                     
    sub.l    1, D3                  
    beq      prepare_compress          
    sub.l    1, D5                  
    bne      extract_byte_inner
    jmp      extract_loop       

prepare_compress:                       
    movea.l  bytes_data, A2             ;откуда берем
    move.l   (A2), D2
    movea.l  D2, A2                    
    movea.l  compressed_data, A3        ;инит куда суем
    move.l   (A3), D2
    movea.l  D2, A3                    
    move.l   D0, D3                     ;счетчик байтиков
    move.l   0, D7                      ;обнуление д7, будет считать длину
                                                 
loop_out:                               ;цикл на каждый новый символ
    cmp.l    0, D3                      ;пусто? да -> идем паковать 
    beq      pack_result
    
    move.b   (A2)+, D1                  ;сохранение "эталона" для текущей итерации      
    sub.l    1, D3
    move.l   1, D2                      ;счетчитк повторок

loop_in:
    cmp.l    0, D3                      ;данные кончились?
    beq      write_rle                  
    cmp.l    255, D2                    ;проверяем лимит счетчика
    beq      write_rle
    
    move.b   (A2), D4                   ;читаем байт
    cmp.b    D1, D4                     ;сравниваем
    bne      write_rle
    
    move.b   (A2)+, D4                 ;инкремент на некст символ
    add.l    1, D2                     ;счетчик++
    sub.l    1, D3                     ;общее кол-во байт--
    jmp      loop_in

write_rle:
    move.b   D2, (A3)+                 ;запись кол-ва потоврок                 
    move.b   D1, (A3)+                 ;запись значения байта
    add.l    2, D7                     :+ 2 к длине сжатых данныъ
    jmp      loop_out

pack_result:
    move.l   D7, (A1)                  ;пишем длину
    movea.l  compressed_data, A3       ;сброс указателя в начало
    move.l   (A3), D2
    movea.l  D2, A3                    
    move.l   D7, D3                    ;d3 - счетчик байт

loop_pack:
    cmp.l    0, D3                     ;кончились байты?
    beq      stop
    
    move.l   0, D4                     ;d4 - для сложения байтов 
    move.l   4, D5                     ;счетчитк на 4 итерации

loop_build_word:                        
    cmp.l    0, D3                     ;кончились байты?
    beq      finish_word_padding       
    clr.l    D6                        ;чистим и готов d6
    move.b   (A3)+, D6                 ;читаем байт
    and.l    255, D6                   ;смотрим на страший байт
    
    cmp.l    4, D5                     ;сдвигаем
    beq      p_shift24
    cmp.l    3, D5
    beq      p_shift16
    cmp.l    2, D5
    beq      p_shift8
    jmp      p_or                  

p_shift24:
    lsl.l    24, D6
    jmp      p_or

p_shift16:
    lsl.l    16, D6
    jmp      p_or

p_shift8:
    lsl.l    8, D6

p_or:                                   
    or.l     D6, D4                     ;счетчик--
    sub.l    1, D3                      ;уменьшаем кол-во байт
    
finish_word_padding:                    
    sub.l    1, D5                      ;счетчик--
    bne      loop_build_word            
    move.l   D4, (A1)                   ;отправка слова в порт
    jmp      loop_pack

out_minus_1:                            ;вывод -1
    move.l   -1, D6
    move.l   D6, (A1)               
    halt

out_0:
    move.l   0, (A1)                    ;вывод 0       
    halt

stop:
    halt
    


    
https://wrench.edu.swampbuds.me/report/b4181f8a-c4d0-456a-95d8-73ae1d08f4f3