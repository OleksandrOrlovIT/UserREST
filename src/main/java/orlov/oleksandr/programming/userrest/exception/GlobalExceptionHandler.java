package orlov.oleksandr.programming.userrest.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getBindingResult() + " for uri: " + request.getRequestURI());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(HttpServletRequest request, ValidationException exception) {
        StringBuilder message = new StringBuilder(exception.getMessage() + " for uri: " + request.getRequestURI() + "\n");
        for(Map.Entry<String, String> entry : exception.getErrors().entrySet()) {
            message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        log.error(message.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(message.toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage() + " for uri: " + request.getRequestURI());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(HttpServletRequest request, DateTimeParseException exception) {
        log.error(exception.getMessage(), exception);

        String message = "Wrong date/time format: " + exception.getParsedString() + "\n"
                + "Please use yyyy-mm-dd format " + " for uri: " + request.getRequestURI();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException
            (HttpServletRequest request, HttpMessageNotReadableException exception) throws IOException {
        log.error(exception.getMessage(), exception);

        String message = "Wrong date/time format: " + exception.getLocalizedMessage() + "\n"
                + "Please use yyyy-mm-dd format " + " for uri: " + request.getRequestURI();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternalServerError(HttpServletRequest request, Exception exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage() + " for uri: " + request.getRequestURI());
    }
}
