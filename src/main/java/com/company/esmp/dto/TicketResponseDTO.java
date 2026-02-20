package com.company.esmp.dto;

import com.company.esmp.enums.TicketPriority;
import com.company.esmp.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TicketResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;

    private UserResponseDTO createdBy;
    private UserResponseDTO assignedTo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
