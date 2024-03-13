package cinemaroommanager;

import cinemaroommanager.configuration.CinemaConfig;
import cinemaroommanager.model.Seat;
import cinemaroommanager.repository.CinemaRepositoryInMemory;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class CinemaRepositoryTest {

    CinemaRepositoryInMemory cinemaRepository;

    public CinemaRepositoryTest() {
        CinemaConfig cinemaConfig = new CinemaConfig(9, 9);
        cinemaRepository = new CinemaRepositoryInMemory(cinemaConfig);
    }

    @Test
    void test() {
        ArrayList<Seat> actualSeats = cinemaRepository.getSeats();

        assertThat(cinemaRepository.getRows()).isEqualTo(9);
        assertThat(cinemaRepository.getColumns()).isEqualTo(9);
        assertThat(actualSeats).hasSize(81);
        assertThat(actualSeats).hasOnlyElementsOfType(Seat.class);
    }
}
