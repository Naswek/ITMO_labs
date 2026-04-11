  .data

input_addr:     .word  0x80
output_addr:    .word  0x84
a_word:         .word 0             ;a   
b_word:         .word 0             ;b
curr: 		    .word 0
mask: 		    .word 0x7FFFFFFF    
error_b:	    .word -1
error_carry:	.word 0xCCCCCCCC
		

  .text
  .org 0x100
_start:
load_arg:
	load			input_addr      ;load a
    load_acc
    ble             if_input_is_0   ;check if a != 0
	store	 	    a_word
	load 			input_addr      ;load b
    load_acc       
    ble             if_input_is_0   ;check if b != 0
	store		    b_word

loop:
	load_addr 	a_word 
	rem 	  	b_word              ;a % b
	store_addr 	curr                ;store a % b

	load_addr 	b_word      
	store_addr 	a_word              ;a = b

	load_addr 	curr
	store_addr 	b_word              ;b = a % b
	bcs 		if_carry            ;check if carry

	load_addr 	b_word
	beqz 		done		        ;b == 0 -> done
	jmp 		loop                ;b != 0 -> loop

done:
	load_addr 	a_word	
	and 		mask                ;clear 15 bit
    jmp write_and_stop              

if_input_is_0:
	load_addr	error_b             ;load value if a or b == 0
    jmp write_and_stop

if_carry:
	load_addr 	error_carry         ;load value if carry
    jmp write_and_stop

write_and_stop:
    store_ind 	output_addr         ;store output
	halt                            ;stop machine

