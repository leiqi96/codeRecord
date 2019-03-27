/*
 * 本来用户在gui菜单上打开文档，就直接指向打开文档的代码
 * Command模式在这两者之间增加一个中间者
 * 
 * 司令员下令让士兵去干件事情，从整个事情的角度来考虑，
 * 司令员发出口令(持有口令），口令经过传递（口令持有士兵对象，调用士兵对象的接口），传到了士兵耳朵里，士兵去执行
 * 
 * 
 * Invoker是调用者（司令员），Receiver是被调用者（士兵），
 * MyCommand是命令，实现了Command接口，持有接收对象
 */


// 将命令/请求封装为对象，每个对象有一个统一的接口
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

// 具体不同命令/请求代码是实现接口Command
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