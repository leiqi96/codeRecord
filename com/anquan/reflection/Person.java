package com.anquan.reflection;

public class Person {  
    public String name;  
    private int age;  
    
    //����һ��˽�з���  
    private void privateMthod(){  
    }  
    
    public Person() {  
        System.out.println("�޲ι�����");  
    }  
  
    public Person(String name, int age) {  
        System.out.println("�вι�����");  
        this.name = name;  
        this.age = age;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }
    
    /** 
     *  
     * @param age  ������Integer������int 
     */  
    public void setName(String name , int age){  
        System.out.println("name: " + name);  
        System.out.println("age:"+ age);  
  
    } 
  
    public int getAge() {  
        return age;  
    }  
  
    public void setAge(int age) {  
        this.age = age;  
    }  
  
 
    public String toString() {  
        return "Person{" +  
                "name='" + name + '\'' +  
                ", age=" + age +  
                '}';  
    }  
}  