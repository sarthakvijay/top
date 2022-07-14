package com.idea.toptal.controllers;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Player;
import com.idea.toptal.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR')")
    public List<Player> getAllPlayers(){
        List<Player> players =  playerService.getAllPlayers();
        return players;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") Long id) throws RecordNotFoundException {
        Player player = playerService.getPlayerById(id);
        return new ResponseEntity<>(player, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<Player> createOrUpdatePlayer(@Valid @RequestBody Player player){
        Player updated = playerService.createOrUpdatePlayer(player);
        return new ResponseEntity<Player>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public HttpStatus deletePlayerById(@PathVariable("id") Long id) throws RecordNotFoundException {
        playerService.deletePlayerById(id);
        return HttpStatus.FORBIDDEN;
    }
}
