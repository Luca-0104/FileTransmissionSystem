package message;

import java.io.Serializable;

/**
 * A message between sender and receiver should claim:
 *      what operation is wanted to be done on which task.
 */
public class Message implements Serializable {

    /*
        Possible message types
    */
    public static final String START_TASK = "start_task";
    public static final String SUSPEND_TASK = "suspend_task";
    public static final String RESUME_TASK = "resume_task";
    public static final String START_TASK_ACK = "start_task_ACK";
    public static final String SUSPEND_TASK_ACK = "suspend_task_ACK";
    public static final String RESUME_TASK_ACK = "resume_task_ACK";

    private String type;
    private Object content;

    public Message(String type, Object content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
