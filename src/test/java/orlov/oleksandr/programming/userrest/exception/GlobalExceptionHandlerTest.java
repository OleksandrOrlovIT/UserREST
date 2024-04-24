package orlov.oleksandr.programming.userrest.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MethodArgumentNotValidException mockException;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void testHandleValidationException() {
        Map<String, String> errors = Map.of("Validation error", "Error details");
        ValidationException exception = new ValidationException(errors);
        ResponseEntity<Map<String, Object>> responseEntity = globalExceptionHandler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Validation error", responseEntity.getBody().get("message"));
        assertEquals(errors, responseEntity.getBody().get("errors"));
    }

    @Test
    public void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Bad request");
        ResponseEntity<Map<String, Object>> responseEntity = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Bad request", responseEntity.getBody().get("message"));
        assertEquals("Bad request", responseEntity.getBody().get("errors"));
    }

    @Test
    public void testHandleDateTimeParseException() {
        DateTimeParseException exception = new DateTimeParseException("Invalid date", "2022-04-30", 0);
        ResponseEntity<Map<String, Object>> responseEntity = globalExceptionHandler.handleDateTimeParseException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Wrong date/time format", responseEntity.getBody().get("message"));
        assertEquals("Please use yyyy-mm-dd format", responseEntity.getBody().get("details"));
        assertEquals("Invalid date", responseEntity.getBody().get("error"));
    }

    @Test
    public void testHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Bad request");
        ResponseEntity<Map<String, Object>> responseEntity = globalExceptionHandler.handleHttpMessageNotReadableException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Bad request", responseEntity.getBody().get("message"));
        assertEquals("Wrong date/time format", responseEntity.getBody().get("details"));
        assertEquals("Bad request", responseEntity.getBody().get("error"));
    }

    @Test
    public void testHandleMethodArgumentNotValid() {
        BindingResult mockBindingResult = mock(BindingResult.class);

        List<ObjectError> errors = new ArrayList<>();
        errors.add(new ObjectError("field", "Error message 1"));
        errors.add(new ObjectError("field", "Error message 2"));

        when(mockBindingResult.getAllErrors()).thenReturn(errors);

        when(mockException.getBindingResult()).thenReturn(mockBindingResult);

        ResponseEntity<Map<String, Object>> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(mockException);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Validation error", responseEntity.getBody().get("message"));
        assertEquals(errors, responseEntity.getBody().get("errors"));
    }

    @Test
    public void testHandleInternalServerError() {
        Exception exception = new Exception("Internal server error");
        ResponseEntity<Map<String, Object>> responseEntity = globalExceptionHandler.handleInternalServerError(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Internal server error", responseEntity.getBody().get("message"));
        assertEquals("Internal server error", responseEntity.getBody().get("error"));
    }
}