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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.core.domain.impl.OthelloBoardBuilder;
import de.fhdw.gaming.othello.core.domain.impl.OthelloStateStub;

/**
 * Tests {@link OthelloPlaceTokenMove}.
 */
class OthelloPlaceTokenMoveImplTest {

    /**
     * Tests {@link OthelloPlaceTokenMove#isPlacingBlackToken()}.
     */
    @Test
    void testIsPlacingBlackToken() {
        final OthelloPosition pos = pos(2, 3);
        final OthelloPlaceTokenMove moveBlack = new OthelloPlaceTokenMove(true, pos);
        assertThat(moveBlack.isPlacingBlackToken(), is(equalTo(true)));
        final OthelloPlaceTokenMove moveWhite = new OthelloPlaceTokenMove(false, pos);
        assertThat(moveWhite.isPlacingBlackToken(), is(equalTo(false)));
    }

    /**
     * Tests {@link OthelloPlaceTokenMove#getTokenPosition()}.
     */
    @Test
    void testGetTokenPosition() {
        final OthelloPosition posBlack = pos(2, 3);
        final OthelloPlaceTokenMove moveBlack = new OthelloPlaceTokenMove(true, posBlack);
        assertThat(moveBlack.getTokenPosition(), is(equalTo(posBlack)));

        final OthelloPosition posWhite = pos(1, 0);
        final OthelloPlaceTokenMove moveWhite = new OthelloPlaceTokenMove(false, posWhite);
        assertThat(moveWhite.getTokenPosition(), is(equalTo(posWhite)));
    }

    /**
     * Tests {@link OthelloPlaceTokenMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)},
     * black token, first move.
     */
    @Test
    void testPlaceBlackToken1() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).build();
        final OthelloState state = new OthelloStateStub(board);

        final OthelloPlaceTokenMove move = new OthelloPlaceTokenMove(true, pos(0, 1));
        move.applyTo(state, state.getBlackPlayer());

        assertThat(
                board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 2),
                        pos(0, 3),
                        pos(1, 0),
                        pos(1, 3),
                        pos(2, 0),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(
                board.getFieldsBeing(OthelloFieldState.BLACK).keySet(),
                containsInAnyOrder(pos(0, 1), pos(1, 1), pos(1, 2), pos(2, 1)));
        assertThat(board.getFieldsBeing(OthelloFieldState.WHITE).keySet(), containsInAnyOrder(pos(2, 2)));
    }

    /**
     * Tests {@link OthelloPlaceTokenMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)},
     * black token, second move.
     */
    @Test
    void testPlaceBlackToken2() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).build();
        final OthelloState state = new OthelloStateStub(board);

        final OthelloPlaceTokenMove move = new OthelloPlaceTokenMove(true, pos(1, 0));
        move.applyTo(state, state.getBlackPlayer());

        assertThat(
                board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 1),
                        pos(0, 2),
                        pos(0, 3),
                        pos(1, 3),
                        pos(2, 0),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(
                board.getFieldsBeing(OthelloFieldState.BLACK).keySet(),
                containsInAnyOrder(pos(1, 0), pos(1, 1), pos(1, 2), pos(2, 1)));
        assertThat(board.getFieldsBeing(OthelloFieldState.WHITE).keySet(), containsInAnyOrder(pos(2, 2)));
    }

    /**
     * Tests {@link OthelloPlaceTokenMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)},
     * black token, wrong move.
     */
    @Test
    void testPlaceBlackTokenWrongMove() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).build();
        final OthelloState state = new OthelloStateStub(board);

        final OthelloState copy = state.deepCopy();
        final OthelloPlaceTokenMove move = new OthelloPlaceTokenMove(true, pos(1, 1));
        assertThrows(GameException.class, () -> move.applyTo(copy, copy.getBlackPlayer()));
        assertThat(copy.getBoard(), is(equalTo(state.getBoard())));
    }

    /**
     * Tests {@link OthelloPlaceTokenMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)},
     * white token, first move.
     */
    @Test
    void testPlaceWhiteToken1() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).build();
        final OthelloState state = new OthelloStateStub(board);

        final OthelloPlaceTokenMove move = new OthelloPlaceTokenMove(false, pos(0, 2));
        move.applyTo(state, state.getWhitePlayer());

        assertThat(
                board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 1),
                        pos(0, 3),
                        pos(1, 0),
                        pos(1, 3),
                        pos(2, 0),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(board.getFieldsBeing(OthelloFieldState.BLACK).keySet(), containsInAnyOrder(pos(2, 1)));
        assertThat(
                board.getFieldsBeing(OthelloFieldState.WHITE).keySet(),
                containsInAnyOrder(pos(0, 2), pos(1, 1), pos(1, 2), pos(2, 2)));
    }

    /**
     * Tests {@link OthelloPlaceTokenMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)},
     * white token, second move.
     */
    @Test
    void testPlaceWhiteToken2() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).build();
        final OthelloState state = new OthelloStateStub(board);

        final OthelloPlaceTokenMove move = new OthelloPlaceTokenMove(false, pos(1, 3));
        move.applyTo(state, state.getWhitePlayer());

        assertThat(
                board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 1),
                        pos(0, 2),
                        pos(0, 3),
                        pos(1, 0),
                        pos(2, 0),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(board.getFieldsBeing(OthelloFieldState.BLACK).keySet(), containsInAnyOrder(pos(2, 1)));
        assertThat(
                board.getFieldsBeing(OthelloFieldState.WHITE).keySet(),
                containsInAnyOrder(pos(1, 1), pos(1, 2), pos(1, 3), pos(2, 2)));
    }

    /**
     * Tests {@link OthelloPlaceTokenMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)},
     * white token, wrong move.
     */
    @Test
    void testPlaceWhiteTokenWrongMove() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).build();
        final OthelloState state = new OthelloStateStub(board);

        final OthelloState copy = state.deepCopy();
        final OthelloPlaceTokenMove move = new OthelloPlaceTokenMove(false, pos(1, 1));
        assertThrows(GameException.class, () -> move.applyTo(copy, copy.getBlackPlayer()));
        assertThat(copy.getBoard(), is(equalTo(state.getBoard())));
    }

    /**
     * Tests {@link OthelloPlaceTokenMove#applyTo(OthelloState, de.fhdw.gaming.othello.core.domain.OthelloPlayer)},
     * wrong moves: black token for white player and white token for black player.
     */
    @Test
    void testPlaceBlackTokenForWhitePlayer() throws GameException {
        final OthelloBoard board = new OthelloBoardBuilder(4).build();
        final OthelloState state = new OthelloStateStub(board);

        final OthelloState copy = state.deepCopy();
        assertThrows(
                GameException.class,
                () -> new OthelloPlaceTokenMove(true, pos(0, 1)).applyTo(copy, copy.getWhitePlayer()));
        assertThrows(
                GameException.class,
                () -> new OthelloPlaceTokenMove(false, pos(0, 2)).applyTo(copy, copy.getBlackPlayer()));
        assertThat(copy.getBoard(), is(equalTo(state.getBoard())));
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
