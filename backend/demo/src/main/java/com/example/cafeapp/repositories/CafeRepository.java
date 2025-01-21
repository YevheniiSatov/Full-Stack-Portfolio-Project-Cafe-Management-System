package com.example.cafeapp.repositories;


import com.example.cafeapp.entities.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {
    Optional<Cafe> findByEmail(String email);
    Optional<Cafe> findByName(String name);
    boolean existsByEmail(String email);

}
