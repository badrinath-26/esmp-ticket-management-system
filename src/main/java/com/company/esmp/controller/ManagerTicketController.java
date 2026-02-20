package com.company.esmp.controller;

import com.company.esmp.dto.ApiResponse;
import com.company.esmp.dto.TicketResponseDTO;
import com.company.esmp.entity.User;
import com.company.esmp.enums.TicketPriority;
import com.company.esmp.enums.TicketStatus;
import com.company.esmp.exception.ResourceNotFoundException;
import com.company.esmp.mapper.TicketMapper;
import com.company.esmp.repository.UserRepository;
import com.company.esmp.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/tickets")
public class ManagerTicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public ManagerTicketController(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ApiResponse<List<TicketResponseDTO>> getAllTickets() {

        List<TicketResponseDTO> tickets = ticketService.getAllTickets()
                .stream()
                .map(TicketMapper::mapToTicketResponse)
                .toList();

        return new ApiResponse<>(
                true,
                "All tickets fetched successfully",
                tickets
        );
    }

    @PutMapping("/{ticketId}/assign/{agentId}")
    public ApiResponse<TicketResponseDTO> assignTicket(@PathVariable Long ticketId,
                                                       @PathVariable Long agentId) {

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agentId));

        TicketResponseDTO updatedTicket = TicketMapper.mapToTicketResponse(
                ticketService.assignTicket(ticketId, agent)
        );

        return new ApiResponse<>(
                true,
                "Ticket assigned successfully",
                updatedTicket
        );
    }

    @GetMapping("/filter")
    public ApiResponse<List<TicketResponseDTO>> filterTickets(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority
    ) {

        List<TicketResponseDTO> tickets;

        if (status != null && priority != null) {
            tickets = ticketService.getTicketsByStatusAndPriority(status, priority)
                    .stream()
                    .map(TicketMapper::mapToTicketResponse)
                    .toList();
        } else if (status != null) {
            tickets = ticketService.getTicketsByStatus(status)
                    .stream()
                    .map(TicketMapper::mapToTicketResponse)
                    .toList();
        } else if (priority != null) {
            tickets = ticketService.getTicketsByPriority(priority)
                    .stream()
                    .map(TicketMapper::mapToTicketResponse)
                    .toList();
        } else {
            tickets = ticketService.getAllTickets()
                    .stream()
                    .map(TicketMapper::mapToTicketResponse)
                    .toList();
        }

        return new ApiResponse<>(
                true,
                "Filtered tickets fetched successfully",
                tickets
        );
    }

    // ✅ PAGINATION + SORTING
    @GetMapping("/page")
    public ApiResponse<List<TicketResponseDTO>> getTicketsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<TicketResponseDTO> ticketPage = ticketService.getAllTicketsPaginated(pageable)
                .map(TicketMapper::mapToTicketResponse);

        return new ApiResponse<>(
                true,
                "Paginated tickets fetched successfully",
                ticketPage.getContent()
        );
    }
}
