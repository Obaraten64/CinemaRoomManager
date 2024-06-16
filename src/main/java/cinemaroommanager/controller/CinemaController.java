package cinemaroommanager.controller;

import cinemaroommanager.dto.requests.PurchaseTicketRequest;
import cinemaroommanager.dto.requests.Token;
import cinemaroommanager.dto.responses.CinemaRoomDTO;
import cinemaroommanager.dto.responses.ReturnedTicket;
import cinemaroommanager.dto.responses.PurchaseTicketResponse;
import cinemaroommanager.dto.responses.StatsDTO;
import cinemaroommanager.service.CinemaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @Operation(summary = "Get info about cinema room")
    @ApiResponse(responseCode = "200", description = "List of cinema room seats",
            content = @Content(
                    schema = @Schema(implementation = CinemaRoomDTO.class),
                    examples = @ExampleObject(value = "{\"rows\":1,\"columns\":1,\"seats\":" +
                            "[{\"row\":1,\"column\":1,\"price\":10}]}")))

    @GetMapping("/seats")
    public CinemaRoomDTO getSeats() {
        return cinemaService.getCinemaRoom();
    }

    @Operation(summary = "Purchase a seat in cinema room")
    @ApiResponse(responseCode = "200", description = "Purchased seat",
            content = @Content(
                    schema = @Schema(implementation = PurchaseTicketResponse.class),
                    examples = @ExampleObject(value = "{\"token\":\"409548d1-2f6b-4180-8f70-5800c77c17a8\"," +
                            "\"ticket\":{\"row\":1,\"column\":1,\"price\":10}}")))
    @ApiResponse(responseCode = "400", description = "Wrong number of row or column," +
            "the ticket already purchased", content = @Content)

    @PostMapping("/purchase")
    public PurchaseTicketResponse purchaseSeat(@RequestBody PurchaseTicketRequest ticket) {
        return cinemaService.purchaseSeat(ticket);
    }

    @Operation(summary = "Return your ticket")
    @ApiResponse(responseCode = "200", description = "Returned ticket",
            content = @Content(
                    schema = @Schema(implementation = PurchaseTicketResponse.class),
                    examples = @ExampleObject(value = "{\"ticket\":{\"row\":1,\"column\":1,\"price\":10}}}")))
    @ApiResponse(responseCode = "400", description = "Wrong token", content = @Content)

    @PostMapping("/return")
    public ReturnedTicket returnTicket(@Valid @RequestBody Token token) {
        return cinemaService.returnTicket(token.token());
    }

    @Operation(summary = "Check cinema stats, admin role required",
            security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponse(responseCode = "200", description = "Updated user",
            content = @Content(
                    schema = @Schema(implementation = StatsDTO.class),
                    examples = @ExampleObject(value = "{\"income\":0,\"available\":81,\"purchased\":0}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Wrong role", content = @Content)

    @GetMapping("/stats")
    public StatsDTO getStats() {
        return cinemaService.getStats();
    }
}
