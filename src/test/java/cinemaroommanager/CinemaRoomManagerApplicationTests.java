package cinemaroommanager;

import cinemaroommanager.configuration.CinemaConfig;
import cinemaroommanager.dto.CinemaRoomDTO;
import cinemaroommanager.model.CinemaRoom;
import cinemaroommanager.service.CinemaService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CinemaRoomManagerApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CinemaService cinemaService;
    @Autowired
    private CinemaConfig cinemaConfig;

    @Test
    @DisplayName("Test for GET /seats endpoint")
    void testEndpointsGetSeats() throws Exception {
        var mockCinemaRoomDTO = new CinemaRoomDTO(
                new CinemaRoom(cinemaConfig.getNumberOfColumns(),
                cinemaConfig.getNumberOfRows()));
        when(cinemaService.getCinemaRoom()).thenReturn(mockCinemaRoomDTO);

        var requestBuilder = get("/seats");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows").value(cinemaConfig.getNumberOfRows()))
                .andExpect(jsonPath("$.columns").value(cinemaConfig.getNumberOfColumns()))
                .andExpect(jsonPath("$.seats").isArray())
                .andExpect(jsonPath("$.seats.length()").value(
                        cinemaConfig.getNumberOfRows() * cinemaConfig.getNumberOfColumns()));
    }
}
