#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/syscalls.h>
#include <linux/delay.h>
#include <linux/file.h>
#include <asm/paravirt.h>
#include <asm/syscall.h>
#include <linux/sys.h>
#include <linux/slab.h>
#include <linux/kallsyms.h>
#include <linux/binfmts.h>
#include <linux/version.h>
#include <net/sock.h>
#include <net/netlink.h>

unsigned long **sys_call_table_ptr;
unsigned long original_cr0;
void *orig_sys_call_table [NR_syscalls];

#if LINUX_VERSION_CODE >= KERNEL_VERSION(3, 10, 0)
    typedef asmlinkage long (*func_execve)(const char __user *,
                                           const char __user * const __user *,
                                           const char __user *const  __user *);
    extern asmlinkage long my_stub_execve_hook (const char __user *,
                                                     const char __user *const __user *,
                                                     const char __user *const __user *);
#elif LINUX_VERSION_CODE == KERNEL_VERSION(2, 6, 32)
    typedef asmlinkage long (*func_execve)(const char __user *,
                                           const char __user * const __user *,
                                           const char __user *const  __user *,
                                           struct pt_regs *);
    extern asmlinkage long my_stub_execve_hook(const char __user *,
                                                    const char __user * const __user *,
                                                    const char __user *const  __user *,
                                                    struct pt_regs *);
#endif


func_execve orig_execve;

unsigned long **find_sys_call_table(void) {
    unsigned long ptr;
    unsigned long *p;
    pr_err("Start found sys_call_table.\n");
    
    for (ptr = (unsigned long)sys_close;
         ptr < (unsigned long)&loops_per_jiffy;
         ptr += sizeof(void *)) {
        p = (unsigned long *)ptr;
        if (p[__NR_close] == (unsigned long)sys_close) {
            pr_err("Found the sys_call_table!!! __NR_close[%d] sys_close[%lx]\n"
                    " __NR_execve[%d] sct[__NR_execve][0x%lx]\n",
                    __NR_close,
                    (unsigned long)sys_close,
                    __NR_execve,
                    p[__NR_execve]);
            return (unsigned long **)p;
        }
    }
    
    return NULL;
}


#if LINUX_VERSION_CODE == KERNEL_VERSION(2, 6, 32)
asmlinkage long my_execve_hook(char __user *name,
        char __user * __user *argv,
        char __user * __user *envp, 
        struct pt_regs *regs)
{
    struct filename *path = NULL;
    long error = 0;

    path = getname(name);
    error = PTR_ERR(path);
    if (IS_ERR(path)) {
        pr_err("get path failed.\n");
        goto err;
    }
    
    printk(KERN_INFO"this is my execve prog: %s\n", path->name);

err:
    return 0;
}

#elif LINUX_VERSION_CODE >= KERNEL_VERSION(3, 10, 0)
asmlinkage long my_execve_hook(const char __user *filename, 
        const char __user *const __user *argv,
        const char __user *const __user *envp)
{
    printk(KERN_INFO"this is my execve.\n");
    return 0;
}
#else
asmlinkage long my_execve_hook(void)
{
    return 0;
}
#endif

static int __init execve_hook_init(void)
{
    int i = 0;
    if (!(sys_call_table_ptr = find_sys_call_table())){
        pr_err("Get sys_call_table failed.\n");
        return -1;
    }

    original_cr0 = read_cr0();
    write_cr0(original_cr0 & ~0x00010000);
    pr_err("Loading module change syshook, sys_call_table at %p\n", sys_call_table_ptr);
    
    for(i = 0; i < NR_syscalls - 1; i ++) {
        orig_sys_call_table[i] = sys_call_table_ptr[i];
    }
    orig_execve = (void *)(sys_call_table_ptr[__NR_execve]);
    sys_call_table_ptr[__NR_execve]= (void *)my_stub_execve_hook;
    write_cr0(original_cr0);
    
    return 0;
}

static void __exit execve_hook_exit(void)
{

    if (!sys_call_table_ptr){
        return;
    }

    write_cr0(original_cr0 & ~0x00010000);
    sys_call_table_ptr[__NR_execve] = (void *)orig_execve;
    write_cr0(original_cr0);
    
    sys_call_table_ptr = NULL;
    pr_err("unload change syshook succ.\n");
}

module_init(execve_hook_init);
module_exit(execve_hook_exit);
MODULE_LICENSE("GPL");
