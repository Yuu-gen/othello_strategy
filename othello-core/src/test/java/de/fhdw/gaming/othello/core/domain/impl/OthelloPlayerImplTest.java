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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fhdw.gaming.core.domain.PlayerState;
import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloState;

/**
 * Tests {@link OthelloPlayerImpl}.
 */
class OthelloPlayerImplTest {

    /**
     * Name of player using black tokens.
     */
    private static final String BLACK = "Black";
    /**
     * Name of player using white tokens.
     */
    private static final String WHITE = "White";

    /**
     * The game state.
     */
    private OthelloState state;
    /**
     * The player using black tokens.
     */
    private OthelloPlayer blackPlayer;
    /**
     * The player using white tokens.
     */
    private OthelloPlayer whitePlayer;

    /**
     * Sets up the players.
     */
    @BeforeEach
    void setUp() {
        final OthelloBoard board = new OthelloBoardStub();
        this.state = new OthelloStateStub(board);
        this.blackPlayer = this.state.getBlackPlayer();
        this.whitePlayer = this.state.getWhitePlayer();
    }

    /**
     * Tests {@link OthelloPlayerImpl#getName()}.
     */
    @Test
    void testGetName() {
        assertThat(this.blackPlayer.getName(), is(equalTo(OthelloPlayerImplTest.BLACK)));
        assertThat(this.whitePlayer.getName(), is(equalTo(OthelloPlayerImplTest.WHITE)));
    }

    /**
     * Tests {@link OthelloPlayerImpl#isUsingBlackTokens()}.
     */
    @Test
    void testIsUsingBlackTokens() {
        assertThat(this.blackPlayer.isUsingBlackTokens(), is(equalTo(true)));
        assertThat(this.whitePlayer.isUsingBlackTokens(), is(equalTo(false)));
    }

    /**
     * Tests {@link OthelloPlayerImpl#getState()}.
     */
    @Test
    void testGetState() {
        assertThat(this.blackPlayer.getState(), is(equalTo(PlayerState.PLAYING)));
        this.state.setPlayerState(this.blackPlayer.getName(), PlayerState.WON);
        assertThat(this.blackPlayer.getState(), is(equalTo(PlayerState.WON)));
        this.state.setPlayerState(this.blackPlayer.getName(), PlayerState.LOST);
        assertThat(this.blackPlayer.getState(), is(equalTo(PlayerState.LOST)));

        assertThat(this.whitePlayer.getState(), is(equalTo(PlayerState.PLAYING)));
        this.state.setPlayerState(this.whitePlayer.getName(), PlayerState.RESIGNED);
        assertThat(this.whitePlayer.getState(), is(equalTo(PlayerState.RESIGNED)));
        this.state.setPlayerState(this.whitePlayer.getName(), PlayerState.DRAW);
        assertThat(this.whitePlayer.getState(), is(equalTo(PlayerState.DRAW)));
    }

    /**
     * Tests {@link OthelloPlayerImpl#toString()}.
     */
    @Test
    void testToString() {
        assertThat(this.blackPlayer.toString(), is(equalTo("OthelloPlayer[name=Black, tokens=black, state=PLAYING]")));
        assertThat(this.whitePlayer.toString(), is(equalTo("OthelloPlayer[name=White, tokens=white, state=PLAYING]")));
    }

    /**
     * Tests {@link OthelloPlayerImpl#equals(Object)}.
     */
    @Test
    void testEqualsObject() {
        assertThat(this.blackPlayer, is(equalTo(this.blackPlayer)));
        assertThat(this.blackPlayer, is(equalTo(new OthelloStateStub(this.state.getBoard()).getBlackPlayer())));
        assertThat(this.blackPlayer, is(not(equalTo(this.whitePlayer))));
        assertThat(this.blackPlayer, is(not(equalTo(OthelloPlayerImplTest.BLACK))));

        final OthelloBoard otherBoard = new OthelloBoardStub();
        final OthelloState otherState = new OthelloStateStub(otherBoard);
        assertThat(this.blackPlayer, is(equalTo(otherState.getBlackPlayer())));
    }

    /**
     * Tests {@link OthelloPlayerImpl#hashCode()}.
     */
    @Test
    void testHashCode() {
        assertThat(
                this.blackPlayer.hashCode(),
                is(equalTo(new OthelloPlayerImpl(this.state, OthelloPlayerImplTest.BLACK, true).hashCode())));
    }
}
