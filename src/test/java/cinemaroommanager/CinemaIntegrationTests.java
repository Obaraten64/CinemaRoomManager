package cinemaroommanager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CinemaIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test for GET /seats endpoint")
    void testEndpointsGetSeats() throws Exception {
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
                        .value(Matchers.hasSize(81)))
                .andExpect(jsonPath("$.seats[?(@.price == 10 || @.price == 8)]")
                        .value(Matchers.hasSize(81)));
    }

    @Test
    @DisplayName("Test for POST /purchase endpoint")
    @Order(1)
    void testEndpointPurchase() throws Exception {
        var requestBuilder = post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"row\":1,\n\"column\":1}");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.row").value(1))
                .andExpect(jsonPath("$.column").value(1))
                .andExpect(jsonPath("$.price").value(10));
    }

    @Test
    @DisplayName("Test for POST /purchase endpoint(with row out of bounds)")
    void testEndpointPurchaseOutOfBoundsException() throws Exception {
        var requestBuilder = post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"row\":15,\n\"column\":1}");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("The number of a row or a column is out of bounds!"));
    }

    @Test
    @DisplayName("Test for POST /purchase endpoint(when ticket is purchased)")
    @Order(2)
    void testEndpointPurchasePurchasedException() throws Exception {
        var requestBuilder = post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"row\":1,\n\"column\":1}");
        //mockMvc.perform(requestBuilder);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("The ticket has been already purchased!"));
    }
}
