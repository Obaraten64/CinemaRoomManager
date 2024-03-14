package cinemaroommanager.dto.responses;

import cinemaroommanager.model.Seat;

public record ReturnedTicket(SeatDTO ticket) {

    public ReturnedTicket(Seat seat) {
        this(new SeatDTO(seat));
    }
}
