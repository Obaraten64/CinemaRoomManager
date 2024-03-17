package cinemaroommanager.controller.advice;

import cinemaroommanager.exception.PurchaseSeatException;
import cinemaroommanager.exception.ReturnSeatException;
import cinemaroommanager.exception.StatsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CinemaAdvice {

    @ExceptionHandler(PurchaseSeatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleSeatException(PurchaseSeatException exception) {
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler(ReturnSeatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleReturnSeatException(ReturnSeatException exception) {
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler(StatsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleStatsException(StatsException exception) {
        return Map.of("error", exception.getMessage());
    }
}
