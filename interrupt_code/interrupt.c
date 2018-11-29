
/*
	�жϴ�������һ�������ĳ���豸����������
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


//	����ע��һ���豸ʱ����Ҫ����һ��IRQ��ͬʱע���жϴ�������
/*
	irq��Ҫ�����Ӳ���жϺ�
	irq_handler_t ����ָ��
				��һ���ص��������жϷ���ʱ��ϵͳ�������������dev_id�����������ݸ�����
	flags: ��־λ
				����ȡIRQF_DISABLED��IRQF_SHARED��IRQF_SAMPLE_RANDOM֮һ��
				IRQF_SHARED���ñ�־��ʾ����豸����һ��IRQ�ߣ������Ӧ��ÿ���豸����Ҫ���Ե��жϷ������̡�
				һ��ĳ���ж����ϵ��жϷ��������ִ��ʱ�����λ�����ͬ���ж����ϵ��ж�����,
				IRQF_DISABLED��־������ִ�и��жϷ������ʱ�����������������жϡ�
				IRQF_SAMPLE_RANDOM���ʾ�豸���Ա��������¼�����ķ���Դ��
	
	devname�����ж����ƣ�ͨ�����豸�������������  ��cat /proc/interrupts�п��Կ��������ơ�
	
	���������Ϊһ��ָ���ͱ�����ע��˲���Ϊvoid�ͣ�Ҳ����˵ͨ��ǿ��ת������ת��Ϊ�������͡�
	���������IRQF_SHARED��־ʱʹ�ã�Ŀ����Ϊ����Ҫ�ͷ��жϴ�������ṩΨһ��־����Ϊ����豸����һ���ж��ߣ����Ҫ�ͷ�ĳ���жϴ������ʱ������ͨ���˱�־��Ψһָ������жϴ������
	ϰ���ϣ���������������һ�����豸���������Ӧ���豸�ṹ��ָ��
*/
static int request_irq(unsigned int irq, irq_handler_t handler, unsigned long flags,const char *name, void *dev);


	

/*
	ע���жϴ�������
*/
static void __exit myirq_exit()
{
	printk("Module is leaving..\n");
	free_irq(irq,&mydev);
	printk("%s request IRQ:%d success..\n",devname,irq);
}

������ж��߲��ǹ���ģ���ô�ú������ͷ��жϴ�������ͬʱҲ�����ô����ж��ߡ�
����ǹ����ж��ߣ�ֻ���ͷ���mydev��Ӧ���жϴ�����򡣳��Ǹ��жϴ������ǡ��Ϊ���ж����ϵ����һԱ�������ж��߲Żᱻ����


//�жϴ�������
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


//���ں�ģ���ڲ���ʱ����Ҫ��������
static int irq;
static char* devname;

module_param(devname,charp,0644);
module_param(irq,int,0644);

ʹ�÷�����

1.ͨ��cat /proc/interrupts�鿴�жϺţ���ȷ��һ������Ҫ������жϺš���������Ϊ������̹���1���ж��ߣ����irq=1��

2.ʹ����������Ϳ��Բ����ںˣ�

sudo insmod filename.ko irq=1 devname=myirq

3.�ٴβ鿴/proc/interrupts�ļ������Է���1���ж��߶�Ӧ�ĵ��豸��������myirq�豸����

4.dmesg�鿴�ں���־�ļ����ɿ������жϴ������������ʾ����Ϣ��

5.ж���ں�ģ��