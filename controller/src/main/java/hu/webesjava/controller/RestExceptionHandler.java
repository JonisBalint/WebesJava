package hu.webesjava.controller;

import java.util.Map;

import hu.webesjava.core.CarAlreadyExistsException;
import hu.webesjava.core.CarNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CarNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(CarNotFoundException exception) {
        return errorBody(exception.getMessage());
    }

    @ExceptionHandler(CarAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflict(CarAlreadyExistsException exception) {
        return errorBody(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidData(IllegalArgumentException exception) {
        return errorBody(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidJson() {
        return errorBody("Hibás vagy hiányzó JSON. A lóerő egész szám, a dátum formátuma "
                + "ÉÉÉÉ-HH-NN, az üzemanyag típusa PETROL, DIESEL vagy ELECTRIC.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> missingParameter(MissingServletRequestParameterException exception) {
        return errorBody("Hiányzó paraméter: " + exception.getParameterName());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> methodNotAllowed(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(exception.getHeaders())
                .body(errorBody("Nem támogatott HTTP-metódus: " + exception.getMethod()));
    }

    private Map<String, String> errorBody(String message) {
        return Map.of("error", message);
    }
}
