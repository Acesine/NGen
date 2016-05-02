package exceptions;

/**
 * Created by xianggao on 5/1/16.
 */
public class UriException extends RuntimeException {
    public UriException(String msg) {
        super(msg);
    }

    public UriException(Exception e) {
        super(e);
    }
}
