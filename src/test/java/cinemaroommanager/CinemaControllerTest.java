package cinemaroommanager;

import cinemaroommanager.controller.CinemaController;
import cinemaroommanager.dto.requests.PurchaseTicketRequest;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.responses.SeatDTO;
import cinemaroommanager.dto.responses.ReturnedTicket;
import cinemaroommanager.dto.responses.PurchaseTicketResponse;
import cinemaroommanager.exception.PurchaseSeatException;
import cinemaroommanager.exception.ReturnSeatException;
import cinemaroommanager.service.CinemaService;

import cinemaroommanager.util.TestUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CinemaController.class)
class CinemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CinemaService cinemaService;

    @Test
    @DisplayName("Test for GET /seats endpoint")
    void testEndpointsGetSeats() throws Exception {
        var mockCinemaRoomDTO = new CinemaRoomDTO(9, 9,
                TestUtils.getSeats()
                        .stream()
                        .map(SeatDTO::new)
                        .toList());

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
                        .value(Matchers.hasSize(81)))
                .andExpect(jsonPath("$.seats[?(@.price == 10 || @.price == 8)]")
                        .value(Matchers.hasSize(81)));
    }

    @Test
    @DisplayName("Test for POST /purchase endpoint")
    void testEndpointPurchase() throws Exception {
        var mockTicket = new PurchaseTicketResponse(UUID.randomUUID(),new SeatDTO(1, 1, 10));
        when(cinemaService.purchaseSeat(new PurchaseTicketRequest(1, 1)))
                .thenReturn(mockTicket);

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
        when(cinemaService.purchaseSeat(new PurchaseTicketRequest(15, 1)))
                .thenThrow(new PurchaseSeatException("The number of a row or a column is out of bounds!"));

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
    void testEndpointPurchasePurchasedException() throws Exception {
        when(cinemaService.purchaseSeat(new PurchaseTicketRequest(1, 1)))
                .thenThrow(new PurchaseSeatException("The ticket has been already purchased!"));

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
    void testEndpointReturn() throws Exception {
        when(cinemaService.returnTicket(UUID.fromString("409548d1-2f6b-4180-8f70-5800c77c17a8")))
                .thenReturn(new ReturnedTicket(new SeatDTO(1, 1, 10)));

        var requestBuilder = post("/return")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"409548d1-2f6b-4180-8f70-5800c77c17a8\"}");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket.row").value(1))
                .andExpect(jsonPath("$.ticket.column").value(1))
                .andExpect(jsonPath("$.ticket.price").value(10));
    }

    @Test
    @DisplayName("Test for POST /return endpoint(when token is expired)")
    void testEndpointReturnTicketException() throws Exception {
        when(cinemaService.returnTicket(UUID.fromString("409548d1-2f6b-4180-8f70-5800c77c17a8")))
                .thenThrow(new ReturnSeatException("Wrong token!"));

        var requestBuilder = post("/return")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"409548d1-2f6b-4180-8f70-5800c77c17a8\"}");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Wrong token!"));
    }
}
