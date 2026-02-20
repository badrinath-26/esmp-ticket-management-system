package com.company.esmp.controller;

import com.company.esmp.dto.ApiResponse;
import com.company.esmp.dto.TicketResponseDTO;
import com.company.esmp.entity.User;
import com.company.esmp.enums.TicketStatus;
import com.company.esmp.exception.ResourceNotFoundException;
import com.company.esmp.mapper.TicketMapper;
import com.company.esmp.repository.UserRepository;
import com.company.esmp.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent/tickets")
public class AgentTicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public AgentTicketController(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ApiResponse<List<TicketResponseDTO>> getAssignedTickets(Authentication authentication) {

        String email = authentication.getName();

        User agent = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        List<TicketResponseDTO> tickets = ticketService.getTicketsAssignedTo(agent)
                .stream()
                .map(TicketMapper::mapToTicketResponse)
                .toList();

        return new ApiResponse<>(
                true,
                "Assigned tickets fetched successfully",
                tickets
        );
    }

    @PutMapping("/{ticketId}/status")
    public ApiResponse<TicketResponseDTO> updateTicketStatus(@PathVariable Long ticketId,
                                                             @RequestParam TicketStatus status,
                                                             Authentication authentication) {

        String email = authentication.getName();

        User agent = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        TicketResponseDTO updatedTicket = TicketMapper.mapToTicketResponse(
                ticketService.updateTicketStatus(ticketId, status, agent)
        );

        return new ApiResponse<>(
                true,
                "Ticket status updated successfully",
                updatedTicket
        );
    }
}
