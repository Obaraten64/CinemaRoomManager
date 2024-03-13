package cinemaroommanager;

import cinemaroommanager.dto.SeatDTO;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.responses.ReturnedTicket;
import cinemaroommanager.dto.responses.Ticket;
import cinemaroommanager.exception.PurchaseSeatException;
import cinemaroommanager.exception.ReturnSeatException;
import cinemaroommanager.model.Seat;
import cinemaroommanager.repository.CinemaRepositoryInMemory;
import cinemaroommanager.service.CinemaService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CinemaServiceUTest { //use assertThat instead of Assertion

    @Mock
    CinemaRepositoryInMemory cinemaRepository;

    @InjectMocks
    CinemaService cinemaService;

    @Test
    void testGetCinemaRoom() {
        ArrayList<Seat> seats = getSeats();

        CinemaRoomDTO expect = new CinemaRoomDTO(9, 9,
                seats.stream()
                        .map(SeatDTO::new)
                        .toList());

        when(cinemaRepository.getColumns()).thenReturn(9);
        when(cinemaRepository.getRows()).thenReturn(9);
        when(cinemaRepository.getSeats()).thenReturn(seats);

        CinemaRoomDTO cinemaRoomDTO = cinemaService.getCinemaRoom();
        assertThat(cinemaRoomDTO).isEqualTo(expect);
    }

    private ArrayList<Seat> getSeats() {
        ArrayList<Seat> seats = new ArrayList<>(9 * 9);
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                seats.add(new Seat(i, j));
            }
        }
        return seats;
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
    void testPurchaseTicket_OutOfBounds() {
        Exception exception = Assertions.assertThrows(PurchaseSeatException.class,
                () -> cinemaService.purchaseSeat(new SeatDTO(15, 1)));

        Assertions.assertEquals("The number of a row or a column is out of bounds!", exception.getMessage());
    }

    @Test
    void testPurchaseTicket_AlreadyPurchased() {
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
    void testReturnTicket_WrongToken() {
        Exception exception = Assertions.assertThrows(ReturnSeatException.class,
                () -> cinemaService.returnTicket(UUID.randomUUID()));

        Assertions.assertEquals("Wrong token!", exception.getMessage());
    }

    private UUID purchaseTicketForTest() {
        return cinemaService.purchaseSeat(new SeatDTO(1, 1))
                .token();
    }
}
