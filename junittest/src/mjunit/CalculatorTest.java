package mjunit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class CalculatorTest {

    private static Calculator calculator = new Calculator();
    
    //execute only once, in the starting
    @BeforeClass   
    public static void beforeClass() {
        System.out.println("in before class");
    }

    //execute only once, in the end
    @AfterClass
    public static void  afterClass() {
        System.out.println("in after class");
    }
    
    // @Before ����ÿִ��һ�β��Ժ���ǰ�����������Ҫִ�У����Ի����¶��
    @Before
    public void setUp() throws Exception {
        calculator.clear();
        System.out.println("in before ");
    }
    
    /*
     * @Test ��������һ�����Է��������������⣬����ֵvoid
     */
    @Test   
    public void testAdd() {
        calculator.add(2);
        calculator.add(3);
        assertEquals(5, calculator.getResult());
    }

    @Test
    public void testSubstract() {
        calculator.add(10);
        calculator.substract(2);
        assertEquals(8, calculator.getResult());
    }

    // @Ignore����"�˷�����δ��ɣ��ݲ�����˴β���
    @Ignore("Multiply() Not yet implemented")
    @Test
    public void testMultiply() {
    }

    @Test
    public void testDivide() {
        calculator.add(8);
        calculator.divide(2);
        assertEquals(4, calculator.getResult());
    }
    
    
    /**���ƣ���ʱ����
     * ���ͣ����Դ���������ѭ��������timeoutʱ���ϵͳ��ǿ��ֹͣ
     * 	   1000����1s
     */
    @Test(timeout = 1000)
    public void squareRoot()  {
       calculator.squareRoot( 4 );
       assertEquals( 2 , calculator.getResult());
   } 

    
    /**���ƣ������쳣
     * ���ͣ���������ʱ����׳��쳣������1/0ʱ�����׳��쳣�������Ҳ��Ҫ����
     * ��ע��expected��ָ��������Ҫ�׳����쳣
     */
    @Test(expected = ArithmeticException.class)
    public void divideByZero(){
    	calculator.divide( 0 ); 
    } 

 
    //@After ����ÿִ����һ�β��Ժ��������������Ҫ����һ�Σ�����Ҫִ�ж��
    @After
    public void tearDown() {
    	System.out.println("in after");
    }
    
}










