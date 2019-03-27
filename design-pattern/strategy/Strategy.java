/*名称:策略模式
 *解释：属于对象行为型设计模式，主要是定义一系列的算法，把这些算法一个个封装成拥有共同接口的单独的类，并且使它们之间可以互换
 *将算法本身与算法的使用 分离
 *
 *1：找出应用中可能需要变化之处，把它们独立起来，不要和那些不需要变化混在一起
 *2：针对接口编程，而不是针对实现编程。
 *3：多用组合，少用继承
*/


//Fly

/*FlyBehavior是一个接口，定义了一个fly()，用接口可以实现多态
*FlyRocketPowered和FlyNoWay都是具体的实现，可以理解为不同的算法，算法接口统一
*/
public interface FlyBehavior {
	public void fly();
}

public class FlyRocketPowered implements FlyBehavior {
	public void fly() {
		System.out.println("I'm flying with a rocket");
	}
}

public class FlyNoWay implements FlyBehavior {
	public void fly() {
		System.out.println("I can't fly");
	}
}


//当继承抽象类Duck的具体类时，动态设定flyBehavior
//具体的fly动作由实现了FlyBehavior接口的其它类来完成，这可以说是第三条的组合
public abstract class Duck {
	FlyBehavior flyBehavior;
 
	public Duck() {
	}
 
	public void setFlyBehavior (FlyBehavior fb) {
		flyBehavior = fb;
	}
 
	abstract void display();
 
	public void performFly() {
		flyBehavior.fly();
	}
 
	public void swim() {
		System.out.println("All ducks float, even decoys!");
	}
}


public class MallardDuck extends Duck {
 
	public MallardDuck() {
  	flyBehavior = new FlyWithWings();
	}
 
	public void display() {
		System.out.println("I'm a real Mallard duck");
	}
}

public class ModelDuck extends Duck {
	public ModelDuck() {
		flyBehavior = new FlyNoWay();
	}

	public void display() {
		System.out.println("I'm a model duck");
	}
}

public class MiniDuckSimulator {
 
	public static void main(String[] args) {
 
		MallardDuck	mallard = new MallardDuck();
		
		ModelDuck	model = new ModelDuck();

		mallard.performQuack();

   
		model.performFly();	
		model.setFlyBehavior(new FlyRocketPowered());
		model.performFly();
	}
}