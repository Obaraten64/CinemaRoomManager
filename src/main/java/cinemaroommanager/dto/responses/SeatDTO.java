package cinemaroommanager.dto.responses;

import cinemaroommanager.model.Seat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public record SeatDTO(int row, int column, int price) {
    public SeatDTO(Seat seat) {
        this(seat.getRow(), seat.getColumn(), seat.getPrice());
    }
}
