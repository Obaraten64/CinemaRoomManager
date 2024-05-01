package cinemaroommanager;

import cinemaroommanager.repository.CinemaRepositoryDB;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = {"classpath:testApp.properties"})
public class CinemaRoomIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CinemaRepositoryDB cinemaRepository;

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
    void testEndpointPurchase() throws Exception {
        var requestBuilder = post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"row\":1,\n\"column\":1}");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.token").value(Matchers.hasLength(36)))
                .andExpect(jsonPath("$.ticket.row").value(1))
                .andExpect(jsonPath("$.ticket.column").value(1))
                .andExpect(jsonPath("$.ticket.price").value(10));
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
    @Sql(statements = "UPDATE cinema SET isPurchased = true WHERE rowNumber = 1 AND columnNumber = 1")
    void testEndpointPurchasePurchasedException() throws Exception {
        var requestBuilder = post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"row\":1,\n\"column\":1}");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("The ticket has been already purchased!"));
    }

    @Test
    @DisplayName("Test for POST /return endpoint")
    @Sql(statements = "UPDATE cinema SET uuid = '409548d1-2f6b-4180-8f70-5800c77c17a8' WHERE rowNumber = 1 AND columnNumber = 1")
    void testEndpointReturn() throws Exception {
        String token = cinemaRepository.getSeatByRowAndColumn(1, 1).get().getUuid().toString();//still don't like this

        var requestBuilder = post("/return")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"" + token + "\"}");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket.row").value(1))
                .andExpect(jsonPath("$.ticket.column").value(1))
                .andExpect(jsonPath("$.ticket.price").value(10));
    }

    @Test
    @DisplayName("Test for POST /return endpoint(when token is expired)")
    void testEndpointReturnTicketException() throws Exception {
        var requestBuilder = post("/return")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"409548d1-2f6b-4180-8f70-5800c77c17a8\"}");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Wrong token!"));
    }

    @Test
    @DisplayName("Test for GET /stats endpoint")
    void testEndpointGetStats() throws Exception {
        var postProcessor = SecurityMockMvcRequestPostProcessors
                .httpBasic("admin", "super_secret");

        var requestBuilder = get("/stats")
                .with(postProcessor);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.income").value(0))
                .andExpect(jsonPath("$.available").value(81))
                .andExpect(jsonPath("$.purchased").value(0));
    }

    @Test
    @DisplayName("Test for GET /stats endpoint wrong user")
    void testEndpointGetStats_WrongUser() throws Exception {
        var postProcessor = SecurityMockMvcRequestPostProcessors
                .httpBasic("user", "password");

        var requestBuilder = get("/stats")
                .with(postProcessor);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test for GET /stats endpoint unauthorized user")
    void testEndpointGetStats_UnauthorizedUser() throws Exception {
        var requestBuilder = get("/stats");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }
}
