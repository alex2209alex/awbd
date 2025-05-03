package ro.unibuc.fmi.awbd.common.exception;

public class UnauthorizedException extends RuntimeException {
    private UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
