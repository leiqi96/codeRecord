package com.wuanquan.models;

import java.util.Calendar;

import com.wuanquan.annotation.*;

/*
    create database dsm;
    create table student(
        student_id int(11) not null auto_increment,
        name varchar(32) not null,
        age int(11),
        birthday date,
        primary key(student_id)
    );
 */

@Table(name="student")
public class Student {
    @Id(name = "student_id")
    private int studentNo;
    @Column(name = "name")
    private String name;
    @Column(name = "age", type = "int")
    private int age;
    @Column(name = "birthday", type = "Calendar")
    private Calendar birthday;

    public Student(){
    	super(); 
    }
    
    public Student(int studentNo, String name, int age){
        this.studentNo = studentNo;
        this.name = name;
        this.age = age;
    }

    public int getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(int studentNo) {
        this.studentNo = studentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentNo=" + studentNo +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                '}';
    }
}