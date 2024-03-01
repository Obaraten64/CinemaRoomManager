package cinemaroommanager.dto;

import cinemaroommanager.model.CinemaRoom;
import cinemaroommanager.model.Seat;

import java.util.ArrayList;

public record CinemaRoomDTO(int rows, int columns, ArrayList<Seat> seats) {

    public CinemaRoomDTO(CinemaRoom cinemaRoom) {
        this(cinemaRoom.getNumberOfRows(), cinemaRoom.getNumberOfColumns(), cinemaRoom.getSeats());
    }
}
