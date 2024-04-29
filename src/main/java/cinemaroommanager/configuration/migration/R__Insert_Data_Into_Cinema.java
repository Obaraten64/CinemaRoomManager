package cinemaroommanager.configuration.migration;

import cinemaroommanager.configuration.CinemaConfig;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Component
public class R__Insert_Data_Into_Cinema extends BaseJavaMigration {
    private final int rows;
    private final int columns;

    public R__Insert_Data_Into_Cinema(CinemaConfig cinemaConfig) {
        rows = cinemaConfig.getNumberOfRows();
        columns = cinemaConfig.getNumberOfColumns();
    }

    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("TRUNCATE TABLE cinema");
        }

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

    @Override
    public Integer getChecksum() {
        return Long.valueOf(System.currentTimeMillis()).intValue();
    }
}
