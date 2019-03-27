/*���ƣ�ģ�巽��
 *���ͣ��ڳ������ж���һ���㷨�Ŀ�ܣ��㷨��ÿһ��ʵ���ɾ��������ȥʵ��
 * 
 */

/*CaffeineBeverageWithHook�������е�prepareRecipe()�������㷨�Ŀ��
 * customerWantsCondiments()��һ������(hook)���ڳ�������Ĭ�Ϸ���True��
 * ������������ȥ����������hook�������������ʵ���������True��Fals��
 * �Ӷ�����Ϳ��Ծ�����һ��addCondimentsִ��ִ��
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
		if (customerWantsCondiments()) {    //����һ��hook
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
 
	boolean customerWantsCondiments() {  //�ڳ����������hookĬ�Ϸ���true������ɶ�²���
		return true;
	}
}



/*
 * ��������CoffeeWithHook
 */
public class CoffeeWithHook extends CaffeineBeverageWithHook {
 
	public void brew() {
		System.out.println("Dripping Coffee through filter");
	}
 
	public void addCondiments() {
		System.out.println("Adding Sugar and Milk");
	}
 
	public boolean customerWantsCondiments() {   //�������ʵ�ֵ�hook����������û�������������True/False
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


