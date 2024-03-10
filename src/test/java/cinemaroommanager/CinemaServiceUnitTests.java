package cinemaroommanager;

import cinemaroommanager.dto.SeatDTO;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.responses.Ticket;
import cinemaroommanager.exception.PurchaseSeatException;
import cinemaroommanager.model.CinemaRoom;
import cinemaroommanager.model.Seat;
import cinemaroommanager.service.CinemaService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.mockito.Mockito.mockStatic;

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

    @Test
    void testPurchaseTicketOutOfBounds() {
        Exception expect = Assertions.assertThrows(PurchaseSeatException.class,
                () -> cinemaService.purchaseSeat(new SeatDTO(15, 1)));

        Assertions.assertEquals("The number of a row or a column is out of bounds!", expect.getMessage());
    }

    @Test
    void testPurchaseTicketAlreadyPurchased() {
        SeatDTO seatDTO = new SeatDTO(1, 1);
        cinemaService.purchaseSeat(seatDTO);

        Exception expect = Assertions.assertThrows(PurchaseSeatException.class,
                () -> cinemaService.purchaseSeat(seatDTO));

        Assertions.assertEquals("The ticket has been already purchased!", expect.getMessage());
    }
}
