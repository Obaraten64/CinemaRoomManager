package cinemaroommanager.controller;

import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.SeatDTO;
import cinemaroommanager.dto.responses.Ticket;
import cinemaroommanager.service.CinemaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/purchase")
    public Ticket purchaseSeat(@RequestBody SeatDTO seatDTO) {
        return cinemaService.purchaseSeat(seatDTO);
    }


}
