package cinemaroommanager.service;

import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.SeatDTO;
import cinemaroommanager.dto.responses.Ticket;
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

    public Ticket purchaseSeat(SeatDTO seatDTO) {
        /*if (validateSeat(seatDTO)) {
            throw new PurchaseSeatException("The number of a row or a column is out of bounds!");
        }*/

        Seat seat = cinemaRepository.getSeatByRowAndColumn(seatDTO.row(), seatDTO.column())
                .orElseThrow(() ->
                        new PurchaseSeatException("The number of a row or a column is out of bounds!"));

        if (seat.isPurchased()) {
            throw new PurchaseSeatException("The ticket has been already purchased!");
        }

        seat.purchaseSeat();
        return new Ticket(seat);
    }

    public ReturnedTicket returnTicket(UUID uuid) {
        Seat seat = cinemaRepository.getSeatByUUID(uuid)
                .orElseThrow(() -> new ReturnSeatException("Wrong token!"));

        seat.returnSeat();
        return new ReturnedTicket(new SeatDTO(seat));
    }
}
