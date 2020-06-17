/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-core.
 *
 * Othello-core is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Othello-core is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * othello-core. If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.othello.core.domain.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.domain.PlayerState;
import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloPlayerBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloState;

/**
 * Implements {@link OthelloState}.
 */
final class OthelloStateImpl implements OthelloState {

    /**
     * The board.
     */
    private final OthelloBoard board;
    /**
     * The player using the black tokens.
     */
    private final OthelloPlayer blackPlayer;
    /**
     * The player using the white tokens.
     */
    private final OthelloPlayer whitePlayer;
    /**
     * The current player.
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
     * Creates an Othello state.
     *
     * @param board              The board.
     * @param blackPlayerBuilder A builder for the player using the black tokens.
     * @param whitePlayerBuilder A builder for the player using the white tokens.
     * @param blackIsNext        {@code true} if black is next, else white is next.
     * @throws GameException if the state cannot be created according to the rules of the game.
     */
    OthelloStateImpl(final OthelloBoard board, final OthelloPlayerBuilder blackPlayerBuilder,
            final OthelloPlayerBuilder whitePlayerBuilder, final boolean blackIsNext) throws GameException {

        this.board = Objects.requireNonNull(board, "board");
        this.blackPlayer = Objects.requireNonNull(blackPlayerBuilder, "blackPlayerBuilder").build(this);
        this.whitePlayer = Objects.requireNonNull(whitePlayerBuilder, "whitePlayerBuilder").build(this);
        this.currentPlayer = blackIsNext ? this.blackPlayer : this.whitePlayer;
        this.numberOfConsecutiveSkips = 0;

        this.playerStates = new LinkedHashMap<>();
        this.playerStates.put(this.blackPlayer.getName(), PlayerState.PLAYING);
        this.playerStates.put(this.whitePlayer.getName(), PlayerState.PLAYING);

        if (!this.blackPlayer.isUsingBlackTokens()) {
            throw new IllegalArgumentException(
                    String.format("Black player %s does not use black tokens.", this.blackPlayer));
        }
        if (this.whitePlayer.isUsingBlackTokens()) {
            throw new IllegalArgumentException(
                    String.format("White player %s does not use white tokens.", this.whitePlayer));
        }
        if (this.blackPlayer.getName().equals(this.whitePlayer.getName())) {
            throw new IllegalArgumentException(
                    String.format("Both players have the same name '%s'.", this.blackPlayer.getName()));
        }
    }

    /**
     * Creates an Othello state by copying an existing one.
     *
     * @param source The state to copy.
     */
    OthelloStateImpl(final OthelloStateImpl source) {
        this.board = source.board.deepCopy();
        this.blackPlayer = source.blackPlayer.deepCopy(this);
        this.whitePlayer = source.whitePlayer.deepCopy(this);
        this.currentPlayer = source.currentPlayer == source.blackPlayer ? this.blackPlayer : this.whitePlayer;
        this.numberOfConsecutiveSkips = source.numberOfConsecutiveSkips;
        this.playerStates = new LinkedHashMap<>();
        this.playerStates.put(this.blackPlayer.getName(), source.playerStates.get(this.blackPlayer.getName()));
        this.playerStates.put(this.whitePlayer.getName(), source.playerStates.get(this.whitePlayer.getName()));
    }

    @Override
    public String toString() {
        return String.format(
                "OthelloState[board=%s, blackPlayer=%s, whitePlayer=%s, currentPlayer=%s, numberOfConsecutiveSkips=%d]",
                this.board,
                this.blackPlayer,
                this.whitePlayer,
                this.currentPlayer.isUsingBlackTokens() ? OthelloFieldState.BLACK : OthelloFieldState.WHITE,
                this.numberOfConsecutiveSkips);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof OthelloStateImpl) {
            final OthelloStateImpl other = (OthelloStateImpl) obj;
            return this.board.equals(other.board) && this.blackPlayer.equals(other.blackPlayer)
                    && this.whitePlayer.equals(other.whitePlayer)
                    && (this.currentPlayer == this.blackPlayer) == (other.currentPlayer == other.blackPlayer)
                    && this.numberOfConsecutiveSkips == other.numberOfConsecutiveSkips;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.board,
                this.blackPlayer,
                this.whitePlayer,
                this.currentPlayer == this.blackPlayer,
                this.numberOfConsecutiveSkips);
    }

    @Override
    public OthelloStateImpl deepCopy() {
        return new OthelloStateImpl(this);
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
        final PlayerState playerState = this.playerStates.get(playerName);
        if (playerState != null) {
            return playerState;
        } else {
            throw new IllegalArgumentException(String.format("Unknown player %s.", playerName));
        }
    }

    @Override
    public void setPlayerState(final String playerName, final PlayerState newState) {
        if (this.playerStates.containsKey(playerName)) {
            this.playerStates.put(playerName, newState);
        } else {
            throw new IllegalArgumentException(String.format("Unknown player %s.", playerName));
        }
    }

    @Override
    public OthelloBoard getBoard() {
        return this.board;
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
            if (this.numberOfConsecutiveSkips > 1) {
                this.gameOver();
            }
        } else {
            this.numberOfConsecutiveSkips = 0;
        }
    }

    @Override
    public int getNumberOfConsecutiveSkips() {
        return this.numberOfConsecutiveSkips;
    }

    @Override
    public Set<OthelloPlayer> computeNextPlayers() {
        return Collections.singleton(this.currentPlayer);
    }

    @Override
    public void nextTurn() {
        this.currentPlayer = this.getOtherPlayer();
        if (this.numberOfConsecutiveSkips > 1) {
            this.gameOver();
        }
    }

    /**
     * Returns the currently inactive player.
     */
    private OthelloPlayer getOtherPlayer() {
        if (this.currentPlayer == this.blackPlayer) {
            return this.whitePlayer;
        } else {
            return this.blackPlayer;
        }
    }

    /**
     * Updates the player states after the game has finished.
     */
    private void gameOver() {
        final int blackTokens = this.board.getFieldsBeing(OthelloFieldState.BLACK).size();
        final int whiteTokens = this.board.getFieldsBeing(OthelloFieldState.WHITE).size();
        if (blackTokens > whiteTokens) {
            this.setPlayerState(this.blackPlayer.getName(), PlayerState.WON);
            this.setPlayerState(this.whitePlayer.getName(), PlayerState.LOST);
        } else if (blackTokens < whiteTokens) {
            this.setPlayerState(this.blackPlayer.getName(), PlayerState.LOST);
            this.setPlayerState(this.whitePlayer.getName(), PlayerState.WON);
        } else {
            this.setPlayerState(this.blackPlayer.getName(), PlayerState.DRAW);
            this.setPlayerState(this.whitePlayer.getName(), PlayerState.DRAW);
        }
    }
}
