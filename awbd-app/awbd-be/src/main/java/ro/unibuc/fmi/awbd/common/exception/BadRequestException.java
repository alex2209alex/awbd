package ro.unibuc.fmi.awbd.common.exception;

public class BadRequestException extends RuntimeException {
    private BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
