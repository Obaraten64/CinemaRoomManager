package cinemaroommanager.service;

import cinemaroommanager.dto.requests.PurchaseTicketRequest;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.responses.SeatDTO;
import cinemaroommanager.dto.responses.PurchaseTicketResponse;
import cinemaroommanager.dto.responses.ReturnedTicket;
import cinemaroommanager.exception.PurchaseSeatException;
import cinemaroommanager.exception.ReturnSeatException;
import cinemaroommanager.model.Seat;

import cinemaroommanager.repository.CinemaRepositoryInMemory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CinemaService {
    private final CinemaRepositoryInMemory cinemaRepository;

    public CinemaService(CinemaRepositoryInMemory cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    public CinemaRoomDTO getCinemaRoom() {
        return new CinemaRoomDTO(cinemaRepository.getRows(),
                cinemaRepository.getColumns(),
                cinemaRepository.getSeats()
                        .stream()
                        .map(SeatDTO::new)
                        .toList());
    }

    public PurchaseTicketResponse purchaseSeat(PurchaseTicketRequest ticket) {
        /*if (validateSeat(seatDTO)) {
            throw new PurchaseSeatException("The number of a row or a column is out of bounds!");
        }*/
        Seat seat = cinemaRepository.getSeatByRowAndColumn(ticket.row(), ticket.column())
                .orElseThrow(() ->
                        new PurchaseSeatException("The number of a row or a column is out of bounds!"));

        if (seat.isPurchased()) {
            throw new PurchaseSeatException("The ticket has been already purchased!");
        }

        seat.purchaseSeat();
        return new PurchaseTicketResponse(seat);
    }

    public ReturnedTicket returnTicket(UUID uuid) {
        Seat seat = cinemaRepository.getSeatByUUID(uuid)
                .orElseThrow(() -> new ReturnSeatException("Wrong token!"));

        seat.returnSeat();
        return new ReturnedTicket(seat);
    }
}
