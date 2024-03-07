package cinemaroommanager.service;

import cinemaroommanager.configuration.CinemaConfig;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.SeatDTO;
import cinemaroommanager.dto.responses.Ticket;
import cinemaroommanager.exception.SeatPurchaseException;
import cinemaroommanager.model.CinemaRoom;
import cinemaroommanager.model.Seat;

import org.springframework.stereotype.Service;

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
            throw new SeatPurchaseException("The number of a row or a column is out of bounds!");
        }

        Seat seat = cinemaRoom.getSeats()
                .stream()
                .filter(s -> s.getRow() == seatDTO.row()
                        && s.getColumn() == seatDTO.column())
                .findFirst().get();
        if (seat.isPurchased()) {
            throw new SeatPurchaseException("The ticket has been already purchased!");
        }

        seat.purchase();
        return new Ticket(seat);
    }

    private boolean validateSeat(SeatDTO seatDTO) {
        return seatDTO.row() < 0 || seatDTO.row() > cinemaRoom.getNumberOfRows()
                || seatDTO.column() < 0 || seatDTO.column() > cinemaRoom.getNumberOfColumns();
    }
}
