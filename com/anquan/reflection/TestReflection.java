package com.anquan.reflection;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Properties;
import org.junit.AfterClass;
import org.junit.Test;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/*
 *关于Class
 *1、Class是一个类，一个描述类的类（也就是描述类本身），封装了描述方法的Method，描述字段的Filed，描述构造器的Constructor等属性
 *2、对象照镜子后（反射）可以得到的信息：某个类的数据成员名、方法和构造器、某个类到底实现了哪些接口。
 *3、对于每个类而言，JRE 都为其保留一个不变的 Class 类型的对象。 一个 Class 对象包含了特定某个类的有关信息。
 *4、Class 对象只能由系统建立对象
 *5、一个类在 JVM 中只会有一个Class实例 
 */


public class TestReflection {
	
	private static final String			TAB								= "   ";
	
	/*
	 * 反射机制获取类的三种方法
	 * 	  1.直接通过类名.Class的方式得到
	 * 	  2.通过对象的getClass()方法获取
	 * 	  3.通过全类名获取，用的比较多，但可能抛出ClassNotFoundException异常
	 */
	@Test
	public void testGetClass() throws ClassNotFoundException {
	    System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
	    System.out.println("反射机制获取类的三种方法");
	    
	    Class<?>  clazz = null;
	
	    //1 直接通过类名.Class的方式得到  
	    clazz = Person.class;  
	    System.out.println(TAB+"通过类名: " + clazz);  
	  
	    //2 通过对象的getClass()方法获取,这个使用的少（一般是传的是Object，不知道是什么类型的时候才用）  
	    Object obj = new Person();  
	    clazz = obj.getClass();  
	    System.out.println(TAB+"通过getClass(): " + clazz);  
	  
	    //3 通过全类名获取，用的比较多，但可能抛出ClassNotFoundException异常  
	    clazz = Class.forName("com.java.reflection.Person");  
	    System.out.println(TAB+"通过全类名获取: " + clazz);  
	    

	}
	
	
	/*
	 * 利用newInstance创建对象：调用的类必须有无参的构造器
	 */
	@Test
	public void testNewInstance()  
	        throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		System.out.println("利用newInstance创建对象：调用的类必须有无参的构造器");
	    
		Class<?> clazz = Class.forName("com.java.reflection.Person");  
	  
	    //使用Class类的newInstance()方法创建类的一个对象  
	    //实际调用的类的那个 无参数的构造器（这就是为什么写的类的时候，要写一个无参数的构造器，就是给反射用的）  
	    //一般的，一个类若声明了带参数的构造器，也要声明一个无参数的构造器  
	    Object obj = clazz.newInstance();  
	    System.out.println(TAB+obj);
	    
	}  
	
	/** 
	 * 三种ClassLoader类装载器 
	 */  
	@Test
	public void testClassLoader1() throws ClassNotFoundException, IOException {
		System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		System.out.println("三种ClassLoader类装载器 ");
		
	    //1、获取一个系统的类加载器  
	    ClassLoader classLoader = ClassLoader.getSystemClassLoader();  
	    System.out.println(TAB+"系统的类加载器-->" + classLoader);  
	  
	    //2、获取系统类加载器的父类加载器(扩展类加载器（extensions classLoader）)  
	    classLoader = classLoader.getParent();  
	    System.out.println(TAB+"扩展类加载器-->" + classLoader);  
	  
	    //3、获取扩展类加载器的父类加载器  
	    //输出为Null,无法被Java程序直接引用  
	    classLoader = classLoader.getParent();  
	    System.out.println(TAB+"启动类加载器-->" + classLoader);  
	   
	    //4、测试当前类由哪个类加载器进行加载 ,结果就是系统的类加载器  
	    classLoader = Class.forName("com.java.reflection.Person").getClassLoader();  
	    System.out.println(TAB+"当前类由哪个类加载器进行加载-->"+classLoader);  
	  
	    //5、测试JDK提供的Object类由哪个类加载器负责加载的  
	    classLoader = Class.forName("java.lang.Object").getClassLoader();  
	    System.out.println(TAB+"JDK提供的Object类由哪个类加载器加载-->" + classLoader);
	   
	} 
	
	
    @Test  
    public void testGetResourceAsStream() throws ClassNotFoundException, IOException {  
    	/*  
    	 * InputStream in = new FileInputStream("test.properties");  
    	 * 这么写的话，文件需要放到src目录下
    	 * 
    	 */
    	System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");;

        //5、关于类加载器的一个主要方法  
        //调用getResourceAsStream 获取类路径下的文件对应的输入流  
        InputStream in =  this.getClass().getClassLoader().getResourceAsStream("com/java/reflection/test.properties");  
        System.out.println("in: " +in);  
  
        Properties properties = new Properties();  
        properties.load(in);  
  
        String driverClass = properties.getProperty("dirver");  
        String jdbcUrl = properties.getProperty("jdbcUrl");  
        
        //中文可能会出现乱码，需要转换一下  
        String user = properties.getProperty("user");  
        //String user = new String(properties.getProperty("user").getBytes("ISO-8859-1"), "UTF-8");  
        String password = properties.getProperty("password");  
  
        System.out.println("diverClass: "+driverClass);  
        System.out.println("user: " + user);  
    }  
    
    /*
     * 获取一个类的方法
     */
    @Test  
    public void testMethod() throws ClassNotFoundException, NoSuchMethodException,   
            IllegalAccessException, InstantiationException, InvocationTargetException {  
    	System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    	System.out.println("获取一个类的方法 ");
    	Class<?> clazz = Class.forName("com.java.reflection.Person");  
      
        //1、得到clazz 对应的类中有哪些方法,不能获取private方法  
        Method[] methods =clazz.getMethods();  
        System.out.print("        getMethods: ");  
        for (Method method : methods){  
            System.out.print(method.getName() + ", ");  
        }  
      
        //2、获取所有的方法(且只获取这个类声明的方法，包括private方法）  
        Method[] methods2 = clazz.getDeclaredMethods();  
        System.out.print("\ngetDeclaredMethods: ");  
        for (Method method : methods2){  
            System.out.print(method.getName() + ", ");  
        }  
      
        //3、获取指定的方法  
        Method method = clazz.getDeclaredMethod("setName",String.class);//第一个参数是方法名，后面的是方法里的参数  
        System.out.println("\nmethod : " + method);  
      
        Method method2 = clazz.getDeclaredMethod("setName",String.class ,int.class);//第一个参数是方法名，后面的是方法里的参数  
        System.out.println("method2: " + method2);  
      
        //4、执行方法！  
        Object obj = clazz.newInstance();  
        method2.invoke(obj, "changwen", 22);  
    } 
    
    /** 
     * 构造器：开发用的比较少 
     */  
    @Test  
    public void testConstructor() throws ClassNotFoundException, NoSuchMethodException,  
            IllegalAccessException, InvocationTargetException, InstantiationException {
    	System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    	System.out.println("获取构造器");
        String className = "com.java.reflection.Person2";  
        Class<Person2> clazz = (Class<Person2>) Class.forName(className);  
      
        //1.获取Constructor对象  
        Constructor<Person2>[] constructors =  
                (Constructor<Person2>[]) Class.forName(className).getConstructors();  
      
        for (Constructor<Person2> constructor: constructors) {  
            System.out.println(TAB+constructor);  
        }  
      
        Constructor<Person2> constructor2 = clazz.getConstructor(String.class, Integer.class);  
        System.out.println(TAB+"指定的-->" + constructor2);  
      
        //2.调用构造器的newInstance()方法创建对象  
        Object obj= constructor2.newInstance("changwen", 11);  
    }  
	
    //execute only once, in the end
    @AfterClass
    public static void  afterClass() {
    	System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    }
	
	

}
