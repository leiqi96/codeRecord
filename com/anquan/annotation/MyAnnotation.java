package com.anquan.annotation;

/* https://blog.csdn.net/briblue/article/details/73824058
 * һ��Ԫע��:���������Լ�ע��Ļ���ע��
 * 	  1.@Retention
 * 		���ͣ�
 *			˵���Լ�ע��ĵĴ��ʱ��		 
 * 		ȡֵ��
 * 			@Retention(RetentionPolicy.SOURCE)   //ע���������Դ���У���class�ֽ����ļ��в�����
			@Retention(RetentionPolicy.CLASS)    // Ĭ�ϵı������ԣ�ע�����class�ֽ����ļ��д��ڣ�������ʱ�޷���ã�
			@Retention(RetentionPolicy.RUNTIME)  // ע�����class�ֽ����ļ��д��ڣ�������ʱ����ͨ�������ȡ��
 * 	  2.@Documented
		���ͣ�
			�ܹ���ע���е�Ԫ�ذ����� Javadoc ��ȥ
 * 	  3.@Target
 * 		���ͣ�
 * 			�����Լ���ע�����õ��ĸ��ط�
 * 		ȡֵ��
 * 			ElementType.ANNOTATION_TYPE ���Ը�һ��ע�����ע��
			ElementType.CONSTRUCTOR ���Ը����췽������ע��
			ElementType.FIELD ���Ը����Խ���ע��
			ElementType.LOCAL_VARIABLE ���Ը��ֲ���������ע��
			ElementType.METHOD ���Ը���������ע��
			ElementType.PACKAGE ���Ը�һ��������ע��
			ElementType.PARAMETER ���Ը�һ�������ڵĲ�������ע��
			ElementType.TYPE ���Ը�һ�����ͽ���ע�⣬�����ࡢ�ӿڡ�ö��
			
			
 *	  4.@Inherited
 *		���ͣ�@Inherited������@Testע�⣬@Testע�����A����B�̳�A����ô��Bͬ��ӵ��ע��@Test
 *		������
 *			@Inherited
			@interface Test {}


			@Test
			public class A {}

			public class B extends A {}			
 *
 *	  5.@Repeatable
 *		���ͣ�
 *			@Person��һ��ע�⣬@Persons��һ������ע�⣬��Ϊ���ܴ��@Personע��
 *			Persons ��һ���ܵı�ǩ������������ Person ����ͬ���͵����ݲ�һ���ı�ǩ��
 *			�� Persons ��һ�� SuperMan ���ϣ��൱��ͬʱ�������˳���Ա����Ʒ�������ҵı�ǩ
 *		������
 *			@interface Persons {  
    		Person[]  value();
			}


			@Repeatable(Persons.class)
			@interface Person{
    		String role default "";
			}
			
			//@Person(role=��PM��)�����Ǹ� Person ���ע��� role ���Ը�ֵΪ PM
			@Person(role="artist")    
			@Person(role="coder")
			@Person(role="PM")
			public class SuperMan{
			
			}
 */
 
/*����ע�������
 *	ע��ֻ�г�Ա������û�з�����
 *	ע��ĳ�Ա������ע��Ķ������ԡ����βεķ�������ʽ��������
 *	�䷽���������˸ó�Ա���������֣��䷵��ֵ�����˸ó�Ա���������͡�
 *	ע�������ʱ���ͱ����� 8 �ֻ�������������� �ࡢ�ӿڡ�ע�⼰���ǵ�����
 *	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface TestAnnotation {
		int id()     default -1;;
    	String msg() default "Hi";
	}
	
	ʹ��TestAnnotationע��ʱӦ�������Ը�ֵ
		@TestAnnotation(id=3,msg="hello annotation")
		public class Test {
		
		}
 */	
 


/*����ע���뷴��
 *ע��ͨ�������ȡ�����ȿ���ͨ�� Class ����� isAnnotationPresent() �����ж����Ƿ�Ӧ����ĳ��ע��
 *	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {}
 *
 *Ȼ��ͨ�� getAnnotation() ��������ȡ Annotation ����
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {}
 *������ getAnnotations() ������
 	public Annotation[] getAnnotations() {}

 *ǰһ�ַ�������ָ�����͵�ע�⣬��һ�ַ�������ע�⵽���Ԫ���ϵ�����ע��		
 */





