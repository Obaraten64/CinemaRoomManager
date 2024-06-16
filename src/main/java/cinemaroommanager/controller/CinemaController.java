package cinemaroommanager.controller;

import cinemaroommanager.dto.requests.PurchaseTicketRequest;
import cinemaroommanager.dto.requests.Token;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.responses.ReturnedTicket;
import cinemaroommanager.dto.responses.PurchaseTicketResponse;
import cinemaroommanager.dto.responses.StatsDTO;
import cinemaroommanager.service.CinemaService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @GetMapping("/seats")
    public CinemaRoomDTO getSeats() {
        return cinemaService.getCinemaRoom();
    }

    @PostMapping("/purchase")
    public PurchaseTicketResponse purchaseSeat(@RequestBody PurchaseTicketRequest ticket) {
        return cinemaService.purchaseSeat(ticket);
    }

    @PostMapping("/return")
    public ReturnedTicket returnTicket(@Valid @RequestBody Token token) {
        return cinemaService.returnTicket(token.token());
    }

    @GetMapping("/stats")
    public StatsDTO getStats() {
        return cinemaService.getStats();
    }
}
