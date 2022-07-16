package com.idea.toptal.repository;

import com.idea.toptal.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Optional<Transfer> findByPlayerId (Long id);

}
