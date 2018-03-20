package mjunit;


import   static  org.junit.Assert.assertEquals;
import  org.junit.Test;
import  org.junit.runner.RunWith;
import  org.junit.runners.Parameterized;
import  org.junit.runners.Parameterized.Parameters;
import  java.util.Arrays;
import  java.util.Collection;


/*���ƣ�Runner����������
 *���ͣ����Դ��뽻��Junit��ܺ���Runner��������Щ����
 * 	  Junit��һ��Ĭ��Runner
 * @RunWith �������������
 * @RunWith(Parameterized.class)��ʾ��ParameterizedRunner�����в��Դ���
 * ֻҪ��һ����ָ���� Runner ����ô������е����к���������� Runner ������
 */

 
/**���ƣ�����������
 * ���ͣ���������Աʹ�ò�ͬ��ֵ��������ͬһ�����Ժ���
 * ������贴������������
 * 	 1.�� @RunWith(Parameterized.class) ��ע�� test �ࡣ
 * 	 2.����һ���� @Parameters ע�͵Ĺ����ľ�̬������������һ������ļ���(����)����Ϊ�������ݼ��ϡ�
 * 	 3.����һ�������Ĺ��캯���������ܺ�һ�в����������ͬ�Ķ�����
 * 	 4.Ϊÿһ�в������ݴ���һ��ʵ��������
 * 	 5.��ʵ��������Ϊ�������ݵ���Դ��������Ĳ���������
 */

@RunWith(Parameterized.class)
public class CalculatorParameterTest  {
	
    private static Calculator calculator  =   new  Calculator();
     
    //��������������һ�����ڴ�Ų�����һ�����ڴ���ڴ��Ľ����
    private int param;
    private int result;

    @Parameters
    public static Collection<Object[]> data() {
         return  Arrays.asList( new  Object[][] {
                 { 2 ,  4 } ,

                 { 0 ,  0 } ,

                 {-3 ,  9 } ,
        } );
    } 

    // ���캯�����Ա������г�ʼ�� 
    public  CalculatorParameterTest(int param,  int  result)  {
         this .param  =  param;
         this .result  =  result;
    } 

    @Test
     public void testSquare()  {
        calculator.square(param);
        assertEquals(result, calculator.getResult());
    } 
    
} 

