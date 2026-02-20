package com.company.esmp.controller;

import com.company.esmp.dto.ApiResponse;
import com.company.esmp.dto.request.TicketCommentRequestDTO;
import com.company.esmp.dto.response.TicketCommentResponseDTO;
import com.company.esmp.service.TicketCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketCommentController {

    private final TicketCommentService ticketCommentService;

    // Add Comment
    @PostMapping("/{ticketId}/comments")
    public ResponseEntity<ApiResponse<TicketCommentResponseDTO>> addComment(
            @PathVariable Long ticketId,
            @Valid @RequestBody TicketCommentRequestDTO requestDTO
    ) {
        TicketCommentResponseDTO responseDTO = ticketCommentService.addComment(ticketId, requestDTO);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Comment added successfully", responseDTO)
        );
    }

    // Get All Comments
    @GetMapping("/{ticketId}/comments")
    public ResponseEntity<ApiResponse<List<TicketCommentResponseDTO>>> getComments(
            @PathVariable Long ticketId
    ) {
        List<TicketCommentResponseDTO> comments = ticketCommentService.getCommentsByTicket(ticketId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Comments fetched successfully", comments)
        );
    }
}
