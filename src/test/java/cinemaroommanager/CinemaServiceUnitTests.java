package cinemaroommanager;

import cinemaroommanager.dto.SeatDTO;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.responses.Ticket;
import cinemaroommanager.model.CinemaRoom;
import cinemaroommanager.model.Seat;
import cinemaroommanager.service.CinemaService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CinemaServiceUnitTests {
    @Autowired
    CinemaService cinemaService;

    @Test
    void testGetCinemaRoom() {
        CinemaRoomDTO cinemaRoomDTO = new CinemaRoomDTO(new CinemaRoom(9, 9));

        CinemaRoomDTO expect = cinemaService.getCinemaRoom();
        Assertions.assertEquals(cinemaRoomDTO, expect);
    }

    @Test
    void testPurchaseTicket() {
        try (var mock = mockStatic(UUID.class)) {
            mock.when(UUID::randomUUID).thenReturn(new UUID(1, 10));
            Seat seat = new Seat(1, 1);
            seat.purchaseSeat();
            Ticket ticket = new Ticket(seat);

            Ticket expect = cinemaService.purchaseSeat(new SeatDTO(1, 1));
            Assertions.assertEquals(ticket, expect);
        }
    }
}
