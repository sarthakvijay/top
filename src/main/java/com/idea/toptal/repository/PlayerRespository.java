package com.idea.toptal.repository;

import com.idea.toptal.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRespository extends JpaRepository<Player, Long> {

}
