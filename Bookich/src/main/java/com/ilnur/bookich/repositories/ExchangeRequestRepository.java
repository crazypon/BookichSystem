package com.ilnur.bookich.repositories;

import com.ilnur.bookich.entities.ExchangeRequest;
import com.ilnur.bookich.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    /*
    With this query we are able to solve N+1 problem, where we do too many calls to the database, instead
    we use JOIN FETCH this is same as simple join. This is not SQL, this is JPSQL. So it is different, so basically
    with joins we are telling what should we include in our result row.

    SO with "JOIN FETCHES"s we are just telling to make FetchType.EAGER, for those objects we asked for. For example,
    offeredBook, will include a row of offered book as well
     */
    @Query("""
        SELECT r FROM ExchangeRequest r
        JOIN FETCH r.offeredBook
        LEFT JOIN FETCH r.requestedBook
        JOIN FETCH r.initiator
        WHERE r.receiver = :receiver
    """)
    Page<ExchangeRequest> getExchangeRequestsByReceiver(@Param("receiver") User receiver, Pageable pageable);

    @Query("""
        SELECT r FROM ExchangeRequest r
        JOIN FETCH r.offeredBook
        LEFT JOIN FETCH r.requestedBook
        JOIN FETCH r.initiator
        WHERE r.initiator = :initiator
    """)
    Page<ExchangeRequest> getExchangeRequestsByInitiator(@Param("initiator") User initiator, Pageable pageable);
}
