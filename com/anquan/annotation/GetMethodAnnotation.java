package com.anquan.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

@TestAnnotation(msg="hello")
public class GetMethodAnnotation {
    @Check(value="hi")
    int a;

    @Check(value="hi")
    @Perform
    public void testMethod(){}



    public static void main(String[] args) {

        boolean hasAnnotation = GetMethodAnnotation.class.isAnnotationPresent(TestAnnotation.class);

        if ( hasAnnotation ) {
            TestAnnotation testAnnotation = GetMethodAnnotation.class.getAnnotation(TestAnnotation.class);
            //��ȡ���ע��
            System.out.println("id:"+testAnnotation.id());
            System.out.println("msg:"+testAnnotation.msg());
        }


        try {
            Field a = GetMethodAnnotation.class.getDeclaredField("a");
            a.setAccessible(true);
            //��ȡһ����Ա�����ϵ�ע��
            Check check = a.getAnnotation(Check.class);

            if ( check != null ) {
                System.out.println("check value:"+check.value());
            }

            Method testMethod = GetMethodAnnotation.class.getDeclaredMethod("testMethod");

            if ( testMethod != null ) {
                // ��ȡ�����е�ע��
                Annotation[] ans = testMethod.getAnnotations();
                for( int i = 0;i < ans.length;i++) {
                    System.out.println("method GetMethodAnnotationMethod annotation:"+ans[i].annotationType().getSimpleName());
                }
            }
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
        }



    }
}
