package cinemaroommanager.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class CinemaConfig {

    private final int number_of_rows;
    private final int number_of_columns;

    public CinemaConfig(@Value("${cinema.number_of_rows}") int number_of_rows,
                        @Value("${cinema.number_of_columns}") int number_of_columns) {
        this.number_of_rows = number_of_rows;
        this.number_of_columns = number_of_columns;
    }

    public int getNumberOfRows() {
        return number_of_rows;
    }

    public int getNumberOfColumns() {
        return number_of_columns;
    }
}
