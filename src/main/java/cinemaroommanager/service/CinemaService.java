package cinemaroommanager.service;

import cinemaroommanager.configuration.CinemaConfig;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.SeatDTO;
import cinemaroommanager.dto.responses.Ticket;
import cinemaroommanager.dto.responses.ReturnedTicket;
import cinemaroommanager.exception.PurchaseSeatException;
import cinemaroommanager.exception.ReturnSeatException;
import cinemaroommanager.model.CinemaRoom;
import cinemaroommanager.model.Seat;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CinemaService {
    private final CinemaRoom cinemaRoom;

    public CinemaService(CinemaConfig cinemaConfig) {
        cinemaRoom = new CinemaRoom(cinemaConfig.getNumberOfColumns(), cinemaConfig.getNumberOfRows());
    }

    public CinemaRoomDTO getCinemaRoom() {
        return new CinemaRoomDTO(cinemaRoom);
    }

    public Ticket purchaseSeat(SeatDTO seatDTO) {
        if (validateSeat(seatDTO)) {
            throw new PurchaseSeatException("The number of a row or a column is out of bounds!");
        }

        Seat seat = cinemaRoom.getSeats()
                .stream()
                .filter(s -> s.getRow() == seatDTO.row()
                        && s.getColumn() == seatDTO.column())
                .findFirst().get();
        if (seat.isPurchased()) {
            throw new PurchaseSeatException("The ticket has been already purchased!");
        }

        seat.purchaseSeat();
        return new Ticket(seat);
    }

    public ReturnedTicket returnTicket(UUID uuid) {
        Seat seat = cinemaRoom.getSeats()
                .stream()
                .filter(s -> uuid.equals(s.getUuid()))
                .findFirst()
                .orElse(null);
        if (seat == null) {
            throw new ReturnSeatException("Wrong token!");
        }

        seat.returnSeat();
        return new ReturnedTicket(new SeatDTO(seat));
    }

    private boolean validateSeat(SeatDTO seatDTO) {
        return seatDTO.row() < 0 || seatDTO.row() > cinemaRoom.getNumberOfRows()
                || seatDTO.column() < 0 || seatDTO.column() > cinemaRoom.getNumberOfColumns();
    }
}
