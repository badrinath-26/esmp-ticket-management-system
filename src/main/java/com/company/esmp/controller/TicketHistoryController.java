package com.company.esmp.controller;

import com.company.esmp.dto.ApiResponse;
import com.company.esmp.dto.response.TicketHistoryResponseDTO;
import com.company.esmp.service.TicketHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketHistoryController {

    private final TicketHistoryService ticketHistoryService;

    @GetMapping("/{ticketId}/history")
    public ResponseEntity<ApiResponse<List<TicketHistoryResponseDTO>>> getTicketHistory(
            @PathVariable Long ticketId
    ) {
        List<TicketHistoryResponseDTO> history = ticketHistoryService.getTicketHistory(ticketId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Ticket history fetched successfully", history)
        );
    }
}
