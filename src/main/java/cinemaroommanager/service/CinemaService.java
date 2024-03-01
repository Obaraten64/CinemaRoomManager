package cinemaroommanager.service;

import cinemaroommanager.configuration.CinemaConfig;
import cinemaroommanager.dto.CinemaRoomDTO;
import cinemaroommanager.model.CinemaRoom;

import org.springframework.stereotype.Service;

@Service
public class CinemaService {
    private final CinemaRoom cinemaRoom;

    public CinemaService(CinemaConfig cinemaConfig) {
        cinemaRoom = new CinemaRoom(cinemaConfig.getNumberOfColumns(), cinemaConfig.getNumberOfRows());
    }

    public CinemaRoomDTO getCinemaRoom() {
        return new CinemaRoomDTO(cinemaRoom);
    }
}
