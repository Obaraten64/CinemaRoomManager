package cinemaroommanager.dto.responses;

import cinemaroommanager.dto.SeatDTO;
import cinemaroommanager.model.Seat;

import java.util.UUID;

public record Ticket(UUID token, SeatDTO ticket) {
    public Ticket(Seat seat) {
        this(seat.getUuid(), new SeatDTO(seat));
    }
}
