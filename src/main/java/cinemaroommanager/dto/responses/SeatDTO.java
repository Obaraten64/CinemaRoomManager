package cinemaroommanager.dto.responses;

import cinemaroommanager.model.Seat;

public record SeatDTO(int row, int column, int price) {
    public SeatDTO(Seat seat) {
        this(seat.getRow(), seat.getColumn(), seat.getPrice());
    }
}
