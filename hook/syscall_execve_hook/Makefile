obj-m := syscall_hook_execve.o

syscall_hook_execve-objs := syscall_execve_hook.o syscall_stub_execve_hook.o

KERNEL := /lib/modules/`uname -r`/build

all:
	make -C $(KERNEL)   M=`pwd` modules

install:
	make -C $(KERNEL)   M=`pwd` modules_install
	depmod -A

clean:
	make -C $(KERNEL)   M=`pwd` clean
