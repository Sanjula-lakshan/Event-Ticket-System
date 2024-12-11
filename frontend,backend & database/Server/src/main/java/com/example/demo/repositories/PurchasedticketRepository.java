package com.example.demo.repositories;

import com.example.demo.models.Purchasedticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchasedticketRepository extends JpaRepository<Purchasedticket, Integer> {

    Optional<Purchasedticket> findById(Integer id);

    @Query("SELECT count(t.id) FROM Purchasedticket t")
    int countPurchasedticketTickets();
}
