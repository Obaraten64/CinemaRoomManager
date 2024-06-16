package cinemaroommanager;

import cinemaroommanager.configuration.CinemaConfig;
import cinemaroommanager.model.Seat;
import cinemaroommanager.repository.CinemaRepositoryInMemory;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Deprecated
public class CinemaInMemoryRepositoryTest {

    CinemaRepositoryInMemory cinemaRepository;

    public CinemaInMemoryRepositoryTest() {
        CinemaConfig cinemaConfig = new CinemaConfig(9, 9);
        cinemaRepository = new CinemaRepositoryInMemory(cinemaConfig);
    }

    @Test
    void testGetColumns_GetRows_GetSeats() {
        assertThat(cinemaRepository.getRows()).isEqualTo(9);
        assertThat(cinemaRepository.getColumns()).isEqualTo(9);
        assertThat(cinemaRepository.getSeats())
                .hasSize(81)
                .hasOnlyElementsOfType(Seat.class)
                .allMatch(s -> s.getColumn() > 0 && s.getColumn() < 10 && s.getRow() > 0
                        && s.getRow() < 10 && (s.getPrice() == 10 || s.getPrice() == 8));
    }

    @Test
    void testGetSeatByRowAndColumn() {
        assertThat(cinemaRepository.getSeatByRowAndColumn(1, 1))
                .isPresent()
                .get()
                .matches(s -> s.getRow() == 1 && s.getColumn() == 1);
    }

    @Test
    void testGetSeatByRowAndColumn_OutOfBounds() {
        assertThat(cinemaRepository.getSeatByRowAndColumn(15, 1)).isNotPresent();
        assertThat(cinemaRepository.getSeatByRowAndColumn(1, 15)).isNotPresent();
    }

    @Test
    void testGetSeatByUUID() {
        Seat seat = cinemaRepository.getSeats().get(2);
        seat.purchaseSeat();
        UUID uuid = seat.getUuid();

        assertThat(cinemaRepository.getSeatByUUID(uuid))
                .isPresent()
                .get()
                .matches(s -> s.getRow() == 1 && s.getColumn() == 3);
    }

    @Test
    void testGetSeatByUUID_WrongToken() {
        UUID uuidFromString = UUID.fromString("409548d1-2f6b-4180-8f70-5800c77c17a8");
        assertThat(cinemaRepository.getSeatByUUID(uuidFromString))
                .isNotPresent();
    }

    @Test
    void testGetAvailableSeats() {
        List<Seat> seats = cinemaRepository.getAvailableSeats();
        assertThat(seats)
                .hasOnlyElementsOfType(Seat.class)
                .hasSize(81)
                .allMatch(s -> !s.getIsPurchased());
    }

    @Test
    void testGetAvailableSeats_WhenTwoSeatsPurchased() {
        Seat seat1 = cinemaRepository.getSeats().get(0);
        Seat seat2 = cinemaRepository.getSeats().get(80);
        seat1.purchaseSeat();
        seat2.purchaseSeat();

        List<Seat> seats = cinemaRepository.getAvailableSeats();
        assertThat(seats)
                .hasOnlyElementsOfType(Seat.class)
                .hasSize(79)
                .noneMatch(e -> (e.getRow() == 1 && e.getColumn() == 1) || (e.getRow() == 9 && e.getColumn() == 9));
    }

    @Test
    void testGetPurchasedSeats() {
        List<Seat> seats = cinemaRepository.getPurchasedSeats();
        assertThat(seats)
                .hasSize(0);
    }

    @Test
    void testGetPurchasedSeats_WhenTwoSeatsPurchased() {
        Seat seat1 = cinemaRepository.getSeats().get(0);
        Seat seat2 = cinemaRepository.getSeats().get(80);
        seat1.purchaseSeat();
        seat2.purchaseSeat();

        List<Seat> seats = cinemaRepository.getPurchasedSeats();
        assertThat(seats)
                .hasOnlyElementsOfType(Seat.class)
                .hasSize(2)
                .allMatch(s -> (s.getRow() == 1 && s.getColumn() == 1) || (s.getRow() == 9 && s.getColumn() == 9));
    }
}
