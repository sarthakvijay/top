package com.idea.toptal.controllers;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Transfer;
import com.idea.toptal.payload.response.MessageResponse;
import com.idea.toptal.service.TransferService;
import com.idea.toptal.service.security.jwt.JwtUtils;
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
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    TransferService transferService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR')")
    public List<Transfer> getAllTransfers(){
        List<Transfer> transfers =  transferService.getAllTransfers();
        return transfers;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<Transfer> getTransferById(@PathVariable("id") Long id) throws RecordNotFoundException {
        Transfer transfer = transferService.getTransferById(id);
        return new ResponseEntity<>(transfer, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> createOrUpdateTransfer(HttpServletRequest request, @Valid @RequestBody Transfer transfer){
        String jwt = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Transfer updated = null;
        try {
            updated = transferService.createOrUpdatePlayer(transfer, username);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        return new ResponseEntity<Transfer>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/buy")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> buyTransfer(HttpServletRequest request, @Valid @RequestBody Transfer transfer){
        String jwt = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Transfer updated = null;
        try {
            updated = transferService.buyTransfer(transfer, username);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        return new ResponseEntity<Transfer>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public HttpStatus deleteTransferById(@PathVariable("id") Long id) throws RecordNotFoundException {
        transferService.deleteTransferById(id);
        return HttpStatus.FORBIDDEN;
    }
}