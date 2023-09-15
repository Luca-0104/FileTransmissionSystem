package interfaces;

public interface IStatus {

    // possible status are  "ongoing"， “suspended”, "finished"
    public static final String STATUS_ONGOING = "ongoing";
    public static final String STATUS_SUSPENDED = "suspended";
    public static final String STATUS_FINISHED = "finished";

    // possible operations
    public abstract String getStatus();
    public abstract void setStatus(String s);

}
