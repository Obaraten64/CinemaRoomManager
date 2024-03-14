package cinemaroommanager.util;

import cinemaroommanager.model.Seat;

import java.util.ArrayList;

public class TestUtils {

    public static ArrayList<Seat> getSeats() {
        ArrayList<Seat> seats = new ArrayList<>(9 * 9);
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                seats.add(new Seat(i, j));
            }
        }
        return seats;
    }
}
