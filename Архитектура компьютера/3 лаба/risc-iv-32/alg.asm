

    .data
.org 0x90
input_addr:     .word 0x80
output_addr:    .word 0x84

;a0 - число
;а1 - длина
;a2 - a1/2
;a3 - generated mask

    .text
.org 0x120
_start:
    lui  t0, %hi(input_addr)                ;загрузка числа
    addi t0, t0, %lo(input_addr)
    lw t1, 0(t0)
    lw a0, 0(t1)

    lui  t0, %hi(output_addr)               ;инициализируем сразу вывод
    addi t0, t0, %lo(output_addr)
    lw a5, 0(t0)

init_32bit:                                 ;инициализация констант и указателя
    addi a1, zero, 32
    addi a2, zero, 16
    addi t4, zero, 0
    addi s0, zero, 1

generate_mask:
    addi t6, zero, 1                        ;добавляем первую единицу
    addi t0, a1, -1                         ;индекс левого бита
    sll a3, t6, t0                          ;левый бит маски
    sll t1, t6, t4                          ;правый бит маски
    or a3, a3, t1                           ;мержим их

check_palindrom:
    beqz a2, success                        ;счетчитк итераций = 0, успех
    and t0, a0, a3                          ;проверка
    beqz t0, rebuild_mask                   ;рез = 0, ок
    beq t0, a3, rebuild_mask                ;рез = маска, ок
    j fail


rebuild_mask:
    addi a1, a1, -1                         ;смещение левого указателя
    addi t4, t4, 1                          ;смещение правого указателя
    addi a2, a2, -1                         ;счетчик итераций--
    j generate_mask                         ;снова генерим маску


fail:                                       ;вывод провала
    sb zero, 0(a5)
    j end

success:                                    ;вывод успеха
    addi t0, zero, 1
    sb t0, 0(a5)
    j end

end:
    halt




https://wrench.edu.swampbuds.me/report/301e6703-5ccc-41ba-80b1-163442572eb4
