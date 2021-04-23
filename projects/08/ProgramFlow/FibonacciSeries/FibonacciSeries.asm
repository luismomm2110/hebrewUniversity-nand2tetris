// push argument 1
@ARG
D=M
@1
A=A+D
D=M
@SP
A=M
M=D
@SP
M=M+1
// pop pointer 1
@SP
AM=M-1
D=M
@4
M=D
// push constant 0
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
// pop that 0
@THAT
D=M
@0
D=A+D
@adress
M=D
@SP
AM=M-1
D=M
@adress
A=M
M=D
// push constant 1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
// pop that 1
@THAT
D=M
@1
D=A+D
@adress
M=D
@SP
AM=M-1
D=M
@adress
A=M
M=D
// push argument 0
@ARG
D=M
@0
A=A+D
D=M
@SP
A=M
M=D
@SP
M=M+1
// push constant 2
@2
D=A
@SP
A=M
M=D
@SP
M=M+1
// sub  
@SP
AM=M-1
D=M
@SP
AM=M-1
M=M-D
@SP
M=M+1
// pop argument 0
@ARG
D=M
@0
D=A+D
@adress
M=D
@SP
AM=M-1
D=M
@adress
A=M
M=D
// (MAIN_LOOP_START)  
(MAIN_LOOP_START)
// push argument 0
@ARG
D=M
@0
A=A+D
D=M
@SP
A=M
M=D
@SP
M=M+1
// if-goto(COMPUTE_ELEMENT)  
@SP
AM=M-1
@COMPUTE_ELEMENT
D;JGT
// goto(END_PROGRAM)  
@SP
AM=M-1
@END_PROGRAM
0;JMP
// if-goto(END_PROGRAM)  
@SP
AM=M-1
@END_PROGRAM
D;JGT
// (COMPUTE_ELEMENT)  
(COMPUTE_ELEMENT)
// push that 0
@THAT
D=M
@0
A=A+D
D=M
@SP
A=M
M=D
@SP
M=M+1
// push that 1
@THAT
D=M
@1
A=A+D
D=M
@SP
A=M
M=D
@SP
M=M+1
// add  
@SP
AM=M-1
M=D
@SP
AM=M-1
M=M+D
@SP
M=M+1
// pop that 2
@THAT
D=M
@2
D=A+D
@adress
M=D
@SP
AM=M-1
D=M
@adress
A=M
M=D
// push pointer 1
@4
D=M
@SP
A=M
M=D
@SP
M=M+1
// push constant 1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
// add  
@SP
AM=M-1
M=D
@SP
AM=M-1
M=M+D
@SP
M=M+1
// pop pointer 1
@SP
AM=M-1
D=M
@4
M=D
// push argument 0
@ARG
D=M
@0
A=A+D
D=M
@SP
A=M
M=D
@SP
M=M+1
// push constant 1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
// sub  
@SP
AM=M-1
D=M
@SP
AM=M-1
M=M-D
@SP
M=M+1
// pop argument 0
@ARG
D=M
@0
D=A+D
@adress
M=D
@SP
AM=M-1
D=M
@adress
A=M
M=D
// goto(MAIN_LOOP_START)  
@SP
AM=M-1
@MAIN_LOOP_START
0;JMP
// if-goto(MAIN_LOOP_START)  
@SP
AM=M-1
@MAIN_LOOP_START
D;JGT
// (END_PROGRAM)  
(END_PROGRAM)
(END)
@END
0;JMP
