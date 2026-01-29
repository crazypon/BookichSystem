package com.ilnur.bookich.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilnur.bookich.dtos.ExchangeRequestDTO;
import com.ilnur.bookich.dtos.ExchangeResponseDTO;
import com.ilnur.bookich.dtos.RequestStatusDTO;
import com.ilnur.bookich.enums.ExchangeStatus;
import com.ilnur.bookich.enums.ExchangeType;
import com.ilnur.bookich.services.ExchangeRequestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExchangeController.class)
class ExchangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExchangeRequestService exchangeRequestService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    @DisplayName("POST /request: Should create exchange request successfully")
    void createRequest_Success() throws Exception {
        // Given
        ExchangeRequestDTO requestDTO = new ExchangeRequestDTO();
        requestDTO.setRequestedBookId(10L);
        requestDTO.setReceiverId("175839");
        requestDTO.setOfferedBookId(2L);
        requestDTO.setType(ExchangeType.GIFT);

        mockMvc.perform(post("/api/exchange/request")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Exchange Request Created Successfully"));

        verify(exchangeRequestService).createRequest(any(ExchangeRequestDTO.class));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /incoming: Should return paged list of incoming requests")
    void getIncomingRequests_Success() throws Exception {
        ExchangeResponseDTO responseDTO = new ExchangeResponseDTO();
        List<ExchangeResponseDTO> list = Collections.singletonList(responseDTO);
        Page<ExchangeResponseDTO> mockPage = new PageImpl<>(list);

        given(exchangeRequestService.getIncoming(any(Pageable.class))).willReturn(mockPage);

        mockMvc.perform(get("/api/exchange/incoming")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0]").exists())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /outgoing: Should return paged list of outgoing requests")
    void getOutgoingRequests_Success() throws Exception {
        Page<ExchangeResponseDTO> mockPage = new PageImpl<>(Collections.emptyList());
        given(exchangeRequestService.getOutgoing(any(Pageable.class))).willReturn(mockPage);

        mockMvc.perform(get("/api/exchange/outgoing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /{reqId}/status: Should update status successfully")
    void changeRequestStatus_Success() throws Exception {
        Long reqId = 10L;
        RequestStatusDTO statusDTO = new RequestStatusDTO();
        statusDTO.setStatus(ExchangeStatus.ACCEPTED);

        mockMvc.perform(put("/api/exchange/{reqId}/status", reqId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Status Updated Successfully"));

        verify(exchangeRequestService).updateExchangeStatus(eq(reqId), any(RequestStatusDTO.class));
    }
}