package ro.unibuc.fmi.awbd.common.exception;

public class InternalServerErrorException extends RuntimeException {
    private InternalServerErrorException() {
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}
