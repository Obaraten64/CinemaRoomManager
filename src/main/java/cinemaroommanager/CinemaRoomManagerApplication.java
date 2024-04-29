package cinemaroommanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinemaRoomManagerApplication {
    //TODO: Add ability to work with database instead of inmemory
        //TODO: Slice test for DBRepository
    public static void main(String[] args) {
        SpringApplication.run(CinemaRoomManagerApplication.class, args);
    }

}
