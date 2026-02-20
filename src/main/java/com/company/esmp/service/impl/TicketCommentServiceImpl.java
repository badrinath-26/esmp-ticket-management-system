package com.company.esmp.service.impl;

import com.company.esmp.dto.request.TicketCommentRequestDTO;
import com.company.esmp.dto.response.TicketCommentResponseDTO;
import com.company.esmp.entity.Ticket;
import com.company.esmp.entity.TicketComment;
import com.company.esmp.entity.User;
import com.company.esmp.exception.ResourceNotFoundException;
import com.company.esmp.exception.UnauthorizedException;
import com.company.esmp.mapper.TicketCommentMapper;
import com.company.esmp.repository.TicketCommentRepository;
import com.company.esmp.repository.TicketRepository;
import com.company.esmp.repository.UserRepository;
import com.company.esmp.service.TicketCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketCommentServiceImpl implements TicketCommentService {

    private final TicketCommentRepository ticketCommentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public TicketCommentResponseDTO addComment(Long ticketId, TicketCommentRequestDTO requestDTO) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        // Get logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));


        // ROLE CHECK
        boolean isCustomer = loggedInUser.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("CUSTOMER")
                        || role.getName().equalsIgnoreCase("ROLE_CUSTOMER"));

        boolean isAgent = loggedInUser.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("AGENT")
                        || role.getName().equalsIgnoreCase("ROLE_AGENT"));

        boolean isManager = loggedInUser.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("MANAGER")
                        || role.getName().equalsIgnoreCase("ROLE_MANAGER"));

        // Customer can comment only on their own ticket
        if (isCustomer && !ticket.getCreatedBy().getId().equals(loggedInUser.getId())) {
            throw new UnauthorizedException("You are not allowed to comment on this ticket");
        }

        // Agent can comment only on assigned ticket
        if (isAgent) {
            if (ticket.getAssignedTo() == null || !ticket.getAssignedTo().getId().equals(loggedInUser.getId())) {
                throw new UnauthorizedException("You are not allowed to comment on this ticket");
            }
        }

        // Manager can comment on any ticket (no restriction)

        if (!isCustomer && !isAgent && !isManager) {
            throw new UnauthorizedException("You are not allowed to comment on tickets");
        }

        TicketComment comment = TicketComment.builder()
                .message(requestDTO.getMessage())
                .ticket(ticket)
                .commentedBy(loggedInUser)
                .build();

        TicketComment savedComment = ticketCommentRepository.save(comment);

        return TicketCommentMapper.toDTO(savedComment);
    }

    @Override
    public List<TicketCommentResponseDTO> getCommentsByTicket(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        List<TicketComment> comments = ticketCommentRepository.findByTicketIdOrderByCreatedAtAsc(ticket.getId());

        return comments.stream()
                .map(TicketCommentMapper::toDTO)
                .toList();
    }
}
