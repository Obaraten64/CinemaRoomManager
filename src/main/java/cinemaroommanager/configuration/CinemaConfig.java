package cinemaroommanager.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class CinemaConfig {
    @Value("${cinema.number_of_columns}")
    private int NUMBER_OF_COLUMNS;

    @Value("${cinema.number_of_rows}")
    private int NUMBER_OF_ROWS;

    public int getNumberOfColumns() {
        return NUMBER_OF_COLUMNS;
    }

    public int getNumberOfRows() {
        return NUMBER_OF_ROWS;
    }
}
