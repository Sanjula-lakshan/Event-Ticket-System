package com.example.demo.repositories;

import com.example.demo.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {

    Vendor findByEmail(String email);

    Optional<Vendor> findById(Integer id);

}
