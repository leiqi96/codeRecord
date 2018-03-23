package com.java.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

public class TestPerson2 {

	/** 
	 * Field: 封装了字段的信息 
	 */  
	@Test  
	public void testField() throws  
	        ClassNotFoundException, NoSuchFieldException, IllegalAccessException {  
	  
	    Class<?> clazz = Class.forName("com.java.reflection.Person2");  
	  
	    //1、获取字段  
	    //1.1 获取Field的数组,私有字段也能获取  
	    Field[] fields = clazz.getDeclaredFields();
	    System.out.print("Class Field Name:");
	    for (Field field: fields) {  
	        System.out.print(field.getName() + ", ");  
	    }  
	  
	    //1.2 获取指定名字的Field（如果是私有的，见下面的4)  
	    Field nameField = clazz.getDeclaredField("name");  
	    System.out.println("\n获取指定Field名=: " + nameField.getName());  
	  
	    Person2 person2 = new Person2("ABC", 12);  
	    //2、获取指定对象的Field的值  
	    Object val = nameField.get(person2);  
	    System.out.println("获取指定对象字段'name'的Field的值=： " + val);  
	  
	    //3、设置指定对象的Field的值  
	    nameField.set(person2, "changwen2");  
	    System.out.println("设置指定对象字段'name'的Field的值=: " + person2.name);  
	  
	    //4、若该字段是私有的，需要调用setAccessible(true)方法  
	    Field ageField = clazz.getDeclaredField("age");  
	    ageField.setAccessible(true);  
	    System.out.println("获取指定私有字段名=: " + ageField.getName());  
	} 

}
