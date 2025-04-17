package ro.unibuc.fmi.awbd.common.exception;

public class ForbiddenException extends RuntimeException {
    private ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
