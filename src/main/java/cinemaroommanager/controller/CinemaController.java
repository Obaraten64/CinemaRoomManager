package cinemaroommanager.controller;

import cinemaroommanager.dto.requests.PurchaseTicketRequest;
import cinemaroommanager.dto.requests.Token;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.responses.ReturnedTicket;
import cinemaroommanager.dto.responses.PurchaseTicketResponse;
import cinemaroommanager.dto.responses.StatsDTO;
import cinemaroommanager.service.CinemaService;

import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/stats")
    public StatsDTO getStats(/*@RequestParam(required = false) String password*/) {
        return cinemaService.getStats(/*password*/);
    }
}
