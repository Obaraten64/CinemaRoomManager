package cinemaroommanager;

import cinemaroommanager.configuration.CinemaConfig;
import cinemaroommanager.model.Seat;
import cinemaroommanager.repository.CinemaRepositoryInMemory;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

public class CinemaRepositoryTest {

    CinemaRepositoryInMemory cinemaRepository;

    public CinemaRepositoryTest() {
        CinemaConfig cinemaConfig = new CinemaConfig(9, 9);
        cinemaRepository = new CinemaRepositoryInMemory(cinemaConfig);
    }

    @Test
    void testGetColumns_GetRows_GetSeats() {
        assertThat(cinemaRepository.getRows()).isEqualTo(9);
        assertThat(cinemaRepository.getColumns()).isEqualTo(9);
        assertThat(cinemaRepository.getSeats())
                .hasSize(81)
                .hasOnlyElementsOfType(Seat.class);
    }

    @Test
    void testGetSeatByRowAndColumn() {
        assertThat(cinemaRepository.getSeatByRowAndColumn(1, 1))
                .isPresent()
                .map(s -> List.of(s.getRow(), s.getColumn(), s.getPrice()))
                .contains(List.of(1, 1, 10));
        assertThat(cinemaRepository.getSeatByRowAndColumn(15, 1)).isNotPresent();
        assertThat(cinemaRepository.getSeatByRowAndColumn(1, 15)).isNotPresent();
    }

    @Test
    void testGetSeatByUUID() {
        try (var mock = mockStatic(UUID.class)) {
            mock.when(UUID::randomUUID).thenReturn(new UUID(1, 10));
            mock.when(() -> UUID.fromString("0"))
                    .thenReturn(new UUID(1, 64));

            Seat seat = cinemaRepository.getSeats().get(2);
            seat.purchaseSeat();
            UUID uuid = seat.getUuid();

            assertThat(cinemaRepository.getSeatByUUID(uuid))
                    .isPresent()
                    .map(s -> List.of(s.getRow(), s.getColumn(), s.getPrice()))
                    .contains(List.of(1, 3, 10));
            assertThat(cinemaRepository.getSeatByUUID(UUID.fromString("0")))
                    .isNotPresent();
        }
    }
}
