package cinemaroommanager.service;

import cinemaroommanager.dto.requests.PurchaseTicketRequest;
import cinemaroommanager.dto.responses.*;
import cinemaroommanager.exception.PurchaseSeatException;
import cinemaroommanager.exception.ReturnSeatException;
import cinemaroommanager.exception.StatsException;
import cinemaroommanager.model.Seat;

import cinemaroommanager.repository.CinemaRepositoryInMemory;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public StatsDTO getStats(String password) {
        if (!("super_secret".equals(password))) { //bad practice to have password here
            throw new StatsException("The password is wrong!");
        }

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
