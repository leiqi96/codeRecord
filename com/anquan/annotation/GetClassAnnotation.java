package com.anquan.annotation;

/*
 * ��λ�ȡע�������ϵ�ע��
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
