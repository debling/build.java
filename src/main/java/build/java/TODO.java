package build.java;


public class TODO extends RuntimeException {
    private static final long serialVersionUID = 1;

    TODO(String msg) {
        super(msg);
    }

    TODO(String msg, Exception cause) {
        super(msg, cause);
    }
}
