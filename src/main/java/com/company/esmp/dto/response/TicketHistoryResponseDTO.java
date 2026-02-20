package com.company.esmp.dto.response;

import com.company.esmp.enums.TicketPriority;
import com.company.esmp.enums.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHistoryResponseDTO {

    private Long id;

    private Long ticketId;

    private TicketStatus oldStatus;
    private TicketStatus newStatus;

    private TicketPriority oldPriority;
    private TicketPriority newPriority;

    private Long changedById;
    private String changedByName;

    private LocalDateTime changedAt;
}
