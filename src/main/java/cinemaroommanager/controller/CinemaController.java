package cinemaroommanager.controller;

import cinemaroommanager.dto.requests.PurchaseTicketRequest;
import cinemaroommanager.dto.requests.Token;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.responses.ReturnedTicket;
import cinemaroommanager.dto.responses.PurchaseTicketResponse;
import cinemaroommanager.service.CinemaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaController {
    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/seats")
    public CinemaRoomDTO getSeats() {
        return cinemaService.getCinemaRoom();
    }

    @PostMapping("/purchase")
    public PurchaseTicketResponse purchaseSeat(@RequestBody PurchaseTicketRequest ticket) {
        return cinemaService.purchaseSeat(ticket);
    }

    @PostMapping("/return")
    public ReturnedTicket returnTicket(@RequestBody Token token) {
        return cinemaService.returnTicket(token.token());
    }
}
