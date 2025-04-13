package ro.unibuc.fmi.awbd.common;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.unibuc.fmi.awbd.common.exception.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            BadRequestException.class
    })
    public ResponseEntity<GenericApplicationError> handleBadRequestException(Exception exc) {
        return getGenericApplicationErrorResponseEntity(exc, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<GenericApplicationError> handleForbiddenException(Exception exc) {
        return getGenericApplicationErrorResponseEntity(exc, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GenericApplicationError> handleNotFoundException(Exception exc) {
        return getGenericApplicationErrorResponseEntity(exc, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            InternalServerErrorException.class
    })
    public ResponseEntity<GenericApplicationError> handleInternalServerErrorException(Exception exc) {
        return getGenericApplicationErrorResponseEntity(exc, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<GenericApplicationError> getGenericApplicationErrorResponseEntity(Exception exc, HttpStatus status) {
        log.error(exc.getMessage());
        val error = GenericApplicationError.of(status.value(), exc.getMessage());
        return ResponseEntity
                .status(status.value())
                .body(error);
    }
}
