package com.idea.toptal.repository;

import com.idea.toptal.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRespository extends JpaRepository<Player, Long> {
    Optional<List<Player>> findByTeamId(String username);
}
