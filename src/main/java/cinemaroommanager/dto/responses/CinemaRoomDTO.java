package cinemaroommanager.dto.responses;

import cinemaroommanager.dto.SeatDTO;

import java.util.List;

public record CinemaRoomDTO(int rows, int columns, List<SeatDTO> seats) {

}
