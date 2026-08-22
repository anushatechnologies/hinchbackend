package com.hinchmart.controller;

import com.hinchmart.dto.request.SupportTicketRequest;
import com.hinchmart.dto.response.ApiResponse;
import com.hinchmart.dto.response.SupportTicketDto;
import com.hinchmart.entity.SupportTicket;
import com.hinchmart.entity.User;
import com.hinchmart.exception.ResourceNotFoundException;
import com.hinchmart.repository.SupportTicketRepository;
import com.hinchmart.repository.UserRepository;
import com.hinchmart.service.ActivityLogService;
import com.hinchmart.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/api/help", "/api/support"})
@Tag(name = "Help & Support Tickets (Flow 10)", description = "Endpoints for Buyer Logistics and Order Support Inquiries")
@SecurityRequirement(name = "Bearer Authentication")
public class HelpTicketController {

    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final ActivityLogService activityLogService;

    public HelpTicketController(SupportTicketRepository supportTicketRepository,
                                UserRepository userRepository,
                                AuthService authService,
                                ActivityLogService activityLogService) {
        this.supportTicketRepository = supportTicketRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.activityLogService = activityLogService;
    }

    @PostMapping("/tickets")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Create Help / Support Ticket", description = "Submits a support inquiry for logistics, gate clearance, delivery issues, or general support.")
    public ResponseEntity<ApiResponse<SupportTicketDto>> createTicket(Authentication authentication,
                                                                      @Valid @RequestBody SupportTicketRequest request) {
        User user = authService.getCurrentUser(authentication.getName());

        String ticketNumber = "TKT-" + (System.currentTimeMillis() % 1000000);
        SupportTicket ticket = new SupportTicket(
                ticketNumber,
                user,
                request.getOrderId(),
                request.getSubject(),
                request.getPriority(),
                request.getCategory(),
                request.getMessage()
        );

        SupportTicket saved = supportTicketRepository.save(ticket);
        activityLogService.log(user.getId(), user.getEmail(), "TICKET_CREATED", "SUPPORT_TICKET", saved.getId(),
                "Created support ticket: " + saved.getTicketNumber() + " (" + saved.getSubject() + ")", null);

        return new ResponseEntity<>(ApiResponse.success("Support ticket created successfully", mapToDto(saved)), HttpStatus.CREATED);
    }

    @GetMapping("/tickets")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "List My Support Tickets", description = "Returns all support tickets submitted by current user.")
    public ResponseEntity<ApiResponse<List<SupportTicketDto>>> getMyTickets(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        List<SupportTicketDto> tickets = supportTicketRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(tickets));
    }

    private SupportTicketDto mapToDto(SupportTicket ticket) {
        SupportTicketDto dto = new SupportTicketDto();
        dto.setNumericId(ticket.getId());
        dto.setId("tkt_" + ticket.getId());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setOrderId(ticket.getOrderId());
        dto.setSubject(ticket.getSubject());
        dto.setPriority(ticket.getPriority());
        dto.setCategory(ticket.getCategory());
        dto.setMessage(ticket.getMessage());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setUpdatedAt(ticket.getUpdatedAt());
        return dto;
    }
}
