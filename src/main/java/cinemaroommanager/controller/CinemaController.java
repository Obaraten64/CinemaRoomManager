package cinema.controller;

import cinema.dto.CinemaRoomDTO;
import cinema.service.CinemaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaController {
    CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/seats")
    public CinemaRoomDTO getSeats() {
        return cinemaService.getCinemaRoom();
    }
}
