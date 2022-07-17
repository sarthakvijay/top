package com.idea.toptal.controllers;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Player;
import com.idea.toptal.payload.response.MessageResponse;
import com.idea.toptal.service.PlayerService;
import com.idea.toptal.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Player> getAllPlayers(){
        List<Player> players =  playerService.getAllPlayers();
        return players;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getPlayerById(HttpServletRequest request, @PathVariable("id") Long id) throws RecordNotFoundException {
        String jwt = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Player player = playerService.getPlayerById(id);
        if(!player.getTeamId().equals(username)){
            return ResponseEntity.ok(new MessageResponse("Player not part of your team."));
        }
        return new ResponseEntity<>(player, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getTeamPlayers(HttpServletRequest request) throws RecordNotFoundException {
        String jwt = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Optional<List<Player>> player_list = playerService.getTeamPlayers(username);
        if(!player_list.isPresent()){
            ResponseEntity.ok(new MessageResponse("No player found on this team"));
        }
        return new ResponseEntity<>(player_list.get(), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> createOrUpdatePlayer(HttpServletRequest request, @Valid @RequestBody Player player) throws RecordNotFoundException {
        String jwt = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Player original_player = playerService.getPlayerById(player.getId());
        if(!original_player.getTeamId().equals(username)){
            return ResponseEntity.ok(new MessageResponse("Not your team player."));
        }
        Player updated = playerService.createOrUpdatePlayer(player);
        return new ResponseEntity<Player>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public HttpStatus deletePlayerById(HttpServletRequest request, @PathVariable("id") Long id) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        return playerService.deletePlayerById(id, username);
    }
}
