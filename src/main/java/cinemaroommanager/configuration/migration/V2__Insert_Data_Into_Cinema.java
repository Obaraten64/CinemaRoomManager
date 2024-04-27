package cinemaroommanager.configuration.migration;

import cinemaroommanager.configuration.CinemaConfig;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class V2__Insert_Data_Into_Cinema extends BaseJavaMigration {
    private final int rows;
    private final int columns;

    public V2__Insert_Data_Into_Cinema(CinemaConfig cinemaConfig) {
        rows = cinemaConfig.getNumberOfRows();
        columns = cinemaConfig.getNumberOfColumns();
    }

    @Override
    public void migrate(Context context) throws Exception {
        try (PreparedStatement statement = context.getConnection().prepareStatement(
                "INSERT INTO cinema (rowNumber, columnNumber, price, isPurchased) VALUES (?, ?, ?, ?)"
            )) {
            for (int i = 1; i <= rows; i++) {
                for (int j = 1; j <= columns; j++) {
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
