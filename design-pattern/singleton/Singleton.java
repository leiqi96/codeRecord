/*
 * 单例模式确保一个类只有一个实例，并提供一个全局访问点。
 * 	     如线程池(threadpool)、缓存(cache)、对话框、处理偏好设置和注册表的对象、日志对象等
*/

/*
 * Danger!  This implementation of Singleton not
 * guaranteed to work prior to Java 5
*/

// 双重检查枷锁
class Singleton{ 
	//volatile关键词确保，当singleton变量被初始化成Singleton实例时，多个线程正确处理singleton变量 
	private volatile static Singleton singleton; 
	
	private Singleton(){} 
	public static Singleton getInstance(){ 
		//如果不存在，就进入同步区块 
		if (singleton==null){ 
			//只有第一次才彻底执行这里的代码 		
			synchronized (Singleton.class){ 
				if(singleton==null){ 
					singleton=new Singleton(); 
				} 
			}
		}
		return singleton; 
	}
}




