package com.company.esmp.repository;

import com.company.esmp.entity.Ticket;
import com.company.esmp.entity.User;
import com.company.esmp.enums.TicketPriority;
import com.company.esmp.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCreatedBy(User createdBy);

    List<Ticket> findByAssignedTo(User assignedTo);

    List<Ticket> findByAssignedToAndStatus(User assignedTo, TicketStatus status);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByPriority(TicketPriority priority);

    List<Ticket> findByStatusAndPriority(TicketStatus status, TicketPriority priority);
    Page<Ticket> findAll(Pageable pageable);

}
