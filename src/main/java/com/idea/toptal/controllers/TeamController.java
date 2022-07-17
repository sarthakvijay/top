package com.idea.toptal.controllers;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Team;
import com.idea.toptal.payload.response.MessageResponse;
import com.idea.toptal.service.TeamService;
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

@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Autowired
    TeamService teamService;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Accessing all the teams part of the league.
     * @return
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    /**
     * Getting your details, accessed using the username from JWT token.
     * @param request
     * @return
     * @throws RecordNotFoundException
     */
    @GetMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Team> getTeamById(HttpServletRequest request)
            throws RecordNotFoundException {
        String jwt = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Team team = teamService.getTeamById(username);
        return new ResponseEntity<Team>(team, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Updating team details using JSON object as part of POST request.
     * @param request
     * @param team
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<Team> updateTeam(HttpServletRequest request, @Valid @RequestBody Team team) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        if(!team.getId().equals(username)){
            ResponseEntity.ok(new MessageResponse("Error: Not your team."));
        }
        Team updated = teamService.updateTeam(team);
        return new ResponseEntity<Team>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Deleting your team details from using.
     * @param request
     * @param id
     * @return
     * @throws RecordNotFoundException
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public HttpStatus deleteTeamById(HttpServletRequest request, @PathVariable("id") String id)
            throws RecordNotFoundException {
        String jwt = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        return teamService.deleteTeamById(id, username);
    }
}
