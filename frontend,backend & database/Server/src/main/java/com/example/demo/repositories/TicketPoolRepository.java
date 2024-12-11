package com.example.demo.repositories;

import com.example.demo.models.TicketPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketPoolRepository extends JpaRepository<TicketPool, Integer> {

    @Query("SELECT t, v from TicketPool t , Vendor v WHERE t.vendor.id = v.id")
    List<TicketPool> findAllTickets();

    Optional<TicketPool> findById(Integer id);

    Optional<TicketPool> findByVendorId(Integer id);

    @Query("SELECT sum(t.count) FROM TicketPool t")
    Integer findAllTicketCount();

    @Query("SELECT t FROM TicketPool t WHERE t.vendor.id = :id")
    List<TicketPool> findAllByVendorId(Integer id);
}
