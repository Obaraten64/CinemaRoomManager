package cinemaroommanager;

import cinemaroommanager.dto.CinemaRoomDTO;
import cinemaroommanager.model.CinemaRoom;
import cinemaroommanager.service.CinemaService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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

    @Test
    @DisplayName("Test for GET /seats endpoint")
    void testEndpointsGetSeats() throws Exception {
        var mockCinemaRoomDTO = new CinemaRoomDTO(
                new CinemaRoom(9, 9));
        when(cinemaService.getCinemaRoom()).thenReturn(mockCinemaRoomDTO);

        var requestBuilder = get("/seats");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows").value(9))
                .andExpect(jsonPath("$.columns").value(9))
                .andExpect(jsonPath("$.seats").isArray())
                .andExpect(jsonPath("$.seats").value(Matchers.hasSize(81)))
                .andExpect(jsonPath("$.seats[?((@.row > 9) || (@.row < 1))]").isEmpty())
                .andExpect(jsonPath("$.seats[:9].row")
                        .value(Matchers.equalTo(List.of(1, 1, 1, 1, 1, 1, 1, 1, 1))))
                .andExpect(jsonPath("$.seats[?(@.column > 0 && @.column < 10)]")
                        .value(Matchers.hasSize(81)));
    }
}
