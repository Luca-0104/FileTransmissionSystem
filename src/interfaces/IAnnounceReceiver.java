package interfaces;

public interface IAnnounceReceiver {

    public abstract void announceStartTaskACK(int taskId);

    public abstract void announceSuspendTask(int taskId);

    public abstract void announceResumeTaskACK(int taskId);
}
