package com.java.reflection;

import java.lang.reflect.Method;

public class InvokeUtlities {
	/** 
	  * @param className  ĳ�����ȫ���� 
	  * @param methodName ���һ�������ķ��������÷���Ҳ������˽�з��� 
	  * @param args  ���ø÷�����Ҫ����Ĳ��� ...�ɱ��������˼ 
	  * @return ���÷�����ķ���ֵ 
	  */  
	 public Object invoke(String className, String methodName, Object ... args) {  
	     Object obj = null;  
	     try {  
	         obj = Class.forName(className).newInstance();  
	         return invoke(obj, methodName, args);  
	  
	     } catch (InstantiationException e) {  
	         e.printStackTrace();  
	     } catch (IllegalAccessException e) {  
	         e.printStackTrace();  
	     } catch (ClassNotFoundException e) {  
	         e.printStackTrace();  
	     }  
	     return invoke(null, methodName, args);  
	 }  
	 /** 
	  * @param obj  ����ִ�е��Ǹ����� 
	  * @param methodName ���һ�������ķ��������÷���Ҳ������˽�з���,�������Ǹ÷����ڸ����ж����˽�з��� 
	  * @param args  ���ø÷�����Ҫ����Ĳ��� ...�ɱ��������˼ 
	  * @return ���÷�����ķ���ֵ 
	  */  
	 public Object invoke(Object obj, String methodName, Object ... args) {  
	     //1����ȡMethod����  
	     Class [] parameterTypes = new Class[args.length];  
	     for (int i=0 ; i<args.length; i++){  
	         parameterTypes[i] = args[i].getClass();  
	     }  
	  
	     try {  
	         //2��ִ��Method����  
	         Method method = getMethod(obj.getClass(), methodName,parameterTypes);  
	  
	         //ͨ������ִ��private����  
	         method.setAccessible(true);  
	  
	         //3�����ط����ķ���ֵ  
	         return method.invoke(obj,args);  
	  
	     } catch (Exception e) {  
	  
	     }  
	  
	     return null;  
	 }  
	  
	 /** 
	  * ��ȡclazz ��methodName ������ �÷���������˽�з������������Ǹ����е�˽�з��� 
	  */  
	 public Method getMethod(Class clazz, String methodName, Class ... parameterTypes) {  
	     //ע�����ѭ��������ݣ�����  
	     for (; clazz != Object.class; clazz = clazz.getSuperclass()){  
	         try {  
	             return clazz.getDeclaredMethod(methodName, parameterTypes);  
	         } catch (Exception e) { //����ҪдException����Ȼ�����Ӧ�����в����쳣û�в���  
	  
	         }  
	     }  
	     return null;  
	 }  
}
