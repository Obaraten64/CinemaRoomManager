package cinema.service;

import cinema.dto.CinemaRoomDTO;
import cinema.model.CinemaRoom;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaService {
    CinemaRoom cinemaRoom = new CinemaRoom();

    public CinemaRoomDTO getCinemaRoom() {
        return new CinemaRoomDTO(cinemaRoom);
    }
}
