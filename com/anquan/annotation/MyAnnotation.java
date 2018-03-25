package com.anquan.annotation;

/* https://blog.csdn.net/briblue/article/details/73824058
 * 一、元注解:用来修饰自己注解的基本注解
 * 	  1.@Retention
 * 		解释：
 *			说明自己注解的的存活时间		 
 * 		取值：
 * 			@Retention(RetentionPolicy.SOURCE)   //注解仅存在于源码中，在class字节码文件中不包含
			@Retention(RetentionPolicy.CLASS)    // 默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得，
			@Retention(RetentionPolicy.RUNTIME)  // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
 * 	  2.@Documented
		解释：
			能够将注解中的元素包含到 Javadoc 中去
 * 	  3.@Target
 * 		解释：
 * 			设置自己的注解能用到哪个地方
 * 		取值：
 * 			ElementType.ANNOTATION_TYPE 可以给一个注解进行注解
			ElementType.CONSTRUCTOR 可以给构造方法进行注解
			ElementType.FIELD 可以给属性进行注解
			ElementType.LOCAL_VARIABLE 可以给局部变量进行注解
			ElementType.METHOD 可以给方法进行注解
			ElementType.PACKAGE 可以给一个包进行注解
			ElementType.PARAMETER 可以给一个方法内的参数进行注解
			ElementType.TYPE 可以给一个类型进行注解，比如类、接口、枚举
			
			
 *	  4.@Inherited
 *		解释：@Inherited修饰了@Test注解，@Test注解的类A，而B继承A，那么类B同样拥有注解@Test
 *		举例：
 *			@Inherited
			@interface Test {}


			@Test
			public class A {}

			public class B extends A {}			
 *
 *	  5.@Repeatable
 *		解释：
 *			@Person是一个注解，@Persons是一个容器注解，因为它能存放@Person注解
 *			Persons 是一张总的标签，上面贴满了 Person 这种同类型但内容不一样的标签。
 *			把 Persons 给一个 SuperMan 贴上，相当于同时给他贴了程序员、产品经理、画家的标签
 *		举例：
 *			@interface Persons {  
    		Person[]  value();
			}


			@Repeatable(Persons.class)
			@interface Person{
    		String role default "";
			}
			
			//@Person(role=”PM”)，就是给 Person 这个注解的 role 属性赋值为 PM
			@Person(role="artist")    
			@Person(role="coder")
			@Person(role="PM")
			public class SuperMan{
			
			}
 */
 
/*二、注解的属性
 *	注解只有成员变量，没有方法。
 *	注解的成员变量在注解的定义中以“无形参的方法”形式来声明，
 *	其方法名定义了该成员变量的名字，其返回值定义了该成员变量的类型。
 *	注解的属性时类型必须是 8 种基本数据类型外加 类、接口、注解及它们的数组
 *	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface TestAnnotation {
		int id()     default -1;;
    	String msg() default "Hi";
	}
	
	使用TestAnnotation注解时应当给属性赋值
		@TestAnnotation(id=3,msg="hello annotation")
		public class Test {
		
		}
 */	
 


/*三、注解与反射
 *注解通过反射获取。首先可以通过 Class 对象的 isAnnotationPresent() 方法判断它是否应用了某个注解
 *	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {}
 *
 *然后通过 getAnnotation() 方法来获取 Annotation 对象。
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {}
 *或者是 getAnnotations() 方法。
 	public Annotation[] getAnnotations() {}

 *前一种方法返回指定类型的注解，后一种方法返回注解到这个元素上的所有注解		
 */





