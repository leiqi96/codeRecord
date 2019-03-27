/*
开放-关闭原则：类应该对扩展开发，对修改关闭

装饰者与被装饰者来自于同一个超类或者接口
这是让两者具有同样的类型，因为装饰者需要取代被装饰者
而不是为了集成超类的行为，行为来自具体的装饰者
*/

/*
装饰器模式的应用场景
1、需要扩展一个类的功能。
2、动态的为一个对象增加功能，而且还能动态撤销。（继承不能做到这一点，继承的功能是静态的，不能动态增删。）

缺点：产生过多相似的对象，不易排错！
*/


# 超类
public abstract class Beverage {
	String description = "Unknown Beverage";
  
	public String getDescription() {
		return description;
	}
 
	public abstract double cost();
}


# 被装饰者：要实现cost()
public class Espresso extends Beverage {
  
	public Espresso() {
		description = "Espresso";
	}
  
	public double cost() {
		return 1.99;
	}
}

# 装饰者
public abstract class CondimentDecorator extends Beverage {
	// 说有的调料装饰者必须重新实现getDescription()
	public abstract String getDescription();
}


# 调料装饰者Milk
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

# 调料装饰者Moca
public class Mocha extends CondimentDecorator {
	Beverage beverage;
 
	public Mocha(Beverage beverage) {
		this.beverage = beverage;
	}
	
	// 重写getDescription,可得到主体饮料喝调料
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
		// 两份Mocha
		beverage2 = new Mocha(beverage2);
		beverage2 = new Whip(beverage2);
		System.out.println(beverage2.getDescription() 
				+ " $" + beverage2.cost());
}