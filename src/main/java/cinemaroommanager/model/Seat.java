package cinemaroommanager.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "cinema")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "uuid")
    @JdbcTypeCode(Types.VARCHAR) //default maps to binary type
    private UUID uuid;
    @Column(name = "rowNumber")
    private int row;
    @Column(name = "columnNumber")
    private int column;
    @Column(name = "price")
    private int price;
    @Column(name = "isPurchased")
    private boolean isPurchased;

    public Seat() {

    }

    public Seat(int row, int column) {
        this.column = column;
        this.row = row;
        price = row < 5 ? 10 : 8;
        isPurchased = false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPrice() {
        return price;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void purchaseSeat() {
        isPurchased = true;
        uuid = UUID.randomUUID();
    }

    public void returnSeat() {
        isPurchased = false;
        uuid = null;
    }
}
