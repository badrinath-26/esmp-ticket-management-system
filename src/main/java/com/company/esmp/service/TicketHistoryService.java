package com.company.esmp.service;

import com.company.esmp.dto.response.TicketHistoryResponseDTO;

import java.util.List;

public interface TicketHistoryService {

    List<TicketHistoryResponseDTO> getTicketHistory(Long ticketId);
}
