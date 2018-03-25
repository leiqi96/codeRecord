package com.anquan.annotation;

/*
 * 如何获取注解在类上的注解
 * 
 */

@TestAnnotation()
public class GetClassAnnotation {
	    public static void main(String[] args) {

	        boolean hasAnnotation = GetClassAnnotation.class.isAnnotationPresent(TestAnnotation.class);

	        if ( hasAnnotation ) {
	            TestAnnotation testAnnotation = GetClassAnnotation.class.getAnnotation(TestAnnotation.class);

	            System.out.println("id:"+testAnnotation.id());
	            System.out.println("msg:"+testAnnotation.msg());
	        }

	    }

}
