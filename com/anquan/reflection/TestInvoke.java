package com.anquan.reflection;

import static org.junit.Assert.*;

import org.junit.Test;

import com.anquan.reflection.InvokeUtlities;

public class TestInvoke {

	@Test  
	public void testGetSuperClass() throws Exception {  
	    String className = "com.java.reflection.StudentInvoke";  
	  
	    Class<?> clazz = Class.forName(className);  
	    Class<?> superClazz = clazz.getSuperclass();  
	  
	    System.out.println(superClazz);  
	    //Êä³ö½á¹û£ºclass com.java.reflection.PersonInvoke  
	    System.out.println("-------------------------------------------");
	} 
	
	@Test  
	public void testInvoke2() {  
	    Object obj = new StudentInvoke();  
	    InvokeUtlities invokeUtlities = new InvokeUtlities();
	    invokeUtlities.invoke(obj, "method1", 10);  
	  
	    Object result = invokeUtlities.invoke(obj, "method2");  
	    System.out.println(result);  
	}  

}
