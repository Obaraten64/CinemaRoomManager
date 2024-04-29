package cinemaroommanager.repository;

import cinemaroommanager.model.Seat;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface CinemaRepository {
    public int getColumns();

    public int getRows();

    public List<Seat> getSeats();

    public Optional<Seat> getSeatByRowAndColumn(int row, int column);

    public Optional<Seat> getSeatByUUID(UUID uuid);

    public List<Seat> getAvailableSeats();

    public List<Seat> getPurchasedSeats();

    public void updateSeat(Seat seat);
}
