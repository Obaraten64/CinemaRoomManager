package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;

public class V2__Insert_Data_Into_Cinema extends BaseJavaMigration {
    //TODO: Use CinemaConfig to init number of rows and columns
    @Override
    public void migrate(Context context) throws Exception {
        try (PreparedStatement statement = context.getConnection().prepareStatement(
                "INSERT INTO cinema (rowNumber, columnNumber, price, isPurchased) VALUES (?, ?, ?, ?)"
            )) {
            for (int i = 1; i <= 9; i++) {
                for (int j = 1; j <= 9; j++) {
                    statement.setInt(1, i);
                    statement.setInt(2, j);
                    statement.setInt(3, i < 5 ? 10 : 8);
                    statement.setBoolean(4, false);
                    statement.execute();
                }
            }
        }
    }
}
