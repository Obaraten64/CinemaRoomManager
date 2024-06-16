package cinemaroommanager.repository;

import cinemaroommanager.model.Seat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface CinemaRepositoryDB extends CinemaRepository, JpaRepository<Seat, UUID> {
    @Override
    default int getColumns() {
        return findAll().stream()
                .map(Seat::getColumn)
                .distinct()
                .toList()
                .size();
    }

    @Override
    default int getRows() {
        return findAll().stream()
                .map(Seat::getRow)
                .distinct()
                .toList()
                .size();
    }

    @Override
    default List<Seat> getSeats() {
        return findAll();
    }

    @Override
    default Optional<Seat> getSeatByRowAndColumn(int row, int column) {
        return findAll().stream()
                .filter(s -> s.getRow() == row
                        && s.getColumn() == column)
                .findFirst();
    }

    @Override
    default Optional<Seat> getSeatByUUID(UUID uuid) {
        return findAll().stream()
                .filter(s -> uuid.equals(s.getUuid()))
                .findFirst();
    }

    @Override
    default List<Seat> getAvailableSeats() {
        return findAll().stream()
                .filter(s -> !s.getIsPurchased())
                .toList();
    }

    @Override
    default List<Seat> getPurchasedSeats() {
        return findAll().stream()
                .filter(Seat::getIsPurchased)
                .toList();
    }

    @Override
    default void updateSeat(Seat seat) {
        save(seat);
    }
}
