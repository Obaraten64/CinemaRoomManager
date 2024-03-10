package cinemaroommanager;

import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.model.CinemaRoom;
import cinemaroommanager.service.CinemaService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;

@SpringBootTest
public class CinemaServiceUnitTests {
    @Autowired
    CinemaService cinemaService;

    @Test
    void testGetCinemaRoom() {
        CinemaRoomDTO cinemaRoomDTO = new CinemaRoomDTO(new CinemaRoom(9, 9));

        CinemaRoomDTO expect = cinemaService.getCinemaRoom();
        Assertions.assertEquals(cinemaRoomDTO, expect);
    }
}
