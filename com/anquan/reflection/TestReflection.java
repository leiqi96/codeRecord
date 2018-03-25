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
 *����Class
 *1��Class��һ���࣬һ����������ࣨҲ���������౾������װ������������Method�������ֶε�Filed��������������Constructor������
 *2�������վ��Ӻ󣨷��䣩���Եõ�����Ϣ��ĳ��������ݳ�Ա���������͹�������ĳ���ൽ��ʵ������Щ�ӿڡ�
 *3������ÿ������ԣ�JRE ��Ϊ�䱣��һ������� Class ���͵Ķ��� һ�� Class ����������ض�ĳ������й���Ϣ��
 *4��Class ����ֻ����ϵͳ��������
 *5��һ������ JVM ��ֻ����һ��Classʵ�� 
 */


public class TestReflection {
	
	private static final String			TAB								= "   ";
	
	/*
	 * ������ƻ�ȡ������ַ���
	 * 	  1.ֱ��ͨ������.Class�ķ�ʽ�õ�
	 * 	  2.ͨ�������getClass()������ȡ
	 * 	  3.ͨ��ȫ������ȡ���õıȽ϶࣬�������׳�ClassNotFoundException�쳣
	 */
	@Test
	public void testGetClass() throws ClassNotFoundException {
	    System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
	    System.out.println("������ƻ�ȡ������ַ���");
	    
	    Class<?>  clazz = null;
	
	    //1 ֱ��ͨ������.Class�ķ�ʽ�õ�  
	    clazz = Person.class;  
	    System.out.println(TAB+"ͨ������: " + clazz);  
	  
	    //2 ͨ�������getClass()������ȡ,���ʹ�õ��٣�һ���Ǵ�����Object����֪����ʲô���͵�ʱ����ã�  
	    Object obj = new Person();  
	    clazz = obj.getClass();  
	    System.out.println(TAB+"ͨ��getClass(): " + clazz);  
	  
	    //3 ͨ��ȫ������ȡ���õıȽ϶࣬�������׳�ClassNotFoundException�쳣  
	    clazz = Class.forName("com.java.reflection.Person");  
	    System.out.println(TAB+"ͨ��ȫ������ȡ: " + clazz);  
	    

	}
	
	
	/*
	 * ����newInstance�������󣺵��õ���������޲εĹ�����
	 */
	@Test
	public void testNewInstance()  
	        throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		System.out.println("����newInstance�������󣺵��õ���������޲εĹ�����");
	    
		Class<?> clazz = Class.forName("com.java.reflection.Person");  
	  
	    //ʹ��Class���newInstance()�����������һ������  
	    //ʵ�ʵ��õ�����Ǹ� �޲����Ĺ������������Ϊʲôд�����ʱ��Ҫдһ���޲����Ĺ����������Ǹ������õģ�  
	    //һ��ģ�һ�����������˴������Ĺ�������ҲҪ����һ���޲����Ĺ�����  
	    Object obj = clazz.newInstance();  
	    System.out.println(TAB+obj);
	    
	}  
	
	/** 
	 * ����ClassLoader��װ���� 
	 */  
	@Test
	public void testClassLoader1() throws ClassNotFoundException, IOException {
		System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		System.out.println("����ClassLoader��װ���� ");
		
	    //1����ȡһ��ϵͳ���������  
	    ClassLoader classLoader = ClassLoader.getSystemClassLoader();  
	    System.out.println(TAB+"ϵͳ���������-->" + classLoader);  
	  
	    //2����ȡϵͳ��������ĸ��������(��չ���������extensions classLoader��)  
	    classLoader = classLoader.getParent();  
	    System.out.println(TAB+"��չ�������-->" + classLoader);  
	  
	    //3����ȡ��չ��������ĸ��������  
	    //���ΪNull,�޷���Java����ֱ������  
	    classLoader = classLoader.getParent();  
	    System.out.println(TAB+"�����������-->" + classLoader);  
	   
	    //4�����Ե�ǰ�����ĸ�����������м��� ,�������ϵͳ���������  
	    classLoader = Class.forName("com.java.reflection.Person").getClassLoader();  
	    System.out.println(TAB+"��ǰ�����ĸ�����������м���-->"+classLoader);  
	  
	    //5������JDK�ṩ��Object�����ĸ��������������ص�  
	    classLoader = Class.forName("java.lang.Object").getClassLoader();  
	    System.out.println(TAB+"JDK�ṩ��Object�����ĸ������������-->" + classLoader);
	   
	} 
	
	
    @Test  
    public void testGetResourceAsStream() throws ClassNotFoundException, IOException {  
    	/*  
    	 * InputStream in = new FileInputStream("test.properties");  
    	 * ��ôд�Ļ����ļ���Ҫ�ŵ�srcĿ¼��
    	 * 
    	 */
    	System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");;

        //5���������������һ����Ҫ����  
        //����getResourceAsStream ��ȡ��·���µ��ļ���Ӧ��������  
        InputStream in =  this.getClass().getClassLoader().getResourceAsStream("com/java/reflection/test.properties");  
        System.out.println("in: " +in);  
  
        Properties properties = new Properties();  
        properties.load(in);  
  
        String driverClass = properties.getProperty("dirver");  
        String jdbcUrl = properties.getProperty("jdbcUrl");  
        
        //���Ŀ��ܻ�������룬��Ҫת��һ��  
        String user = properties.getProperty("user");  
        //String user = new String(properties.getProperty("user").getBytes("ISO-8859-1"), "UTF-8");  
        String password = properties.getProperty("password");  
  
        System.out.println("diverClass: "+driverClass);  
        System.out.println("user: " + user);  
    }  
    
    /*
     * ��ȡһ����ķ���
     */
    @Test  
    public void testMethod() throws ClassNotFoundException, NoSuchMethodException,   
            IllegalAccessException, InstantiationException, InvocationTargetException {  
    	System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    	System.out.println("��ȡһ����ķ��� ");
    	Class<?> clazz = Class.forName("com.java.reflection.Person");  
      
        //1���õ�clazz ��Ӧ����������Щ����,���ܻ�ȡprivate����  
        Method[] methods =clazz.getMethods();  
        System.out.print("        getMethods: ");  
        for (Method method : methods){  
            System.out.print(method.getName() + ", ");  
        }  
      
        //2����ȡ���еķ���(��ֻ��ȡ����������ķ���������private������  
        Method[] methods2 = clazz.getDeclaredMethods();  
        System.out.print("\ngetDeclaredMethods: ");  
        for (Method method : methods2){  
            System.out.print(method.getName() + ", ");  
        }  
      
        //3����ȡָ���ķ���  
        Method method = clazz.getDeclaredMethod("setName",String.class);//��һ�������Ƿ�������������Ƿ�����Ĳ���  
        System.out.println("\nmethod : " + method);  
      
        Method method2 = clazz.getDeclaredMethod("setName",String.class ,int.class);//��һ�������Ƿ�������������Ƿ�����Ĳ���  
        System.out.println("method2: " + method2);  
      
        //4��ִ�з�����  
        Object obj = clazz.newInstance();  
        method2.invoke(obj, "changwen", 22);  
    } 
    
    /** 
     * �������������õıȽ��� 
     */  
    @Test  
    public void testConstructor() throws ClassNotFoundException, NoSuchMethodException,  
            IllegalAccessException, InvocationTargetException, InstantiationException {
    	System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    	System.out.println("��ȡ������");
        String className = "com.java.reflection.Person2";  
        Class<Person2> clazz = (Class<Person2>) Class.forName(className);  
      
        //1.��ȡConstructor����  
        Constructor<Person2>[] constructors =  
                (Constructor<Person2>[]) Class.forName(className).getConstructors();  
      
        for (Constructor<Person2> constructor: constructors) {  
            System.out.println(TAB+constructor);  
        }  
      
        Constructor<Person2> constructor2 = clazz.getConstructor(String.class, Integer.class);  
        System.out.println(TAB+"ָ����-->" + constructor2);  
      
        //2.���ù�������newInstance()������������  
        Object obj= constructor2.newInstance("changwen", 11);  
    }  
	
    //execute only once, in the end
    @AfterClass
    public static void  afterClass() {
    	System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    }
	
	

}
