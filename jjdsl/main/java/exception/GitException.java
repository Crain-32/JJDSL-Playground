package exception;

public class GitException extends RuntimeException {
    public GitException(String message, Throwable e) {
        super(message, e);
    }
}
