package com.company.esmp.service.impl;

import com.company.esmp.dto.response.TicketHistoryResponseDTO;
import com.company.esmp.entity.Ticket;
import com.company.esmp.entity.TicketHistory;
import com.company.esmp.exception.ResourceNotFoundException;
import com.company.esmp.mapper.TicketHistoryMapper;
import com.company.esmp.repository.TicketHistoryRepository;
import com.company.esmp.repository.TicketRepository;
import com.company.esmp.service.TicketHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketHistoryServiceImpl implements TicketHistoryService {

    private final TicketHistoryRepository ticketHistoryRepository;
    private final TicketRepository ticketRepository;

    @Override
    public List<TicketHistoryResponseDTO> getTicketHistory(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        List<TicketHistory> historyList =
                ticketHistoryRepository.findByTicketIdOrderByChangedAtDesc(ticket.getId());

        return historyList.stream()
                .map(TicketHistoryMapper::toDTO)
                .toList();
    }
}
