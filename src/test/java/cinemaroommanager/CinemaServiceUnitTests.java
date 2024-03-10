package cinemaroommanager;

import cinemaroommanager.dto.SeatDTO;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.responses.ReturnedTicket;
import cinemaroommanager.dto.responses.Ticket;
import cinemaroommanager.exception.PurchaseSeatException;
import cinemaroommanager.exception.ReturnSeatException;
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
        CinemaRoomDTO expect = new CinemaRoomDTO(new CinemaRoom(9, 9));

        CinemaRoomDTO cinemaRoomDTO = cinemaService.getCinemaRoom();
        Assertions.assertEquals(expect, cinemaRoomDTO);
    }

    @Test
    void testPurchaseTicket() {
        try (var mock = mockStatic(UUID.class)) {
            mock.when(UUID::randomUUID).thenReturn(new UUID(1, 10));
            Seat seat = new Seat(1, 1);
            seat.purchaseSeat();
            Ticket expect = new Ticket(seat);

            Ticket ticket = cinemaService.purchaseSeat(new SeatDTO(1, 1));
            Assertions.assertEquals(expect, ticket);
        }
    }

    @Test
    void testPurchaseTicketOutOfBounds() {
        Exception exception = Assertions.assertThrows(PurchaseSeatException.class,
                () -> cinemaService.purchaseSeat(new SeatDTO(15, 1)));

        Assertions.assertEquals("The number of a row or a column is out of bounds!", exception.getMessage());
    }

    @Test
    void testPurchaseTicketAlreadyPurchased() {
        purchaseTicketForTest();

        Exception exception = Assertions.assertThrows(PurchaseSeatException.class,
                () -> cinemaService.purchaseSeat(new SeatDTO(1, 1)));

        Assertions.assertEquals("The ticket has been already purchased!", exception.getMessage());
    }

    @Test
    void testReturnTicket() {
        try (var mock = mockStatic(UUID.class)) {
            mock.when(UUID::randomUUID).thenReturn(new UUID(1, 10));
            ReturnedTicket expect = new ReturnedTicket(new SeatDTO(1, 1, 10));

            UUID token = purchaseTicketForTest();
            ReturnedTicket ticket = cinemaService.returnTicket(token);
            Assertions.assertEquals(expect, ticket);
        }
    }

    @Test
    void testReturnTicketWrongToken() {
        Exception exception = Assertions.assertThrows(ReturnSeatException.class,
                () -> cinemaService.returnTicket(UUID.randomUUID()));

        Assertions.assertEquals("Wrong token!", exception.getMessage());
    }

    private UUID purchaseTicketForTest() {
        return cinemaService.purchaseSeat(new SeatDTO(1, 1))
                .token();
    }
}
