package cinemaroommanager.model;

public class Seat {
    private final int row;
    private final int column;
    private boolean isPurchased;

    public Seat(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
