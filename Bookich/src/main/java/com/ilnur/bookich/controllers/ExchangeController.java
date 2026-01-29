package com.ilnur.bookich.controllers;

import com.ilnur.bookich.dtos.ExchangeRequestDTO;
import com.ilnur.bookich.dtos.ExchangeResponseDTO;
import com.ilnur.bookich.dtos.RequestStatusDTO;
import com.ilnur.bookich.services.ExchangeRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeRequestService exchangeRequestService;

    @PostMapping("/request")
    public ResponseEntity<String> createRequest(@Valid @RequestBody ExchangeRequestDTO request) {
        exchangeRequestService.createRequest(request);
        return ResponseEntity.ok("Exchange Request Created Successfully");
    }

    @GetMapping("/incoming")
    public ResponseEntity<Page<ExchangeResponseDTO>> getIncomingRequests(Pageable pageable) {
        Page<ExchangeResponseDTO> incoming = exchangeRequestService.getIncoming(pageable);
        return ResponseEntity.ok(incoming);
    }

    @GetMapping("/outgoing")
    public ResponseEntity<Page<ExchangeResponseDTO>> getOutGoingRequests(Pageable pageable) {
        Page<ExchangeResponseDTO> outgoing = exchangeRequestService.getOutgoing(pageable);
        return ResponseEntity.ok(outgoing);
    }

    @PutMapping("/{reqId}/status")
    public ResponseEntity<String> changeRequestStatus(@PathVariable Long reqId,
                                                      @Valid @RequestBody RequestStatusDTO statusDTO) {
        exchangeRequestService.updateExchangeStatus(reqId, statusDTO);
        return ResponseEntity.ok("Status Updated Successfully");
    }

}
