package cinemaroommanager.dto.responses;

import cinemaroommanager.model.Seat;

import java.util.UUID;

public record PurchaseTicketResponse(UUID token, SeatDTO ticket) {
    public PurchaseTicketResponse(Seat seat) {
        this(seat.getUuid(), new SeatDTO(seat));
    }
}
