package cinemaroommanager.model;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "cinema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "uuid")
    @JdbcTypeCode(Types.VARCHAR) //default maps to binary type
    private UUID uuid;
    @Column(name = "rowNumber")
    private Integer row;
    @Column(name = "columnNumber")
    private Integer column;
    @Column(name = "price")
    private Integer price;
    @Column(name = "isPurchased")
    private Boolean isPurchased;

    public Seat(int row, int column) {
        this.column = column;
        this.row = row;
        price = row < 5 ? 10 : 8;
        isPurchased = false;
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
