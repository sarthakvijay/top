package com.idea.toptal.service;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Player;
import com.idea.toptal.models.Transfer;
import com.idea.toptal.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    PlayerService playerService;

    @Autowired
    TeamService teamService;

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    public Transfer getTransferById(Long id) throws RecordNotFoundException {
        Optional<Transfer> transfer = transferRepository.findById(id);
        if(transfer.isPresent()){
            return transfer.get();
        }
        throw new RecordNotFoundException("No transfer with the given id present in Database");
    }

    public Transfer createOrUpdatePlayer(Transfer transfer, String username) throws RecordNotFoundException {
        Optional<Transfer> to_update = transferRepository.findByPlayerId(transfer.getPlayer().getId());
        if(to_update.isPresent()) {
            Transfer newEntity = to_update.get();
            newEntity.setAsk_value(transfer.getAsk_value());
            transfer = newEntity;
        } else {
            Player player = playerService.getPlayerById(transfer.getPlayer().getId());
            transfer = new Transfer(player, transfer.getAsk_value());
        }
        if(!teamService.getTeamById(transfer.getPlayer().getTeamId()).getId().equals(username)){
            throw new RecordNotFoundException("Error: player does not belong to your user.");
        }
        transfer = transferRepository.save(transfer);
        return transfer;
    }

    public Transfer buyTransfer(Transfer transfer, String username) throws RecordNotFoundException {
        Optional<Transfer> to_update = transferRepository.findByPlayerId(transfer.getPlayer().getId());
        if(to_update.isPresent()) {
            transfer = to_update.get();
            if(teamService.getTeamById(transfer.getPlayer().getTeamId()).getId().equals(username)){
                throw new RecordNotFoundException("Error: player does belong to your user.");
            }
            if(teamService.getTeamById(username).getBudget() < transfer.getAsk_value())
                throw new RecordNotFoundException("Error: player asking price is above available budget");
            Player player = transfer.getPlayer();
            updateBudgetMarketValue(username, transfer, player);
            deleteTransferById(transfer.getId(), username);
            player.setTeamId(username);
            playerService.createOrUpdatePlayer(player);
        } else {
            throw new RecordNotFoundException("Error: No such player present in the transfer list.");
        }

        return transfer;
    }

    private void updateBudgetMarketValue(String username, Transfer transfer, Player player) throws RecordNotFoundException {
        teamService.updateBudget(player.getTeamId(), transfer.getAsk_value());
        player.updateMarketValue(transfer.getAsk_value());
        player.setTeamId(username);
        teamService.updateBudget(username, transfer.getAsk_value(), player.getMarketvalue());
    }

    public HttpStatus deleteTransferById(Long id, String username) throws RecordNotFoundException {
        Optional<Transfer> transfer = transferRepository.findById(id);
        if(transfer.isPresent()){
            if(!transfer.get().getPlayer().getTeamId().equals(username)){
                return HttpStatus.FORBIDDEN;
            }
            transferRepository.deleteById(id);
            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }
}
