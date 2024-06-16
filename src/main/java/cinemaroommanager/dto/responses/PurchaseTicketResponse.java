package cinemaroommanager.dto.responses;

import cinemaroommanager.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public record PurchaseTicketResponse(UUID token, SeatDTO ticket) {
    public PurchaseTicketResponse(Seat seat) {
        this(seat.getUuid(), new SeatDTO(seat));
    }
}
