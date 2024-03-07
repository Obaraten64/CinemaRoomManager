package cinemaroommanager.model;

import cinemaroommanager.dto.SeatDTO;

import java.util.UUID;

public class Seat {
    private UUID uuid;
    private final int row;
    private final int column;
    private final int price;
    private boolean isPurchased;

    public Seat(int row, int column) {
        this.column = column;
        this.row = row;
        price = row < 5 ? 10 : 8;
        isPurchased = false;
    }

    public Seat(SeatDTO seatDTO) {
        row = seatDTO.row();
        column = seatDTO.column();
        price = row < 5 ? 10 : 8;
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
