/*
����-�ر�ԭ����Ӧ�ö���չ���������޸Ĺر�

װ�����뱻װ����������ͬһ��������߽ӿ�
���������߾���ͬ�������ͣ���Ϊװ������Ҫȡ����װ����
������Ϊ�˼��ɳ������Ϊ����Ϊ���Ծ����װ����
*/

/*
װ����ģʽ��Ӧ�ó���
1����Ҫ��չһ����Ĺ��ܡ�
2����̬��Ϊһ���������ӹ��ܣ����һ��ܶ�̬���������̳в���������һ�㣬�̳еĹ����Ǿ�̬�ģ����ܶ�̬��ɾ����

ȱ�㣺�����������ƵĶ��󣬲����Ŵ�
*/


# ����
public abstract class Beverage {
	String description = "Unknown Beverage";
  
	public String getDescription() {
		return description;
	}
 
	public abstract double cost();
}


# ��װ���ߣ�Ҫʵ��cost()
public class Espresso extends Beverage {
  
	public Espresso() {
		description = "Espresso";
	}
  
	public double cost() {
		return 1.99;
	}
}

# װ����
public abstract class CondimentDecorator extends Beverage {
	// ˵�еĵ���װ���߱�������ʵ��getDescription()
	public abstract String getDescription();
}


# ����װ����Milk
public class Milk extends CondimentDecorator {
	Beverage beverage;

	public Milk(Beverage beverage) {
		this.beverage = beverage;
	}

	public String getDescription() {
		return beverage.getDescription() + ", Milk";
	}

	public double cost() {
		return .10 + beverage.cost();
	}
}

# ����װ����Moca
public class Mocha extends CondimentDecorator {
	Beverage beverage;
 
	public Mocha(Beverage beverage) {
		this.beverage = beverage;
	}
	
	// ��дgetDescription,�ɵõ��������Ϻȵ���
	public String getDescription() {
		return beverage.getDescription() + ", Mocha";
	}
 
	public double cost() {
		return .20 + beverage.cost();
	}
}



public class StarbuzzCoffee {
 
	public static void main(String args[]) {
		Beverage beverage2 = new DarkRoast();
		beverage2 = new Mocha(beverage2);
		// ����Mocha
		beverage2 = new Mocha(beverage2);
		beverage2 = new Whip(beverage2);
		System.out.println(beverage2.getDescription() 
				+ " $" + beverage2.cost());
}