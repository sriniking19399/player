/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.player.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import javax.validation.OverridesAttribute;

import com.example.player.model.PlayerRowMapper;
import com.example.player.repository.PlayerRepository;
import com.example.player.model.Player;

@Service
public class PlayerH2Service implements PlayerRepository {

    @Autowired

    private JdbcTemplate db;

    @Override
    public ArrayList<Player> getAllPlayer() {
        List<Player> playerList = db.query("select * from team", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
        return players;
    }

    @Override

    public Player getPlayerById(int playerId) {
        try {
            Player player = db.queryForObject("select * from team where playerId=?",
                    new PlayerRowMapper(), playerId);
            return player;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Player addPlayer(Player player) {
        db.update("insert into team(playerName,jerseyNumber,role) values(?,?,?)",
                new PlayerRowMapper(), player.getPlayerName(), player.getJerseyNumber(), player.getRole());
        Player savePlayer = db.queryForObjet("select*from team where  playerName=? and jerseyNumber=? and role=?",
                new PlayerRowMapper(), player.getPlayerName(), player.getJerseyNumber(), player.getRole());
        return savePlayer;
    }

    @Override

    public Player updatePlayer(int playerId, Player player) {
        if (player.getPlayerName() != null) {
            db.update("update team set playerName=? where playerId=?", new PlayerRowMapper(), player.getPlayerName(),
                    playerId);
        }
        if (player.getJerseyNumber() != 0) {
            db.update("update team set jerseyNumber=?where playerId=?", new PlayerRowMapper(), player.getJerseyNumber(),
                    playerId);
        }
        if (player.getRole() != null) {
            db.update("update team set role=? where playerId=?", new PlayerRowMapper(), player.getRole(),
                    playerId);
        }

        return getPlayerById(playerId);

    }

    @Override
    public void deleteplayer(int playerId) {
        db.update("delete from team where playerId=?", new PlayerRowMapper(), playerId);
    }

}

