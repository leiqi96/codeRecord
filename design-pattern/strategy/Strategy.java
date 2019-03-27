/*����:����ģʽ
 *���ͣ����ڶ�����Ϊ�����ģʽ����Ҫ�Ƕ���һϵ�е��㷨������Щ�㷨һ������װ��ӵ�й�ͬ�ӿڵĵ������࣬����ʹ����֮����Ի���
 *���㷨�������㷨��ʹ�� ����
 *
 *1���ҳ�Ӧ���п�����Ҫ�仯֮���������Ƕ�����������Ҫ����Щ����Ҫ�仯����һ��
 *2����Խӿڱ�̣����������ʵ�ֱ�̡�
 *3��������ϣ����ü̳�
*/


//Fly

/*FlyBehavior��һ���ӿڣ�������һ��fly()���ýӿڿ���ʵ�ֶ�̬
*FlyRocketPowered��FlyNoWay���Ǿ����ʵ�֣��������Ϊ��ͬ���㷨���㷨�ӿ�ͳһ
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


//���̳г�����Duck�ľ�����ʱ����̬�趨flyBehavior
//�����fly������ʵ����FlyBehavior�ӿڵ�����������ɣ������˵�ǵ����������
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