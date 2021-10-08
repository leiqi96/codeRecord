#ifndef __KERNEL__
#define __KERNEL__
#endif
#ifndef MODULE
#define MODULE
#endif
#include <linux/kernel.h>
#include <linux/config.h>
#include <linux/module.h>
#include <asm/string.h>
#include <asm/unistd.h>
#include <linux/fs.h>
#include <linux/sched.h>
#include <linux/mm.h>
#include <linux/pagemap.h>
#include <asm/smplock.h>
int (*orig_vmtruncate) (struct inode * inode, loff_t offset) = (int(*)
(struct inode *inode, loff_t offset))0xc0125d70; 
/* 原vmtruncate函数的地址0xc0125d70可到system.map文件中查找*/
#define CODESIZE 7    /*替换代码的长度 */
static char orig_code[7];  /*保存原vmtruncate函数被覆盖部分的执行码 */
static char code[7] = 
            "\xb8\x00\x00\x00\x00"
"\xff\xe0";  /* 替换码 */
/* 如果该函数没有export出来，则需要自己实现，供vmtruncate调用 */
static void _vmtruncate_list(struct vm_area_struct *mpnt, unsigned long pgoff)
{
    do {
        struct mm_struct *mm = mpnt->vm_mm;
        unsigned long start = mpnt->vm_start;
        unsigned long end = mpnt->vm_end;
        unsigned long len = end - start;
        unsigned long diff;
        if (mpnt->vm_pgoff >= pgoff) {
            zap_page_range(mm, start, len);
            continue;
        }
        len = len >> PAGE_SHIFT;
        diff = pgoff - mpnt->vm_pgoff;
        if (diff >= len)
            continue;
        start += diff << PAGE_SHIFT;
        len = (len - diff) << PAGE_SHIFT;
        zap_page_range(mm, start, len);
    } while ((mpnt = mpnt->vm_next_share) != NULL);
}
/* vmtruncate的替换函数 */
int _vmtruncate(struct inode * inode, loff_t offset)
{
    unsigned long pgoff;
    struct address_space *mapping = inode->i_mapping;
    unsigned long limit;
    /* 在该函数中我们增加了许多判断参数的打印信息 */
    printk (KERN_ALERT "Enter into my vmtruncate, pid: %d\n",
                current->pid);
    printk (KERN_ALERT "inode->i_ino: %d, inode->i_size: %d, pid: %d\n",
                            inode->i_ino, inode->i_size, current->pid);
    printk (KERN_ALERT "offset: %ld, pid: %d\n", offset, current->pid);
    printk (KERN_ALERT "Do nothing, pid: %d\n", current->pid);
    return 0;
    if (inode->i_size < offset)
        goto do_expand;
    inode->i_size = offset;
    spin_lock(&mapping->i_shared_lock);
    if (!mapping->i_mmap && !mapping->i_mmap_shared)
        goto out_unlock;
    pgoff = (offset + PAGE_CACHE_SIZE - 1) >> PAGE_CACHE_SHIFT;
    printk (KERN_ALERT "Begin to truncate mmap list, pid: %d\n",
        current->pid);
    if (mapping->i_mmap != NULL)
        _vmtruncate_list(mapping->i_mmap, pgoff);
    if (mapping->i_mmap_shared != NULL)
        _vmtruncate_list(mapping->i_mmap_shared, pgoff);
out_unlock:
    printk (KERN_ALERT "Before to truncate inode pages, pid:%d\n",
        current->pid);
    spin_unlock(&mapping->i_shared_lock);
    truncate_inode_pages(mapping, offset);
    goto out_truncate;
do_expand:
    limit = current->rlim[RLIMIT_FSIZE].rlim_cur;
    if (limit != RLIM_INFINITY && offset > limit)
        goto out_sig;
    if (offset > inode->i_sb->s_maxbytes)
        goto out;
    inode->i_size = offset;
out_truncate:
    printk (KERN_ALERT "Come to out_truncate, pid: %d\n",
        current->pid);
    if (inode->i_op && inode->i_op->truncate) {
        lock_kernel();
        inode->i_op->truncate(inode);
        unlock_kernel();
    }
    printk (KERN_ALERT "Leave, pid: %d\n", current->pid);
    return 0;
out_sig:
    send_sig(SIGXFSZ, current, 0);
out:
    return -EFBIG;
}
/* 核心中内存拷贝的函数，用于拷贝替换代码 */
void* _memcpy (void *dest, const void *src, int size)
{
    const char *p = src;
    char *q = dest;
    int i;
    for (i=0; i<size; i++) *q++ = *p++;
    return dest;
} 
int init_module (void)
{
    *(long *)&code[1] = (long)_vmtruncate; /* 赋替换函数地址 */
    _memcpy (orig_code, orig_vmtruncate, CODESIZE);   
    _memcpy (orig_vmtruncate, code, CODESIZE);    // move 新函数地址 %eax   jmp %eax
    return 0;
}
void cleanup_module (void)
{
    /* 卸载该核心模块时，恢复原来的vmtruncate函数 */
    _memcpy (orig_vmtruncate, orig_code, CODESIZE);
}