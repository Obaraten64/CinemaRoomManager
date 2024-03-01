package cinemaroommanager.dto;

import cinemaroommanager.model.CinemaRoom;
import cinema.model.Seat;

import java.util.ArrayList;

public class CinemaRoomDTO {
    private final int rows;
    private final int columns;
    private final ArrayList<Seat> seats;

    public CinemaRoomDTO(CinemaRoom cinemaRoom) {
        rows = CinemaRoom.getNumberOfRows();
        columns = CinemaRoom.getNumberOfColumns();
        seats = new ArrayList<>(cinemaRoom.getSeats());
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }
}
