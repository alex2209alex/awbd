package ro.unibuc.fmi.awbd.common.exception;

public class NotFoundException extends RuntimeException {
    private NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
