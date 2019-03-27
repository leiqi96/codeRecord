/*
 * �����û���gui�˵��ϴ��ĵ�����ֱ��ָ����ĵ��Ĵ���
 * Commandģʽ��������֮������һ���м���
 * 
 * ˾��Ա������ʿ��ȥ�ɼ����飬����������ĽǶ������ǣ�
 * ˾��Ա��������(���п������������ݣ��������ʿ�����󣬵���ʿ������Ľӿڣ���������ʿ�������ʿ��ȥִ��
 * 
 * 
 * Invoker�ǵ����ߣ�˾��Ա����Receiver�Ǳ������ߣ�ʿ������
 * MyCommand�����ʵ����Command�ӿڣ����н��ն���
 */


// ������/�����װΪ����ÿ��������һ��ͳһ�Ľӿ�
public interface Command {
	public void execute();
}

public class Receiver
{
    public void action()
    {
        System.out.println("command received!");
    }
}

// ���岻ͬ����/���������ʵ�ֽӿ�Command
public class MyCommand implements Command
{
    private Receiver receiver;

    public MyCommand(Receiver receiver)
    {
        this.receiver = receiver;
    }

    @Override
    public void execute()
    {
        receiver.action();

    }

}

public class Invoker
{
    private Command command;

    public Invoker(Command command)
    {
        this.command = command;
    }

    public void action()
    {
        command.exe();
    }
}


public class Test
{
    public static void main(String[] args)
    {
        Receiver receiver = new Receiver();

        Command cmd = new MyCommand(receiver);

        Invoker invoker = new Invoker(cmd);
        invoker.action();

    }
}