package com.company.esmp.controller;

import com.company.esmp.dto.ApiResponse;
import com.company.esmp.dto.TicketCreateRequestDTO;
import com.company.esmp.dto.TicketResponseDTO;
import com.company.esmp.entity.Ticket;
import com.company.esmp.entity.User;
import com.company.esmp.exception.ResourceNotFoundException;
import com.company.esmp.mapper.TicketMapper;
import com.company.esmp.repository.UserRepository;
import com.company.esmp.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/tickets")
public class CustomerTicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public CustomerTicketController(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ApiResponse<TicketResponseDTO> createTicket(@Valid @RequestBody TicketCreateRequestDTO request,
                                                       Authentication authentication) {

        String email = authentication.getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());

        Ticket savedTicket = ticketService.createTicket(ticket, customer);

        return new ApiResponse<>(
                true,
                "Ticket created successfully",
                TicketMapper.mapToTicketResponse(savedTicket)
        );
    }

    @GetMapping
    public ApiResponse<List<TicketResponseDTO>> getMyTickets(Authentication authentication) {

        String email = authentication.getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        List<TicketResponseDTO> tickets = ticketService.getTicketsCreatedBy(customer)
                .stream()
                .map(TicketMapper::mapToTicketResponse)
                .toList();

        return new ApiResponse<>(
                true,
                "Customer tickets fetched successfully",
                tickets
        );
    }
}
