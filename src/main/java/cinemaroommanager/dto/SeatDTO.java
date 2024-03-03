package cinemaroommanager.dto;

import cinemaroommanager.model.Seat;

public record SeatDTO(int row, int column, int price) {
    public SeatDTO(int rowN, int columnN) {
        this(rowN, columnN, 0);
    }

    public SeatDTO(Seat seat) {
        this(seat.getRow(), seat.getColumn(), seat.getPrice());
    }
}
