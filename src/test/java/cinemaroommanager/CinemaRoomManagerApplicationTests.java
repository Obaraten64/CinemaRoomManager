package cinemaroommanager;

import cinemaroommanager.configuration.CinemaConfig;
import cinemaroommanager.dto.CinemaRoomDTO;
import cinemaroommanager.model.CinemaRoom;
import cinemaroommanager.service.CinemaService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

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
    void testEndpointsGetSeats() throws Exception {
        var mockCinemaRoomDTO = new CinemaRoomDTO(
                new CinemaRoom(cinemaConfig.getNumberOfColumns(),
                cinemaConfig.getNumberOfRows()));
        when(cinemaService.getCinemaRoom()).thenReturn(mockCinemaRoomDTO);

        var requestBuilder = get("/seats");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows").value(9))
                .andExpect(jsonPath("$.columns").value(9))
                .andExpect(jsonPath("$.seats").isArray());
    }
}
