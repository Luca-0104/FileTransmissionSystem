package interfaces;

public interface IOperateTask {

    public abstract void queryTask(int taskId);

    public abstract void suspendTask(int taskId);

    public abstract void resumeTask(int taskId);

}
