package ngen.exceptions;

/**
 * Created by xianggao on 5/2/16.
 */
public class QueueException extends RuntimeException {
    public QueueException(Exception e) {
        super(e);
    }

    public QueueException(String msg) {
        super(msg);
    }
}
