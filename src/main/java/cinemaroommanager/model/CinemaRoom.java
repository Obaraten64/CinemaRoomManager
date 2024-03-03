package cinemaroommanager.model;

import cinemaroommanager.dto.SeatDTO;

import java.util.ArrayList;

public class CinemaRoom {
    private final int NUMBER_OF_COLUMNS;
    private final int NUMBER_OF_ROWS;
    private final ArrayList<Seat> seats;

    public CinemaRoom(int columns, int rows) {
        NUMBER_OF_COLUMNS = columns;
        NUMBER_OF_ROWS = rows;
        seats = new ArrayList<>(NUMBER_OF_COLUMNS * NUMBER_OF_ROWS);
        for (int i = 1; i <= NUMBER_OF_ROWS; i++) {
            for (int j = 1; j <= NUMBER_OF_COLUMNS; j++) {
                seats.add(new Seat(i, j));
            }
        }
    }

    public int getNumberOfRows() {
        return NUMBER_OF_ROWS;
    }

    public int getNumberOfColumns() {
        return NUMBER_OF_COLUMNS;
    }

    public Seat getSeat(SeatDTO seatDTO) {
        return seats.stream()
                .filter(s -> s.getRow() == seatDTO.row()
                        && s.getColumn() == seatDTO.column())
                .findFirst().get();
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }
}
