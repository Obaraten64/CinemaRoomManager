package cinemaroommanager.model;

import java.util.ArrayList;

public class CinemaRoom {
    private final int columns;
    private final int rows;
    private final ArrayList<Seat> seats;

    public CinemaRoom(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        seats = new ArrayList<>(columns * rows);
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                seats.add(new Seat(i, j));
            }
        }
    }

    public int getNumberOfRows() {
        return rows;
    }

    public int getNumberOfColumns() {
        return columns;
    }

    public ArrayList<Seat> getSeats() {
        return new ArrayList<>(seats);
    }
}
