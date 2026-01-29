package com.ilnur.bookich.services;

import com.ilnur.bookich.dtos.ExchangeRequestDTO;
import com.ilnur.bookich.dtos.ExchangeResponseDTO;
import com.ilnur.bookich.dtos.RequestStatusDTO;
import com.ilnur.bookich.entities.Book;
import com.ilnur.bookich.entities.ExchangeRequest;
import com.ilnur.bookich.entities.User;
import com.ilnur.bookich.enums.ExchangeStatus;
import com.ilnur.bookich.mappers.ExchangeRequestMapper;
import com.ilnur.bookich.repositories.BookRepository;
import com.ilnur.bookich.repositories.ExchangeRequestRepository;
import com.ilnur.bookich.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRequestServiceTest {

    @Mock private ExchangeRequestRepository exchangeRequestRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserContextService userContextService;
    @Mock private BookRepository bookRepository;
    @Mock private ExchangeRequestMapper exchangeRequestMapper;

    @InjectMocks
    private ExchangeRequestService service;

    private User initiator;
    private User receiver;
    private Book offeredBook;
    private Book requestedBook;
    private ExchangeRequestDTO requestDTO;
    private ExchangeRequest exchangeRequest;

    @BeforeEach
    void setUp() {
        initiator = new User();
        initiator.setId(1L);
        initiator.setUsername("initiator");

        receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("receiver");
        receiver.setPublicId("106932");

        offeredBook = new Book();
        offeredBook.setId(10L);
        offeredBook.setOwner(initiator);

        requestedBook = new Book();
        requestedBook.setId(20L);
        requestedBook.setOwner(receiver);

        requestDTO = new ExchangeRequestDTO();
        requestDTO.setReceiverId(receiver.getPublicId());
        requestDTO.setOfferedBookId(offeredBook.getId());
        requestDTO.setRequestedBookId(requestedBook.getId());

        exchangeRequest = new ExchangeRequest();
        exchangeRequest.setId(100L);
        exchangeRequest.setInitiator(initiator);
        exchangeRequest.setReceiver(receiver);
        exchangeRequest.setOfferedBook(offeredBook);
        exchangeRequest.setRequestedBook(requestedBook);
        exchangeRequest.setStatus(ExchangeStatus.PENDING);
    }

    @Test
    @DisplayName("Create Request: Success")
    void createRequest_Success() {
        when(userRepository.findByPublicId(requestDTO.getReceiverId())).thenReturn(Optional.of(receiver));
        when(userContextService.getCurrentUser()).thenReturn(initiator);
        when(bookRepository.findById(requestDTO.getOfferedBookId())).thenReturn(Optional.of(offeredBook));
        when(bookRepository.findById(requestDTO.getRequestedBookId())).thenReturn(Optional.of(requestedBook));
        when(exchangeRequestMapper.toExchangeRequest(requestDTO)).thenReturn(exchangeRequest);

        service.createRequest(requestDTO);

        verify(exchangeRequestRepository).save(any(ExchangeRequest.class));
    }

    @Test
    @DisplayName("Create Request: Fails when Receiver ID not found")
    void createRequest_ReceiverNotFound() {
        when(userRepository.findByPublicId(any())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.createRequest(requestDTO));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No Such Receiver With This ID", ex.getReason());
    }

    @Test
    @DisplayName("Create Request: Fails when Initiator tries to exchange with themselves")
    void createRequest_SelfExchange() {
        when(userRepository.findByPublicId(requestDTO.getReceiverId())).thenReturn(Optional.of(initiator));
        when(userContextService.getCurrentUser()).thenReturn(initiator);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.createRequest(requestDTO));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("You cannot Exchange with Yourself", ex.getReason());
    }

    @Test
    @DisplayName("Create Request: Fails when Offered Book not found")
    void createRequest_OfferedBookNotFound() {
        when(userRepository.findByPublicId(requestDTO.getReceiverId())).thenReturn(Optional.of(receiver));
        when(userContextService.getCurrentUser()).thenReturn(initiator);
        when(bookRepository.findById(requestDTO.getOfferedBookId())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.createRequest(requestDTO));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No Such Book to Exchange", ex.getReason());
    }

    @Test
    @DisplayName("Create Request: Fails when Initiator does not own the offered book")
    void createRequest_NotOwner() {
        offeredBook.setOwner(receiver);

        when(userRepository.findByPublicId(requestDTO.getReceiverId())).thenReturn(Optional.of(receiver));
        when(userContextService.getCurrentUser()).thenReturn(initiator);
        when(bookRepository.findById(requestDTO.getOfferedBookId())).thenReturn(Optional.of(offeredBook));
        when(bookRepository.findById(requestDTO.getRequestedBookId())).thenReturn(Optional.of(requestedBook));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.createRequest(requestDTO));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("You are not the owner of offered book", ex.getReason());
    }

    @Test
    @DisplayName("Get Incoming: Success")
    void getIncoming_Success() {
        Pageable pageable = Pageable.unpaged();
        Page<ExchangeRequest> page = new PageImpl<>(List.of(exchangeRequest));

        when(userContextService.getCurrentUser()).thenReturn(receiver);
        when(exchangeRequestRepository.getExchangeRequestsByReceiver(receiver, pageable)).thenReturn(page);
        when(exchangeRequestMapper.toResponseDTO(exchangeRequest)).thenReturn(new ExchangeResponseDTO());

        Page<ExchangeResponseDTO> result = service.getIncoming(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Get Outgoing: Success")
    void getOutgoing_Success() {
        Pageable pageable = Pageable.unpaged();
        Page<ExchangeRequest> page = new PageImpl<>(List.of(exchangeRequest));

        when(userContextService.getCurrentUser()).thenReturn(initiator);
        when(exchangeRequestRepository.getExchangeRequestsByInitiator(initiator, pageable)).thenReturn(page);
        when(exchangeRequestMapper.toResponseDTO(exchangeRequest)).thenReturn(new ExchangeResponseDTO());

        Page<ExchangeResponseDTO> result = service.getOutgoing(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Update Status: Success")
    void updateStatus_Success() {
        RequestStatusDTO statusDTO = new RequestStatusDTO();
        statusDTO.setStatus(ExchangeStatus.ACCEPTED);

        when(userContextService.getCurrentUser()).thenReturn(receiver);
        when(exchangeRequestRepository.findById(100L)).thenReturn(Optional.of(exchangeRequest));

        service.updateExchangeStatus(100L, statusDTO);

        assertEquals(ExchangeStatus.ACCEPTED, exchangeRequest.getStatus());
        verify(exchangeRequestRepository).save(exchangeRequest);
    }

    @Test
    @DisplayName("Update Status: Fails when Request ID not found")
    void updateStatus_NotFound() {
        RequestStatusDTO statusDTO = new RequestStatusDTO();
        when(exchangeRequestRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.updateExchangeStatus(999L, statusDTO));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    @DisplayName("Update Status: Fails when Current User is not the Receiver")
    void updateStatus_Forbidden_NotReceiver() {
        RequestStatusDTO statusDTO = new RequestStatusDTO();

        // Initiator tries to accept their own request? Forbidden!
        when(exchangeRequestRepository.findById(100L)).thenReturn(Optional.of(exchangeRequest));
        when(userContextService.getCurrentUser()).thenReturn(initiator);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.updateExchangeStatus(100L, statusDTO));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertEquals("Only Receiver Can Update the Status", ex.getReason());
    }

    @Test
    @DisplayName("Update Status: Fails when Status is not PENDING (Double update)")
    void updateStatus_Forbidden_NotPending() {
        RequestStatusDTO statusDTO = new RequestStatusDTO();
        exchangeRequest.setStatus(ExchangeStatus.ACCEPTED); // Already accepted

        when(exchangeRequestRepository.findById(100L)).thenReturn(Optional.of(exchangeRequest));
        when(userContextService.getCurrentUser()).thenReturn(receiver);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.updateExchangeStatus(100L, statusDTO));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertEquals("You Cannot Update Old Requests", ex.getReason());
    }
}