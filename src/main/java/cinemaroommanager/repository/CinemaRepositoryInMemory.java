package cinemaroommanager.repository;

import cinemaroommanager.configuration.CinemaConfig;
import cinemaroommanager.model.Seat;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CinemaRepositoryInMemory {
    private final int columns;
    private final int rows;
    private final ArrayList<Seat> seats;

    public CinemaRepositoryInMemory(CinemaConfig cinemaConfig) {
        columns = cinemaConfig.getNumberOfColumns();
        rows = cinemaConfig.getNumberOfRows();
        seats = new ArrayList<>(columns * rows);
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                seats.add(new Seat(i, j));
            }
        }
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public List<Seat> getSeats() {
        return List.copyOf(seats);
    }

    public Optional<Seat> getSeatByRowAndColumn(int row, int column) {
        return seats.stream()
                .filter(s -> s.getRow() == row
                        && s.getColumn() == column)
                .findFirst();
    }

    public Optional<Seat> getSeatByUUID(UUID uuid) {
        return seats.stream()
                .filter(s -> uuid.equals(s.getUuid()))
                .findFirst();
    }
}
