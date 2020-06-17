/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-core.
 *
 * Othello-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Othello-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with othello-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.othello.core.domain.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import de.fhdw.gaming.core.domain.PlayerState;
import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloState;

/**
 * Stub implementation of {@link OthelloState} for testing.
 */
public final class OthelloStateStub implements OthelloState {

    /**
     * The board.
     */
    private final OthelloBoard board;
    /**
     * The player using black tokens.
     */
    private final OthelloPlayer blackPlayer;
    /**
     * The player using white tokens.
     */
    private final OthelloPlayer whitePlayer;
    /**
     * The currently active player.
     */
    private OthelloPlayer currentPlayer;
    /**
     * The number of consecutive skips.
     */
    private int numberOfConsecutiveSkips;
    /**
     * The states of the players.
     */
    private final Map<String, PlayerState> playerStates;

    /**
     * Creates an Othello state stub.
     *
     * @param board The board.
     */
    public OthelloStateStub(final OthelloBoard board) {
        this.board = board;
        this.blackPlayer = new OthelloPlayerImpl(this, "Black", true);
        this.whitePlayer = new OthelloPlayerImpl(this, "White", false);
        this.currentPlayer = this.blackPlayer;
        this.numberOfConsecutiveSkips = 0;
        this.playerStates = new LinkedHashMap<>();
        this.playerStates.put(this.blackPlayer.getName(), PlayerState.PLAYING);
        this.playerStates.put(this.whitePlayer.getName(), PlayerState.PLAYING);
    }

    @Override
    public void nextTurn() {
        if (this.currentPlayer == this.blackPlayer) {
            this.currentPlayer = this.whitePlayer;
        } else {
            this.currentPlayer = this.blackPlayer;
        }
    }

    @Override
    public OthelloState deepCopy() {
        return new OthelloStateStub(this.board.deepCopy());
    }

    @Override
    public OthelloBoard getBoard() {
        return this.board;
    }

    @Override
    public Map<String, OthelloPlayer> getPlayers() {
        final Map<String, OthelloPlayer> result = new LinkedHashMap<>();
        result.put(this.blackPlayer.getName(), this.blackPlayer);
        result.put(this.whitePlayer.getName(), this.whitePlayer);
        return result;
    }

    @Override
    public PlayerState getPlayerState(final String playerName) {
        return this.playerStates.get(playerName);
    }

    @Override
    public void setPlayerState(final String playerName, final PlayerState newState) {
        this.playerStates.put(playerName, newState);
    }

    @Override
    public Set<OthelloPlayer> computeNextPlayers() {
        return Collections.singleton(this.currentPlayer);
    }

    @Override
    public OthelloPlayer getBlackPlayer() {
        return this.blackPlayer;
    }

    @Override
    public OthelloPlayer getWhitePlayer() {
        return this.whitePlayer;
    }

    @Override
    public OthelloPlayer getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public void moveCompleted(final boolean skipMove) {
        if (skipMove) {
            ++this.numberOfConsecutiveSkips;
        } else {
            this.numberOfConsecutiveSkips = 0;
        }
    }

    @Override
    public int getNumberOfConsecutiveSkips() {
        return this.numberOfConsecutiveSkips;
    }
}
