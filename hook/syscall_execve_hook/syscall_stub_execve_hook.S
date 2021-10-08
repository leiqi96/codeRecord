.text
.global my_stub_execve_hook
my_stub_execve_hook:
pushq   %rbx
pushq   %rdi
pushq   %rsi
pushq   %rdx
pushq   %rcx
pushq   %rax
pushq   %r8 
pushq   %r9 
pushq   %r10
pushq   %r11
call my_execve_hook
pop     %r11
pop     %r10
pop     %r9 
pop     %r8 
pop     %rax
pop     %rcx
pop     %rdx
pop     %rsi
pop     %rdi
pop     %rbx
jmp     *orig_sys_call_table(, %rax, 8)
