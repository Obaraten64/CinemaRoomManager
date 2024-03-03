package cinemaroommanager.service;

import  cinemaroommanager.exception.SeatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CinemaAdvice {

    @ExceptionHandler(SeatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleSeatException(SeatException exception) {
        return Map.of("error", exception.getMessage());
    }
}
