package cinemaroommanager.configuration;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CinemaFlywayConfig implements FlywayConfigurationCustomizer {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void customize(FluentConfiguration configuration) {
        JavaMigration[] migrationBeans = applicationContext
                .getBeansOfType(JavaMigration.class)
                .values().toArray(new JavaMigration[0]);
        configuration.javaMigrations(migrationBeans);
    }
}
