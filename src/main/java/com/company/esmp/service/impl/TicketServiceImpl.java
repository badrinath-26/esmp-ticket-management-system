package com.company.esmp.service.impl;

import com.company.esmp.entity.Ticket;
import com.company.esmp.entity.TicketHistory;
import com.company.esmp.entity.User;
import com.company.esmp.enums.TicketPriority;
import com.company.esmp.enums.TicketStatus;
import com.company.esmp.exception.ResourceNotFoundException;
import com.company.esmp.exception.UnauthorizedException;
import com.company.esmp.repository.TicketHistoryRepository;
import com.company.esmp.repository.TicketRepository;
import com.company.esmp.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketHistoryRepository ticketHistoryRepository;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             TicketHistoryRepository ticketHistoryRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketHistoryRepository = ticketHistoryRepository;
    }

    @Override
    public Ticket createTicket(Ticket ticket, User customer) {

        ticket.setCreatedBy(customer);
        ticket.setStatus(TicketStatus.OPEN);

        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getTicketsCreatedBy(User customer) {
        return ticketRepository.findByCreatedBy(customer);
    }

    @Override
    public List<Ticket> getTicketsAssignedTo(User agent) {
        return ticketRepository.findByAssignedTo(agent);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // Helper method to save history
    private void saveTicketHistory(Ticket ticket,
                                   User changedBy,
                                   TicketStatus oldStatus,
                                   TicketStatus newStatus,
                                   TicketPriority oldPriority,
                                   TicketPriority newPriority) {

        TicketHistory history = TicketHistory.builder()
                .ticket(ticket)
                .changedBy(changedBy)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .oldPriority(oldPriority)
                .newPriority(newPriority)
                .build();

        ticketHistoryRepository.save(history);
    }

    @Override
    public Ticket assignTicket(Long ticketId, User agent) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        TicketStatus oldStatus = ticket.getStatus();
        TicketPriority oldPriority = ticket.getPriority();

        ticket.setAssignedTo(agent);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        Ticket updatedTicket = ticketRepository.save(ticket);

        saveTicketHistory(
                updatedTicket,
                agent,
                oldStatus,
                updatedTicket.getStatus(),
                oldPriority,
                updatedTicket.getPriority()
        );

        return updatedTicket;
    }

    @Override
    public Ticket updateTicketStatus(Long ticketId, TicketStatus status, User agent) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        if (ticket.getAssignedTo() == null || !ticket.getAssignedTo().getId().equals(agent.getId())) {
            throw new UnauthorizedException("You are not allowed to update this ticket");
        }

        TicketStatus oldStatus = ticket.getStatus();
        TicketPriority oldPriority = ticket.getPriority();

        ticket.setStatus(status);

        Ticket updatedTicket = ticketRepository.save(ticket);

        saveTicketHistory(
                updatedTicket,
                agent,
                oldStatus,
                updatedTicket.getStatus(),
                oldPriority,
                updatedTicket.getPriority()
        );

        return updatedTicket;
    }

    @Override
    public List<Ticket> getTicketsByStatus(TicketStatus status) {
        return ticketRepository.findByStatus(status);
    }

    @Override
    public List<Ticket> getTicketsByPriority(TicketPriority priority) {
        return ticketRepository.findByPriority(priority);
    }

    @Override
    public List<Ticket> getTicketsByStatusAndPriority(TicketStatus status, TicketPriority priority) {
        return ticketRepository.findByStatusAndPriority(status, priority);
    }

    @Override
    public Page<Ticket> getAllTicketsPaginated(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }
}
