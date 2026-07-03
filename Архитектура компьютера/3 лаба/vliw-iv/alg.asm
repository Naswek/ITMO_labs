.data
.org 0x90
input_addr:     .word 0x80
output_addr:    .word 0x84
result_buffer:  .word 0x500
word_step:      .word 4
minus_one:      .word -1
sign_shift:     .word 31
overflow_code:  .word 0xCCCCCCCC

.text
.org 0x100
_start:
    lui  t0, %hi(input_addr) / lui  t1, %hi(output_addr) / nop / nop
    addi t0, t0, %lo(input_addr) / addi t1, t1, %lo(output_addr) / nop / nop
    lui  t2, %hi(result_buffer) / lui  t3, %hi(word_step) / lw s0fp, 0(t0) / nop
    addi t2, t2, %lo(result_buffer) / addi t3, t3, %lo(word_step) / lw s1, 0(t1) / nop
    lui  t0, %hi(minus_one) / lui  t1, %hi(sign_shift) / lw s4, 0(t2) / nop
    addi t0, t0, %lo(minus_one) / addi t1, t1, %lo(sign_shift) / lw s9, 0(t3) / nop
    lui  t2, %hi(overflow_code) / mv s5, s4 / lw s3, 0(t0) / nop
    addi t2, t2, %lo(overflow_code) / nop / lw s7, 0(t1) / nop
    nop / nop / lw s8, 0(t2) / nop

    nop / nop / lw s2, 0(s0fp) / nop
    nop / nop / nop / blt s2, zero, write_minus_one
    mv s6, s2 / nop / nop / beqz s2, halt_program

calc_loop:
    nop / nop / lw a0, 0(s0fp) / nop
    nop / nop / lw a1, 0(s0fp) / nop
    nop / nop / lw a2, 0(s0fp) / nop
    nop / nop / lw a3, 0(s0fp) / nop

    mul t0, a0, a3 / mulh t1, a0, a3 / nop / nop
    mul t2, a1, a2 / mulh t3, a1, a2 / nop / nop

    sub t4, t0, t2 / sub t5, t1, t3 / nop / nop
    nop / nop / nop / bgtu t2, t0, apply_borrow

check_fit:
    sra t6, t4, s7 / nop / nop / nop
    nop / nop / nop / bne t5, t6, write_overflow

store_det:
    nop / nop / sw t4, 0(s4) / nop
    add s4, s4, s9 / add s2, s2, s3 / nop / nop
    nop / nop / nop / bnez s2, calc_loop

write_loop:
    nop / nop / lw t4, 0(s5) / nop
    add s5, s5, s9 / add s6, s6, s3 / sw t4, 0(s1) / nop
    nop / nop / nop / bnez s6, write_loop
    nop / nop / nop / j halt_program

apply_borrow:
    add t5, t5, s3 / nop / nop / j check_fit

write_minus_one:
    nop / nop / sw s3, 0(s1) / nop
    nop / nop / nop / j halt_program

write_overflow:
    nop / nop / sw s8, 0(s1) / nop
    nop / nop / nop / j halt_program

halt_program:
    nop / nop / nop / halt
