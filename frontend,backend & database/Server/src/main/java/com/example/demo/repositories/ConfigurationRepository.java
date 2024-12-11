package com.example.demo.repositories;

import com.example.demo.models.Configuration;
import com.example.demo.models.TicketPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

    Optional<Configuration> findById(Integer id);

    @Query("SELECT sum(c.remainingtic) FROM Configuration c")
    Integer findAllRemainingTicketCount();
}
