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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.domain.PlayerState;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloPlayerBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.core.moves.factory.OthelloMoveFactory;
import de.fhdw.gaming.othello.core.moves.impl.OthelloDefaultMoveFactory;

/**
 * Tests {@link OthelloStateImpl}.
 */
class OthelloStateImplTest {

    /**
     * Name of player using black tokens.
     */
    private static final String BLACK_PLAYER_NAME = "Black";
    /**
     * Name of player using white tokens.
     */
    private static final String WHITE_PLAYER_NAME = "White";

    /**
     * The board.
     */
    private OthelloBoardImpl board;
    /**
     * The game state.
     */
    private OthelloState state;
    /**
     * The move factory.
     */
    private OthelloMoveFactory moveFactory;

    /**
     * Sets up the state.
     */
    @BeforeEach
    void setUp() throws GameException {
        this.board = new OthelloBoardImpl(4);
        final OthelloPlayerBuilder blackPlayerBuilder = new OthelloPlayerBuilderImpl()
                .changeName(OthelloStateImplTest.BLACK_PLAYER_NAME).changeUsingBlackTokens(true);
        final OthelloPlayerBuilder whitePlayerBuilder = new OthelloPlayerBuilderImpl()
                .changeName(OthelloStateImplTest.WHITE_PLAYER_NAME).changeUsingBlackTokens(false);
        this.state = new OthelloStateImpl(this.board, blackPlayerBuilder, whitePlayerBuilder, true);
        this.moveFactory = new OthelloDefaultMoveFactory();
    }

    /**
     * Tests {@link OthelloStateImpl#getPlayers()}.
     */
    @Test
    void testGetPlayers() {
        assertThat(this.state.getPlayers().keySet(), hasSize(2));
        final OthelloPlayer blackPlayer = this.state.getPlayers().get(OthelloStateImplTest.BLACK_PLAYER_NAME);
        final OthelloPlayer whitePlayer = this.state.getPlayers().get(OthelloStateImplTest.WHITE_PLAYER_NAME);
        assertThat(blackPlayer.getName(), is(equalTo(OthelloStateImplTest.BLACK_PLAYER_NAME)));
        assertThat(blackPlayer.isUsingBlackTokens(), is(equalTo(true)));
        assertThat(blackPlayer.getState(), is(equalTo(PlayerState.PLAYING)));
        assertThat(whitePlayer.getName(), is(equalTo(OthelloStateImplTest.WHITE_PLAYER_NAME)));
        assertThat(whitePlayer.isUsingBlackTokens(), is(equalTo(false)));
        assertThat(whitePlayer.getState(), is(equalTo(PlayerState.PLAYING)));
    }

    /**
     * Tests {@link OthelloStateImpl#getPlayerState(OthelloPlayer)} and
     * {@link OthelloStateImpl#setPlayerState(OthelloPlayer, PlayerState)}.
     */
    @Test
    void testGetAndSetPlayerState() {
        final OthelloPlayer blackPlayer = this.state.getPlayers().get(OthelloStateImplTest.BLACK_PLAYER_NAME);
        final OthelloPlayer whitePlayer = this.state.getPlayers().get(OthelloStateImplTest.WHITE_PLAYER_NAME);
        assertThat(this.state.getPlayerState(blackPlayer.getName()), is(equalTo(PlayerState.PLAYING)));
        assertThat(this.state.getPlayerState(whitePlayer.getName()), is(equalTo(PlayerState.PLAYING)));
        this.state.setPlayerState(blackPlayer.getName(), PlayerState.WON);
        assertThat(this.state.getPlayerState(blackPlayer.getName()), is(equalTo(PlayerState.WON)));
        assertThat(this.state.getPlayerState(whitePlayer.getName()), is(equalTo(PlayerState.PLAYING)));
        this.state.setPlayerState(whitePlayer.getName(), PlayerState.LOST);
        assertThat(this.state.getPlayerState(blackPlayer.getName()), is(equalTo(PlayerState.WON)));
        assertThat(this.state.getPlayerState(whitePlayer.getName()), is(equalTo(PlayerState.LOST)));
    }

    /**
     * Tests {@link OthelloStateImpl#getPlayerState(String)} with a wrong player.
     */
    @Test
    void testGetPlayerStateWithWrongPlayer() {
        assertThrows(IllegalArgumentException.class, () -> this.state.getPlayerState("Blue"));
    }

    /**
     * Tests {@link OthelloStateImpl#setPlayerState(OthelloPlayer, PlayerState)} with a wrong player.
     */
    @Test
    void testSetPlayerStateWithWrongPlayer() {
        assertThrows(IllegalArgumentException.class, () -> this.state.setPlayerState("Blue", PlayerState.RESIGNED));
    }

    /**
     * Tests {@link OthelloStateImpl#getBoard()}.
     */
    @Test
    void testGetBoard() {
        assertThat(this.state.getBoard(), is(equalTo(this.board)));
    }

    /**
     * Tests {@link OthelloStateImpl#getBlackPlayer()}.
     */
    @Test
    void testGetBlackPlayer() {
        final OthelloPlayer blackPlayer = this.state.getBlackPlayer();
        assertThat(blackPlayer.isUsingBlackTokens(), is(equalTo(true)));
        assertThat(this.state.getPlayers().get(OthelloStateImplTest.BLACK_PLAYER_NAME), is(sameInstance(blackPlayer)));
    }

    /**
     * Tests {@link OthelloStateImpl#getWhitePlayer()}.
     */
    @Test
    void testGetWhitePlayer() {
        final OthelloPlayer whitePlayer = this.state.getWhitePlayer();
        assertThat(whitePlayer.isUsingBlackTokens(), is(equalTo(false)));
        assertThat(this.state.getPlayers().get(OthelloStateImplTest.WHITE_PLAYER_NAME), is(sameInstance(whitePlayer)));
    }

    /**
     * Tests {@link OthelloStateImpl#getCurrentPlayer()}.
     */
    @Test
    void testGetCurrentPlayer() {
        final OthelloPlayer currentPlayer = this.state.getCurrentPlayer();
        assertThat(currentPlayer, is(sameInstance(this.state.getBlackPlayer())));
    }

    /**
     * Tests {@link OthelloStateImpl#computeNextPlayers()}.
     */
    @Test
    void testComputeNextPlayers() {
        assertThat(this.state.computeNextPlayers(), is(equalTo(Collections.singleton(this.state.getCurrentPlayer()))));
    }

    /**
     * Tests {@link OthelloStateImpl#moveCompleted(boolean)}.
     */
    @Test
    void testFlipCurrentPlayer() throws GameException {
        assertThat(this.state.getCurrentPlayer(), is(sameInstance(this.state.getBlackPlayer())));
        this.state.moveCompleted(false);
        this.state.nextTurn();
        assertThat(this.state.getCurrentPlayer(), is(sameInstance(this.state.getWhitePlayer())));
        this.state.moveCompleted(false);
        this.state.nextTurn();
        assertThat(this.state.getCurrentPlayer(), is(sameInstance(this.state.getBlackPlayer())));
    }

    /**
     * Tests {@link OthelloStateImpl#getNumberOfConsecutiveSkips()}.
     */
    @Test
    void testGetNumberOfConsecutiveSkips() throws GameException {
        assertThat(this.state.getNumberOfConsecutiveSkips(), is(equalTo(0)));
        this.state.moveCompleted(true);
        this.state.nextTurn();
        assertThat(this.state.getNumberOfConsecutiveSkips(), is(equalTo(1)));
        this.state.moveCompleted(false);
        this.state.nextTurn();
        assertThat(this.state.getNumberOfConsecutiveSkips(), is(equalTo(0)));
        this.state.moveCompleted(true);
        this.state.nextTurn();
        assertThat(this.state.getNumberOfConsecutiveSkips(), is(equalTo(1)));
        this.state.moveCompleted(true);
        this.state.nextTurn();
        assertThat(this.state.getNumberOfConsecutiveSkips(), is(equalTo(2)));
    }

    /**
     * Tests {@link OthelloStateImpl#isFinished()} after two consecutive skip moves.
     */
    @Test
    void testIsFinishedAfterSkipMoves() throws GameException {
        assertThat(this.state.getBlackPlayer().getState(), is(equalTo(PlayerState.PLAYING)));
        assertThat(this.state.getWhitePlayer().getState(), is(equalTo(PlayerState.PLAYING)));

        this.state.moveCompleted(true);
        this.state.nextTurn();
        assertThat(this.state.getBlackPlayer().getState(), is(equalTo(PlayerState.PLAYING)));
        assertThat(this.state.getWhitePlayer().getState(), is(equalTo(PlayerState.PLAYING)));

        this.state.moveCompleted(false);
        this.state.nextTurn();
        assertThat(this.state.getBlackPlayer().getState(), is(equalTo(PlayerState.PLAYING)));
        assertThat(this.state.getWhitePlayer().getState(), is(equalTo(PlayerState.PLAYING)));

        this.state.moveCompleted(true);
        this.state.nextTurn();
        assertThat(this.state.getBlackPlayer().getState(), is(equalTo(PlayerState.PLAYING)));
        assertThat(this.state.getWhitePlayer().getState(), is(equalTo(PlayerState.PLAYING)));

        this.state.moveCompleted(true);
        this.state.nextTurn();
        assertThat(this.state.getBlackPlayer().getState(), is(equalTo(PlayerState.DRAW)));
        assertThat(this.state.getWhitePlayer().getState(), is(equalTo(PlayerState.DRAW)));
    }

    /**
     * Tests {@link OthelloStateImpl#deepCopy()}.
     */
    @Test
    void testDeepCopy() throws GameException {
        final OthelloState other = this.state.deepCopy();
        assertThat(this.state, is(equalTo(other)));

        this.state.moveCompleted(false);
        this.state.nextTurn();
        assertThat(this.state, is(not(equalTo(other))));
        other.moveCompleted(false);
        other.nextTurn();
        assertThat(this.state, is(equalTo(other)));

        this.state.setPlayerState(this.state.getCurrentPlayer().getName(), PlayerState.WON);
        assertThat(this.state, is(not(equalTo(other))));
        other.setPlayerState(other.getCurrentPlayer().getName(), PlayerState.WON);
        assertThat(this.state, is(equalTo(other)));
    }

    /**
     * Tests {@link OthelloStateImpl#toString()}.
     */
    @Test
    void testToString() throws GameException {
        assertThat(
                this.state.toString(),
                is(
                        equalTo(
                                String.format(
                                        "OthelloState["
                                                + "board=%s, blackPlayer=%s, whitePlayer=%s, currentPlayer=black, "
                                                + "numberOfConsecutiveSkips=0]",
                                        this.state.getBoard(),
                                        this.state.getBlackPlayer(),
                                        this.state.getWhitePlayer(),
                                        this.state.getNumberOfConsecutiveSkips()))));

        this.state.nextTurn();

        assertThat(
                this.state.toString(),
                is(
                        equalTo(
                                String.format(
                                        "OthelloState["
                                                + "board=%s, blackPlayer=%s, whitePlayer=%s, currentPlayer=white, "
                                                + "numberOfConsecutiveSkips=0]",
                                        this.state.getBoard(),
                                        this.state.getBlackPlayer(),
                                        this.state.getWhitePlayer(),
                                        this.state.getNumberOfConsecutiveSkips()))));
    }

    /**
     * Tests {@link OthelloStateImpl#equals(Object)}.
     */
    @Test
    void testEquals() throws GameException {
        final OthelloState copy = this.state.deepCopy();
        assertThat(this.state, is(equalTo(copy)));

        copy.getBoard().getFieldAt(pos(0, 2)).placeToken(false);
        assertThat(this.state, is(not(equalTo(copy))));
        this.state.getBoard().getFieldAt(pos(0, 2)).placeToken(false);
        assertThat(this.state, is(equalTo(copy)));

        copy.moveCompleted(true);
        assertThat(this.state, is(not(equalTo(copy))));
        this.state.moveCompleted(true);
        assertThat(this.state, is(equalTo(copy)));

        copy.nextTurn();
        assertThat(this.state, is(not(equalTo(copy))));
        this.state.nextTurn();
        assertThat(this.state, is(equalTo(copy)));

        copy.setPlayerState(OthelloStateImplTest.BLACK_PLAYER_NAME, PlayerState.LOST);
        assertThat(this.state, is(not(equalTo(copy))));
        this.state.setPlayerState(OthelloStateImplTest.BLACK_PLAYER_NAME, PlayerState.LOST);
        assertThat(this.state, is(equalTo(copy)));

        copy.setPlayerState(OthelloStateImplTest.WHITE_PLAYER_NAME, PlayerState.LOST);
        assertThat(this.state, is(not(equalTo(copy))));
        this.state.setPlayerState(OthelloStateImplTest.WHITE_PLAYER_NAME, PlayerState.LOST);
        assertThat(this.state, is(equalTo(copy)));

        assertThat(this.state, is(not(equalTo(this.state.getBoard()))));
    }

    /**
     * Tests {@link OthelloStateImpl#hashCode()}.
     */
    @Test
    void testHashCode() {
        final OthelloState copy = this.state.deepCopy();
        assertThat(this.state.hashCode(), is(equalTo(copy.hashCode())));

        copy.nextTurn();
        this.state.nextTurn();
        assertThat(this.state.hashCode(), is(equalTo(copy.hashCode())));
    }

    /**
     * Tests creating a state with two black players.
     */
    @Test
    void testCreateStateWithTwoBlackPlayers() {
        final OthelloPlayerBuilder blackPlayerBuilder = new OthelloPlayerBuilderImpl()
                .changeName(OthelloStateImplTest.BLACK_PLAYER_NAME).changeUsingBlackTokens(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> new OthelloStateImpl(this.board, blackPlayerBuilder, blackPlayerBuilder, true));
    }

    /**
     * Tests creating a state with two white players.
     */
    @Test
    void testCreateStateWithTwoWhitePlayers() {
        final OthelloPlayerBuilder whitePlayerBuilder = new OthelloPlayerBuilderImpl()
                .changeName(OthelloStateImplTest.WHITE_PLAYER_NAME).changeUsingBlackTokens(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> new OthelloStateImpl(this.board, whitePlayerBuilder, whitePlayerBuilder, true));
    }

    /**
     * Tests creating a state with two equally named players.
     */
    @Test
    void testCreateStateWithTwoPlayersWithTheSameName() {
        final OthelloPlayerBuilder blackPlayerBuilder = new OthelloPlayerBuilderImpl()
                .changeName(OthelloStateImplTest.BLACK_PLAYER_NAME).changeUsingBlackTokens(true);
        final OthelloPlayerBuilder whitePlayerBuilder = new OthelloPlayerBuilderImpl()
                .changeName(OthelloStateImplTest.BLACK_PLAYER_NAME).changeUsingBlackTokens(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> new OthelloStateImpl(this.board, blackPlayerBuilder, whitePlayerBuilder, true));
    }

    /**
     * Tests a complete game on a 4x4 board using moves where black wins.
     */
    @Test
    void testCompleteGameBlackWins() throws GameException {
        this.place(0, 1, true);
        this.place(0, 2, false);
        this.place(0, 3, true);
        this.place(2, 0, false);
        this.place(3, 0, true);
        this.place(0, 0, false);
        this.place(1, 0, true);
        this.skip(false);
        this.place(3, 3, true);
        this.skip(false);
        this.skip(true);

        assertThat(
                this.state.getBoard().toString(),
                is(
                        equalTo(
                                "OthelloBoard[size=4, fields=[[" + "OthelloField[position=A1, state=white], "
                                        + "OthelloField[position=B1, state=black], "
                                        + "OthelloField[position=C1, state=black], "
                                        + "OthelloField[position=D1, state=black]], ["
                                        + "OthelloField[position=A2, state=black], "
                                        + "OthelloField[position=B2, state=black], "
                                        + "OthelloField[position=C2, state=black], "
                                        + "OthelloField[position=D2, state=empty]], ["
                                        + "OthelloField[position=A3, state=black], "
                                        + "OthelloField[position=B3, state=black], "
                                        + "OthelloField[position=C3, state=black], "
                                        + "OthelloField[position=D3, state=empty]], ["
                                        + "OthelloField[position=A4, state=black], "
                                        + "OthelloField[position=B4, state=empty], "
                                        + "OthelloField[position=C4, state=empty], "
                                        + "OthelloField[position=D4, state=black]]]]")));

        assertThat(this.state.getBlackPlayer().getState(), is(equalTo(PlayerState.WON)));
        assertThat(this.state.getWhitePlayer().getState(), is(equalTo(PlayerState.LOST)));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(), hasSize(4));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.BLACK).keySet(), hasSize(11));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.WHITE).keySet(), hasSize(1));
    }

    /**
     * Tests a complete game on a 4x4 board using moves where white wins.
     */
    @Test
    void testCompleteGameWhiteWins() throws GameException {
        this.place(1, 0, true);
        this.place(0, 0, false);
        this.place(2, 3, true);
        this.place(3, 3, false);
        this.place(0, 1, true);
        this.place(0, 2, false);
        this.place(1, 3, true);
        this.place(0, 3, false);
        this.place(3, 2, true);
        this.place(3, 0, false);
        this.place(2, 0, true);
        this.place(3, 1, false);
        this.skip(true);
        this.skip(false);

        assertThat(
                this.state.getBoard().toString(),
                is(
                        equalTo(
                                "OthelloBoard[size=4, fields=[[" + "OthelloField[position=A1, state=white], "
                                        + "OthelloField[position=B1, state=white], "
                                        + "OthelloField[position=C1, state=white], "
                                        + "OthelloField[position=D1, state=white]], ["
                                        + "OthelloField[position=A2, state=black], "
                                        + "OthelloField[position=B2, state=white], "
                                        + "OthelloField[position=C2, state=white], "
                                        + "OthelloField[position=D2, state=white]], ["
                                        + "OthelloField[position=A3, state=black], "
                                        + "OthelloField[position=B3, state=white], "
                                        + "OthelloField[position=C3, state=white], "
                                        + "OthelloField[position=D3, state=white]], ["
                                        + "OthelloField[position=A4, state=white], "
                                        + "OthelloField[position=B4, state=white], "
                                        + "OthelloField[position=C4, state=white], "
                                        + "OthelloField[position=D4, state=white]]]]")));

        assertThat(this.state.getBlackPlayer().getState(), is(equalTo(PlayerState.LOST)));
        assertThat(this.state.getWhitePlayer().getState(), is(equalTo(PlayerState.WON)));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(), is(empty()));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.BLACK).keySet(), hasSize(2));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.WHITE).keySet(), hasSize(14));
    }

    /**
     * Tests creating a state where the white player starts.
     */
    @Test
    void testCreateStateWithWhitePlayerStarting() throws GameException {
        final OthelloPlayerBuilder blackPlayerBuilder = new OthelloPlayerBuilderImpl()
                .changeName(OthelloStateImplTest.BLACK_PLAYER_NAME).changeUsingBlackTokens(true);
        final OthelloPlayerBuilder whitePlayerBuilder = new OthelloPlayerBuilderImpl()
                .changeName(OthelloStateImplTest.WHITE_PLAYER_NAME).changeUsingBlackTokens(false);
        final OthelloState gameState = new OthelloStateImpl(this.board, blackPlayerBuilder, whitePlayerBuilder, false);
        assertThat(gameState.getCurrentPlayer(), is(sameInstance(gameState.getWhitePlayer())));
    }

    /**
     * Places a token on a field.
     *
     * @param row     The row.
     * @param column  The column.
     * @param isBlack {@code true} if the token is black, else {@code false}.
     */
    private void place(final int row, final int column, final boolean isBlack) throws GameException {
        this.moveFactory.createPlaceTokenMove(isBlack, pos(row, column))
                .applyTo(this.state, isBlack ? this.state.getBlackPlayer() : this.state.getWhitePlayer());
    }

    /**
     * Skips a move.
     *
     * @param isBlack {@code true} if the player using black tokens skips a move, else {@code false}.
     */
    private void skip(final boolean isBlack) throws GameException {
        this.moveFactory.createSkipMove(isBlack)
                .applyTo(this.state, isBlack ? this.state.getBlackPlayer() : this.state.getWhitePlayer());
    }

    /**
     * Returns a position.
     *
     * @param row    The row.
     * @param column The column.
     * @return The position.
     */
    private static OthelloPosition pos(final int row, final int column) {
        return OthelloPosition.of(row, column);
    }
}
