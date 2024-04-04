package cinemaroommanager;

import cinemaroommanager.dto.requests.PurchaseTicketRequest;
import cinemaroommanager.dto.responses.*;
import cinemaroommanager.exception.PurchaseSeatException;
import cinemaroommanager.exception.ReturnSeatException;
import cinemaroommanager.model.Seat;
import cinemaroommanager.repository.CinemaRepositoryInMemory;
import cinemaroommanager.service.CinemaService;

import cinemaroommanager.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CinemaServiceTest {

    @Mock
    CinemaRepositoryInMemory cinemaRepository;

    @InjectMocks
    CinemaService cinemaService;

    @Test
    void testGetCinemaRoom() {
        ArrayList<Seat> seats = TestUtils.getSeats();

        CinemaRoomDTO expect = new CinemaRoomDTO(9, 9,
                seats.stream()
                        .map(SeatDTO::new)
                        .toList());

        when(cinemaRepository.getColumns()).thenReturn(9);
        when(cinemaRepository.getRows()).thenReturn(9);
        when(cinemaRepository.getSeats()).thenReturn(seats);

        CinemaRoomDTO actual = cinemaService.getCinemaRoom();
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void testPurchaseTicket() {
        try (var mock = mockStatic(UUID.class)) {
            mock.when(UUID::randomUUID).thenReturn(new UUID(1, 10));
            Seat seat = new Seat(1, 1);
            seat.purchaseSeat();
            PurchaseTicketResponse expect = new PurchaseTicketResponse(seat);

            when(cinemaRepository.getSeatByRowAndColumn(1, 1))
                    .thenReturn(Optional.of(new Seat(1, 1)));

            PurchaseTicketResponse actual = cinemaService.purchaseSeat(new PurchaseTicketRequest(1, 1));
            assertThat(actual).isEqualTo(expect);
        }
    }

    @Test
    void testPurchaseTicket_OutOfBounds() {
        when(cinemaRepository.getSeatByRowAndColumn(15, 1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cinemaService.purchaseSeat(new PurchaseTicketRequest(15, 1)))
                .isInstanceOf(PurchaseSeatException.class)
                .hasMessage("The number of a row or a column is out of bounds!");
    }

    @Test
    void testPurchaseTicket_AlreadyPurchased() {
        Seat seat = new Seat(1, 1);
        seat.purchaseSeat();
        Optional<Seat> seatOptional = Optional.of(seat);

        when(cinemaRepository.getSeatByRowAndColumn(1, 1)).thenReturn(seatOptional);

        assertThatThrownBy(() -> cinemaService.purchaseSeat(new PurchaseTicketRequest(1, 1)))
                .isInstanceOf(PurchaseSeatException.class)
                .hasMessage("The ticket has been already purchased!");
    }

    @Test
    void testReturnTicket() {
        String uuid = "409548d1-2f6b-4180-8f70-5800c77c17a8";
        ReturnedTicket expect = new ReturnedTicket(new SeatDTO(1, 1, 10));

        when(cinemaRepository.getSeatByUUID(UUID.fromString(uuid)))
                .thenReturn(Optional.of(new Seat(1, 1)));

        ReturnedTicket actual = cinemaService.returnTicket(UUID.fromString(uuid));
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void testReturnTicket_WrongToken() {
        String uuid = "409548d1-2f6b-4180-8f70-5800c77c17a8";
        when(cinemaRepository.getSeatByUUID(UUID.fromString(uuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cinemaService.returnTicket(UUID.fromString(uuid)))
                .isInstanceOf(ReturnSeatException.class)
                .hasMessage("Wrong token!");
    }

    @Test
    void testGetStats() {
        StatsDTO expect = new StatsDTO(0, 81, 0);

        List<Seat> seats = TestUtils.getSeats();
        when(cinemaRepository.getAvailableSeats()).thenReturn(seats);
        when(cinemaRepository.getPurchasedSeats()).thenReturn(new ArrayList<>(0));

        assertThat(cinemaService.getStats(/*"super_secret"*/)).isEqualTo(expect);
    }
}
