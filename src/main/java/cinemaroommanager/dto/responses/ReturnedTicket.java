package cinemaroommanager.dto.responses;

import cinemaroommanager.model.Seat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public record ReturnedTicket(SeatDTO ticket) {
    public ReturnedTicket(Seat seat) {
        this(new SeatDTO(seat));
    }
}
