package cinema.model;

import java.util.ArrayList;

public class CinemaRoom {
    private static final int NUMBER_OF_COLUMNS = 9;
    private static final int NUMBER_OF_ROWS = 9;
    private final ArrayList<Seat> seats = new ArrayList<>(NUMBER_OF_COLUMNS * NUMBER_OF_ROWS);

    public CinemaRoom() {
        for (int i = 1; i <= NUMBER_OF_ROWS; i++) {
            for (int j = 1; j <= NUMBER_OF_COLUMNS; j++) {
                seats.add(new Seat(i, j));
            }
        }
    }

    public static int getNumberOfRows() {
        return NUMBER_OF_ROWS;
    }

    public static int getNumberOfColumns() {
        return NUMBER_OF_COLUMNS;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }
}
