package cinemaroommanager.service;

import cinemaroommanager.exception.SeatPurchaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CinemaAdvice {

    @ExceptionHandler(SeatPurchaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleSeatException(SeatPurchaseException exception) {
        return Map.of("error", exception.getMessage());
    }
}
