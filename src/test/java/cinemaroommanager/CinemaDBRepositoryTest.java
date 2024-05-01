package cinemaroommanager;

import cinemaroommanager.model.Seat;
import cinemaroommanager.repository.CinemaRepositoryDB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.datasource.url = jdbc:mysql://localhost:3306/test_db",
        "spring.datasource.username = root",
        "spring.datasource.password =",
        "spring.jpa.hibernate.ddl-auto = create-drop",
        "spring.test.database.replace = NONE",
        "spring.flyway.enabled = false"
}) //for tests, it is better to use H2 db
public class CinemaDBRepositoryTest {
    @Autowired
    private CinemaRepositoryDB cinemaRepository;

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                cinemaRepository.save(new Seat(i, j));
            }
        }
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
        Seat seat = cinemaRepository.getSeatByRowAndColumn(1, 2).get();
        seat.purchaseSeat();
        UUID uuid = seat.getUuid();
        cinemaRepository.updateSeat(seat);

        assertThat(cinemaRepository.getSeatByUUID(uuid))
                .isPresent()
                .get()
                .matches(s -> s.getRow() == 1 && s.getColumn() == 2);
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
                .allMatch(s -> !s.isPurchased());
    }

    @Test
    void testGetAvailableSeats_WhenTwoSeatsPurchased() {
        purchaseTwoSeats();

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
        purchaseTwoSeats();

        for (Seat seat : cinemaRepository.findAll()) {
            System.out.println(seat.isPurchased());
        }

        List<Seat> seats = cinemaRepository.getPurchasedSeats();
        assertThat(seats)
                .hasOnlyElementsOfType(Seat.class)
                .hasSize(2)
                .allMatch(s -> (s.getRow() == 1 && s.getColumn() == 1) || (s.getRow() == 9 && s.getColumn() == 9));
    }

    private void purchaseTwoSeats() {
        Seat seat1 = cinemaRepository.getSeatByRowAndColumn(1, 1).get();
        Seat seat2 = cinemaRepository.getSeatByRowAndColumn(9, 9).get();
        seat1.purchaseSeat();
        seat2.purchaseSeat();
        cinemaRepository.updateSeat(seat1);
        cinemaRepository.updateSeat(seat2);
    }
}
