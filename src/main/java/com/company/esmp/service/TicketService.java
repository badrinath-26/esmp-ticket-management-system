package com.company.esmp.service;

import com.company.esmp.entity.Ticket;
import com.company.esmp.entity.User;
import com.company.esmp.enums.TicketPriority;
import com.company.esmp.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {

    Ticket createTicket(Ticket ticket, User customer);

    List<Ticket> getTicketsCreatedBy(User customer);

    List<Ticket> getTicketsAssignedTo(User agent);

    List<Ticket> getAllTickets();

    Ticket assignTicket(Long ticketId, User agent);

    Ticket updateTicketStatus(Long ticketId, TicketStatus status, User agent);

    List<Ticket> getTicketsByStatus(TicketStatus status);

    List<Ticket> getTicketsByPriority(TicketPriority priority);

    List<Ticket> getTicketsByStatusAndPriority(TicketStatus status, TicketPriority priority);

    Page<Ticket> getAllTicketsPaginated(Pageable pageable);


}
