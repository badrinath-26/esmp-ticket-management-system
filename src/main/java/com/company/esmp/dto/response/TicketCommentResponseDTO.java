package com.company.esmp.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCommentResponseDTO {

    private Long id;
    private String message;

    private Long ticketId;

    private Long commentedById;
    private String commentedByName;

    private LocalDateTime createdAt;
}
