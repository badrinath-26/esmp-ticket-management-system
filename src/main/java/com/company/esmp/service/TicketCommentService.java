package com.company.esmp.service;

import com.company.esmp.dto.request.TicketCommentRequestDTO;
import com.company.esmp.dto.response.TicketCommentResponseDTO;

import java.util.List;

public interface TicketCommentService {

    TicketCommentResponseDTO addComment(Long ticketId, TicketCommentRequestDTO requestDTO);

    List<TicketCommentResponseDTO> getCommentsByTicket(Long ticketId);
}
