package com.company.esmp.mapper;

import com.company.esmp.dto.TicketResponseDTO;
import com.company.esmp.dto.UserResponseDTO;
import com.company.esmp.entity.Ticket;
import com.company.esmp.entity.User;

public class TicketMapper {

    public static UserResponseDTO mapToUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    public static TicketResponseDTO mapToTicketResponse(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                mapToUserResponse(ticket.getCreatedBy()),
                mapToUserResponse(ticket.getAssignedTo()),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}
