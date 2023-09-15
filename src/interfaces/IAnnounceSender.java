package interfaces;

import task.SendTask;

public interface IAnnounceSender {

    public abstract void announceStartTask(SendTask sendTask);

    public abstract void announceResumeTask(int taskId);

}
