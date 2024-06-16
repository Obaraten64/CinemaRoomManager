package cinemaroommanager.service;

import cinemaroommanager.dto.requests.PurchaseTicketRequest;
import cinemaroommanager.dto.responses.*;
import cinemaroommanager.exception.PurchaseSeatException;
import cinemaroommanager.exception.ReturnSeatException;
import cinemaroommanager.model.Seat;
import cinemaroommanager.repository.CinemaRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CinemaService {
    //TODO: OPTIONAL. add logic of choosing one of CinemaRepository beans
    private final CinemaRepository cinemaRepository;

    public CinemaService(CinemaRepository cinemaRepository) {
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
        Seat seat = cinemaRepository.getSeatByRowAndColumn(ticket.row(), ticket.column())
                .orElseThrow(() ->
                        new PurchaseSeatException("The number of a row or a column is out of bounds!"));

        if (seat.getIsPurchased()) {
            throw new PurchaseSeatException("The ticket has been already purchased!");
        }

        seat.purchaseSeat();
        cinemaRepository.updateSeat(seat);

        return new PurchaseTicketResponse(seat);
    }

    public ReturnedTicket returnTicket(String token) {
        Seat seat = cinemaRepository.getSeatByUUID(UUID.fromString(token))
                .orElseThrow(() -> new ReturnSeatException("Wrong token!"));

        seat.returnSeat();
        cinemaRepository.updateSeat(seat);

        return new ReturnedTicket(seat);
    }

    public StatsDTO getStats() {
        List<Seat> purchasedSeats = cinemaRepository.getPurchasedSeats();
        return new StatsDTO(calculateIncome(purchasedSeats),
                cinemaRepository.getAvailableSeats().size(),
                purchasedSeats.size());
    }

    private int calculateIncome(List<Seat> seats) {
        return seats.stream()
                .mapToInt(Seat::getPrice)
                .sum();
    }
}
