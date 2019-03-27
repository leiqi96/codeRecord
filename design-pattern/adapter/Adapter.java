/* 名称：适配器模式
 * 解释：将一个类的接口，转换成客户期望的另一个接口，适配器让原本接口不兼容的类可以合作无间
 * 
 * 有一只鸭子，它有某些方法
 * 然后有一只火鸡，它有另一些方法
 * 现在我没有鸭子，想用火鸡替代，但是鸭子的方法，火鸡没有
 * 于是构建一个适配器，实现鸭子的接口，同时拥有火鸡的实例
 * 
 */


public interface Duck {
	public void quack();
	public void fly();
}

public class MallardDuck implements Duck {
	public void quack() {
		System.out.println("Quack");
	}
 
	public void fly() {
		System.out.println("I'm flying");
	}
}



public interface Turkey {
	public void gobble();
	public void fly();
}


public class WildTurkey implements Turkey {
	public void gobble() {
		System.out.println("Gobble gobble");
	}
 
	public void fly() {
		System.out.println("I'm flying a short distance");
	}
}




public class TurkeyAdapter implements Duck {
	Turkey turkey;
 
	public TurkeyAdapter(Turkey turkey) {
		this.turkey = turkey;
	}
    
	public void quack() {
		turkey.gobble();
	}
  
	public void fly() {
		for(int i=0; i < 5; i++) {
			turkey.fly();
		}
	}
}


