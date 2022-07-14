package com.idea.toptal.controllers;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Team;
import com.idea.toptal.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Autowired
    TeamService teamService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR')")
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<Team> getTeamById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        Team team = teamService.getTeamById(id);
        return new ResponseEntity<Team>(team, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<Team> createOrUpdateTeam(@Valid @RequestBody Team team) {
        Team updated = teamService.createOrUpdateTeam(team);
        return new ResponseEntity<Team>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public HttpStatus deleteTeamById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        teamService.deleteTeamById(id);
        return HttpStatus.FORBIDDEN;
    }
}
