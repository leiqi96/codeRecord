package com.java.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

public class TestPerson2 {

	/** 
	 * Field: ��װ���ֶε���Ϣ 
	 */  
	@Test  
	public void testField() throws  
	        ClassNotFoundException, NoSuchFieldException, IllegalAccessException {  
	  
	    Class<?> clazz = Class.forName("com.java.reflection.Person2");  
	  
	    //1����ȡ�ֶ�  
	    //1.1 ��ȡField������,˽���ֶ�Ҳ�ܻ�ȡ  
	    Field[] fields = clazz.getDeclaredFields();
	    System.out.print("Class Field Name:");
	    for (Field field: fields) {  
	        System.out.print(field.getName() + ", ");  
	    }  
	  
	    //1.2 ��ȡָ�����ֵ�Field�������˽�еģ��������4)  
	    Field nameField = clazz.getDeclaredField("name");  
	    System.out.println("\n��ȡָ��Field��=: " + nameField.getName());  
	  
	    Person2 person2 = new Person2("ABC", 12);  
	    //2����ȡָ�������Field��ֵ  
	    Object val = nameField.get(person2);  
	    System.out.println("��ȡָ�������ֶ�'name'��Field��ֵ=�� " + val);  
	  
	    //3������ָ�������Field��ֵ  
	    nameField.set(person2, "changwen2");  
	    System.out.println("����ָ�������ֶ�'name'��Field��ֵ=: " + person2.name);  
	  
	    //4�������ֶ���˽�еģ���Ҫ����setAccessible(true)����  
	    Field ageField = clazz.getDeclaredField("age");  
	    ageField.setAccessible(true);  
	    System.out.println("��ȡָ��˽���ֶ���=: " + ageField.getName());  
	} 

}
