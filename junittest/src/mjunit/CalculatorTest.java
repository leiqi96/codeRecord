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
    
    // @Before 表明每执行一次测试函数前，这个函数都要执行，所以会重新多次
    @Before
    public void setUp() throws Exception {
        calculator.clear();
        System.out.println("in before ");
    }
    
    /*
     * @Test 表明这是一个测试方法，方法名任意，返回值void
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

    // @Ignore表明"此方法尚未完成，暂不参与此次测试
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
    
    
    /**名称：限时测试
     * 解释：如果源代码出现死循环，超出timeout时间后，系统会强制停止
     * 	   1000代表1s
     */
    @Test(timeout = 1000)
    public void squareRoot()  {
       calculator.squareRoot( 4 );
       assertEquals( 2 , calculator.getResult());
   } 

    
    /**名称：测试异常
     * 解释：代码中有时候会抛出异常，比如1/0时必须抛出异常，因此这也需要测试
     * 标注的expected是指定我们需要抛出的异常
     */
    @Test(expected = ArithmeticException.class)
    public void divideByZero(){
    	calculator.divide( 0 ); 
    } 

 
    //@After 表明每执行完一次测试函数，这个函数都要运行一次，所以要执行多次
    @After
    public void tearDown() {
    	System.out.println("in after");
    }
    
}










