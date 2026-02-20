package com.company.esmp.mapper;

import com.company.esmp.dto.response.TicketHistoryResponseDTO;
import com.company.esmp.entity.TicketHistory;

public class TicketHistoryMapper {

    public static TicketHistoryResponseDTO toDTO(TicketHistory history) {

        return TicketHistoryResponseDTO.builder()
                .id(history.getId())
                .ticketId(history.getTicket().getId())
                .oldStatus(history.getOldStatus())
                .newStatus(history.getNewStatus())
                .oldPriority(history.getOldPriority())
                .newPriority(history.getNewPriority())
                .changedById(history.getChangedBy().getId())
                .changedByName(history.getChangedBy().getUsername())
                .changedAt(history.getChangedAt())
                .build();
    }
}
