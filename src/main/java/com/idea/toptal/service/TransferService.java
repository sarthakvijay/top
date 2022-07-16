package com.idea.toptal.service;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Player;
import com.idea.toptal.models.Transfer;
import com.idea.toptal.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            playerService.createOrUpdatePlayer(player);
            deleteTransferById(transfer.getId());
        } else {
            throw new RecordNotFoundException("Error: No such player present in the transfer list.");
        }

        return transfer;
    }

    private void updateBudgetMarketValue(String username, Transfer transfer, Player player) throws RecordNotFoundException {
        teamService.updateBudget(player.getTeamId(), transfer.getAsk_value(), false);
        player.updateMarketValue(transfer.getAsk_value());
        player.setTeamId(username);
        teamService.updateBudget(username, player.getMarketvalue(), true);
    }


    public void deleteTransferById(Long id) throws RecordNotFoundException {
        Optional<Transfer> transfer = transferRepository.findById(id);
        if(transfer.isPresent()){
            transferRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No transfer found with given in the database");
        }
    }
}
