package cinemaroommanager.service;

import cinemaroommanager.dto.CinemaRoomDTO;
import cinemaroommanager.model.CinemaRoom;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaService {
    CinemaRoom cinemaRoom = new CinemaRoom();

    public CinemaRoomDTO getCinemaRoom() {
        return new CinemaRoomDTO(cinemaRoom);
    }
}
