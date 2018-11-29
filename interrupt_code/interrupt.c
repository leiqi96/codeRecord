
/*
	中断处理例程一般包含在某个设备驱动程序中
*/


static int __init myirq_init()
{
	printk("Module is working..\n");
	if(request_irq(irq,myirq_handler,IRQF_SHARED,devname,&mydev)!=0)
	{
		printk("%s request IRQ:%d failed..\n",devname,irq);
		return -1;
	}
	printk("%s rquest IRQ:%d success..\n",devname,irq);
	return 0;
}


//	驱动注册一个设备时，需要申请一个IRQ，同时注册中断处理例程
/*
	irq是要申请的硬件中断号
	irq_handler_t 函数指针
				是一个回调函数，中断发生时，系统调用这个函数，dev_id参数将被传递给它。
	flags: 标志位
				可以取IRQF_DISABLED、IRQF_SHARED和IRQF_SAMPLE_RANDOM之一。
				IRQF_SHARED，该标志表示多个设备共享一条IRQ线，因此相应的每个设备都需要各自的中断服务例程。
				一般某个中断线上的中断服务程序在执行时会屏蔽会屏蔽同条中断线上的中断请求,
				IRQF_DISABLED标志，则在执行该中断服务程序时会屏蔽所有其他的中断。
				IRQF_SAMPLE_RANDOM则表示设备可以被看做是事件随见的发生源。
	
	devname设置中断名称，通常是设备驱动程序的名称  在cat /proc/interrupts中可以看到此名称。
	
	第五个参数为一个指针型变量。注意此参数为void型，也就是说通过强制转换可以转换为任意类型。
	这个变量在IRQF_SHARED标志时使用，目的是为即将要释放中断处理程序提供唯一标志。因为多个设备共享一条中断线，因此要释放某个中断处理程序时，必须通过此标志来唯一指定这个中断处理程序。
	习惯上，会给这个参数传递一个与设备驱动程序对应的设备结构体指针
*/
static int request_irq(unsigned int irq, irq_handler_t handler, unsigned long flags,const char *name, void *dev);


	

/*
	注销中断处理例程
*/
static void __exit myirq_exit()
{
	printk("Module is leaving..\n");
	free_irq(irq,&mydev);
	printk("%s request IRQ:%d success..\n",devname,irq);
}

如果该中断线不是共享的，那么该函数在释放中断处理程序的同时也将禁用此条中断线。
如果是共享中断线，只是释放与mydev对应的中断处理程序。除非该中断处理程序恰好为该中断线上的最后一员，此条中断线才会被禁用


//中断处理例程
static irqreturn_t myirq_handler(int irq,void* dev)
{
	struct myirq mydev;
	static int count=1;
	mydev=*(struct myirq*)dev;
	printk("key: %d..\n",count);
	printk("devid:%d ISR is working..\n",mydev.devid);
	printk("ISR is leaving..\n");
	count++;
	return IRQ_HANDLED;
}


//本内核模块在插入时还需要附带参数
static int irq;
static char* devname;

module_param(devname,charp,0644);
module_param(irq,int,0644);

使用方法：

1.通过cat /proc/interrupts查看中断号，以确定一个即将要共享的中断号。本程序因为是与键盘共享1号中断线，因此irq=1；

2.使用如下命令就可以插入内核：

sudo insmod filename.ko irq=1 devname=myirq

3.再次查看/proc/interrupts文件，可以发现1号中断线对应的的设备名处多了myirq设备名；

4.dmesg查看内核日志文件，可看到在中断处理程序中所显示的信息；

5.卸载内核模块