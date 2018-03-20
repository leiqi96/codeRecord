package mjunit;


import   static  org.junit.Assert.assertEquals;
import  org.junit.Test;
import  org.junit.runner.RunWith;
import  org.junit.runners.Parameterized;
import  org.junit.runners.Parameterized.Parameters;
import  java.util.Arrays;
import  java.util.Collection;


/*名称：Runner（运行器）
 *解释：测试代码交给Junit框架后，由Runner来调用这些代码
 * 	  Junit由一个默认Runner
 * @RunWith 是用来修饰类的
 * @RunWith(Parameterized.class)表示用ParameterizedRunner来运行测试代码
 * 只要对一个类指定了 Runner ，那么这个类中的所有函数都被这个 Runner 来调用
 */

 
/**名称：参数化测试
 * 解释：允许开发人员使用不同的值反复运行同一个测试函数
 * 五个步骤创建参数化测试
 * 	 1.用 @RunWith(Parameterized.class) 来注释 test 类。
 * 	 2.创建一个由 @Parameters 注释的公共的静态方法，它返回一个对象的集合(数组)来作为测试数据集合。
 * 	 3.创建一个公共的构造函数，它接受和一行测试数据相等同的东西。
 * 	 4.为每一列测试数据创建一个实例变量。
 * 	 5.用实例变量作为测试数据的来源来创建你的测试用例。
 */

@RunWith(Parameterized.class)
public class CalculatorParameterTest  {
	
    private static Calculator calculator  =   new  Calculator();
     
    //定义两个变量，一个用于存放参数，一个用于存放期待的结果。
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

    // 构造函数，对变量进行初始化 
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

