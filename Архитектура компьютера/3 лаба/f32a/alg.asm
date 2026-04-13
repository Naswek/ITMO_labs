    .data
.org 0x90
underscore:        		.word  0x5F
length_addr:			.word  0x0
inv_newline_symb:       .word  0xFFFFFFF6
inv_before_lower_symb:	.word  0xFFFFFF9F
inv_diff:			    .word  0xFFFFFFE0
error_code:             .word  0xCCCCCCCC
overflow_check:         .word  -32
stored_end:             .word  0x0

    .text

    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

.org 0x100
_start:

init:
	lit 0
	!p 0x00

    lit 1
    a!

main:
	lit 0x80  		    \загрузка символа
	b!
	@b


	dup
	is_space 	   	    \проверка на символ переноса строки
	if start_print 	  	\запуск подготовки к циклу печати

    a
    @p overflow_check + \проверка, что длина еще не достигнла границы
    -if overflow        \вывод при оверфлоу

	dup
	is_lower 	   		\проверка, маенькая ли буква
	-if to_upper	    \заменить на большую

out:                    \сохраняем в буфер символ
	!+
	@p 0x00
	lit 1 + !p 0x00
	main ;

to_upper:
	@p inv_diff	+
    out ;

is_lower:
	@p inv_before_lower_symb + ;

is_space:
	@p inv_newline_symb + ;

start_print:
    drop
    a !p  stored_end
    lit 1 a!

print_loop:             \начинаем вывод символов
    a @p stored_end
    inv lit 1 + +
    if fill_loop

    @+
    lit 255 and         \маска для выводов байтов
    lit 132
    b! !b
    print_loop ;

fill_loop:              \заполняем оставшиеся ячейки буфера _
    a
    lit 0xFFFFFFE0
    +
    -if stop

    @p underscore
    !+

    fill_loop ;

overflow:               \выводим ошибку
    drop
    @p error_code
    lit 0x84 b! !b

stop:
	halt





https://wrench.edu.swampbuds.me/report/753f873d-11ab-4685-9956-e28c0537eb50
