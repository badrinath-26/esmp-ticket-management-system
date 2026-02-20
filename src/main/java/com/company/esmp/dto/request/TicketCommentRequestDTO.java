package com.company.esmp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCommentRequestDTO {

    @NotBlank(message = "Comment message cannot be empty")
    @Size(min = 3, max = 1000, message = "Comment message must be between 3 and 1000 characters")
    private String message;
}
