package cinemaroommanager.dto;

import cinemaroommanager.model.CinemaRoom;

import java.util.ArrayList;

public record CinemaRoomDTO(int rows, int columns, ArrayList<SeatDTO> seats) {

    public CinemaRoomDTO(CinemaRoom cinemaRoom) {
        this(cinemaRoom.getNumberOfRows(),
                cinemaRoom.getNumberOfColumns(),
                new ArrayList<>(cinemaRoom.getSeats()
                        .stream()
                        .map(SeatDTO::new)
                        .toList()));
    }
}
