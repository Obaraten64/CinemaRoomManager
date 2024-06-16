package cinemaroommanager.dto.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public record CinemaRoomDTO(int rows, int columns, List<SeatDTO> seats) {
}
