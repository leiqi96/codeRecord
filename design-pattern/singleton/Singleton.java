/*
 * ����ģʽȷ��һ����ֻ��һ��ʵ�������ṩһ��ȫ�ַ��ʵ㡣
 * 	     ���̳߳�(threadpool)������(cache)���Ի��򡢴���ƫ�����ú�ע���Ķ�����־�����
*/

/*
 * Danger!  This implementation of Singleton not
 * guaranteed to work prior to Java 5
*/

// ˫�ؼ�����
class Singleton{ 
	//volatile�ؼ���ȷ������singleton��������ʼ����Singletonʵ��ʱ������߳���ȷ����singleton���� 
	private volatile static Singleton singleton; 
	
	private Singleton(){} 
	public static Singleton getInstance(){ 
		//��������ڣ��ͽ���ͬ������ 
		if (singleton==null){ 
			//ֻ�е�һ�βų���ִ������Ĵ��� 		
			synchronized (Singleton.class){ 
				if(singleton==null){ 
					singleton=new Singleton(); 
				} 
			}
		}
		return singleton; 
	}
}




