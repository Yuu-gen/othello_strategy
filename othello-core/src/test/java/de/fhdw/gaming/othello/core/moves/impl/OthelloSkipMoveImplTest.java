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
package de.fhdw.gaming.othello.core.moves.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.core.domain.impl.OthelloBoardBuilder;
import de.fhdw.gaming.othello.core.domain.impl.OthelloStateStub;

/**
 * Tests {@link OthelloSkipMove}.
 */
class OthelloSkipMoveImplTest {

    /**
     * Tests {@link OthelloSkipMove#isPlacingBlackToken()}.
     */
    @Test
    void testIsPlacingBlackToken() {
        final OthelloSkipMove moveBlack = new OthelloSkipMove(true);
        assertThat(moveBlack.isPlacingBlackToken(), is(equalTo(true)));
        final OthelloSkipMove moveWhite = new OthelloSkipMove(false);
        assertThat(moveWhite.isPlacingBlackToken(), is(equalTo(false)));
    }

    /**
     * Tests {@link OthelloSkipMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)}, black
     * move.
     */
    @Test
    void testApplyToBlackMove() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).changeFieldState(0, 1, OthelloFieldState.WHITE)
                .changeFieldState(1, 0, OthelloFieldState.WHITE).changeFieldState(2, 3, OthelloFieldState.WHITE)
                .changeFieldState(3, 2, OthelloFieldState.WHITE).build();

        final OthelloState state = new OthelloStateStub(board);

        final OthelloState copy = state.deepCopy();
        final OthelloSkipMove move = new OthelloSkipMove(true);
        move.applyTo(copy, copy.getBlackPlayer());
        assertThat(copy.getBoard(), is(equalTo(state.getBoard())));
    }

    /**
     * Tests {@link OthelloSkipMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)}, white
     * move.
     */
    @Test
    void testApplyToWhiteMove() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).changeFieldState(0, 2, OthelloFieldState.BLACK)
                .changeFieldState(1, 3, OthelloFieldState.BLACK).changeFieldState(2, 0, OthelloFieldState.BLACK)
                .changeFieldState(3, 1, OthelloFieldState.BLACK).build();

        final OthelloState state = new OthelloStateStub(board);

        final OthelloState copy = state.deepCopy();
        final OthelloSkipMove move = new OthelloSkipMove(false);
        move.applyTo(copy, copy.getWhitePlayer());
        assertThat(copy.getBoard(), is(equalTo(state.getBoard())));
    }

    /**
     * Tests {@link OthelloSkipMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)} when
     * skipping is not allowed as placing a token is possible.
     */
    @Test
    void testApplyToWrongSkipMoves() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).build();
        final OthelloState state = new OthelloStateStub(board);

        final OthelloState copy = state.deepCopy();
        assertThrows(GameException.class, () -> new OthelloSkipMove(true).applyTo(copy, copy.getBlackPlayer()));
        assertThrows(GameException.class, () -> new OthelloSkipMove(false).applyTo(copy, copy.getWhitePlayer()));
        assertThat(copy.getBoard(), is(equalTo(state.getBoard())));
    }

    /**
     * Tests {@link OthelloPlaceTokenMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)},
     * wrong moves: black token for white player and white token for black player.
     */
    @Test
    void testSkipPlacingBlackTokenForWhitePlayer() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).changeFieldState(0, 1, OthelloFieldState.WHITE)
                .changeFieldState(1, 0, OthelloFieldState.WHITE).changeFieldState(2, 3, OthelloFieldState.WHITE)
                .changeFieldState(3, 2, OthelloFieldState.WHITE).build();

        final OthelloState state = new OthelloStateStub(board);

        final OthelloState copy = state.deepCopy();
        assertThrows(GameException.class, () -> new OthelloSkipMove(true).applyTo(copy, copy.getWhitePlayer()));
        assertThat(copy.getBoard(), is(equalTo(state.getBoard())));
    }

    /**
     * Tests {@link OthelloPlaceTokenMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)},
     * wrong moves: black token for white player and white token for black player.
     */
    @Test
    void testSkipPlacingWhiteTokenForBlackPlayer() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).changeFieldState(0, 2, OthelloFieldState.BLACK)
                .changeFieldState(1, 3, OthelloFieldState.BLACK).changeFieldState(2, 0, OthelloFieldState.BLACK)
                .changeFieldState(3, 1, OthelloFieldState.BLACK).build();

        final OthelloState state = new OthelloStateStub(board);

        final OthelloState copy = state.deepCopy();
        assertThrows(GameException.class, () -> new OthelloSkipMove(false).applyTo(copy, copy.getBlackPlayer()));
        assertThat(copy.getBoard(), is(equalTo(state.getBoard())));
    }
}
