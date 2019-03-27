/*名称：模板方法
 *解释：在抽象类中定义一个算法的框架，算法的每一步实现由具体的子类去实现
 * 
 */

/*CaffeineBeverageWithHook抽象类中的prepareRecipe()定义了算法的框架
 * customerWantsCondiments()是一个钩子(hook)，在抽象类中默认返回True，
 * 而具体的子类会去覆盖这样的hook函数，它会根据实际情况返回True或Fals，
 * 从而子类就可以决定下一步addCondiments执不执行
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public abstract class CaffeineBeverageWithHook {
 
	void prepareRecipe() {
		boilWater();
		brew();
		pourInCup();
		if (customerWantsCondiments()) {    //这是一个hook
			addCondiments();
		}
	}
 
	abstract void brew();
	abstract void addCondiments();
 
	void boilWater() {
		System.out.println("Boiling water");
	}
 
	void pourInCup() {
		System.out.println("Pouring into cup");
	}
 
	boolean customerWantsCondiments() {  //在抽象类中这个hook默认返回true，其它啥事不干
		return true;
	}
}



/*
 * 这是子类CoffeeWithHook
 */
public class CoffeeWithHook extends CaffeineBeverageWithHook {
 
	public void brew() {
		System.out.println("Dripping Coffee through filter");
	}
 
	public void addCondiments() {
		System.out.println("Adding Sugar and Milk");
	}
 
	public boolean customerWantsCondiments() {   //这个子类实现的hook函数会根据用户的输入来返回True/False
		String answer = getUserInput();

		if (answer.toLowerCase().startsWith("y")) {
			return true;
		} else {
			return false;
		}
	}
 
	private String getUserInput() {
		String answer = null;

		System.out.print("Would you like milk and sugar with your coffee (y/n)? ");

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			answer = in.readLine();
		} catch (IOException ioe) {
			System.err.println("IO error trying to read your answer");
		}
		if (answer == null) {
			return "no";
		}
		return answer;
	}
}


