package com.company.esmp.mapper;

import com.company.esmp.dto.response.TicketCommentResponseDTO;
import com.company.esmp.entity.TicketComment;

public class TicketCommentMapper {

    public static TicketCommentResponseDTO toDTO(TicketComment comment) {

        return TicketCommentResponseDTO.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .ticketId(comment.getTicket().getId())
                .commentedById(comment.getCommentedBy().getId())
                .commentedByName(comment.getCommentedBy().getUsername())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
