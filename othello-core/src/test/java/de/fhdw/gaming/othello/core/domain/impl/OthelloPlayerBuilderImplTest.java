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

import org.junit.jupiter.api.Test;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloPlayerBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloState;

/**
 * Tests {@link class OthelloPlayerBuilderImpl}.
 */
class OthelloPlayerBuilderImplTest {

    /**
     * Tries to create an Othello player with as much defaults as possible.
     */
    @Test
    void testCreatePlayerWithDefaults() throws GameException {
        final OthelloBoard board = new OthelloBoardStub();
        final OthelloState state = new OthelloStateStub(board);
        final OthelloPlayerBuilder builder = new OthelloPlayerBuilderImpl();
        final OthelloPlayer player = builder.changeName("A").build(state);
        assertThat(player.getName(), is(equalTo("A")));
        assertThat(player.isUsingBlackTokens(), is(equalTo(true)));
    }

    /**
     * Tries to create an Othello player which uses black tokens.
     */
    @Test
    void testCreateBlackPlayer() throws GameException {
        final OthelloBoard board = new OthelloBoardStub();
        final OthelloState state = new OthelloStateStub(board);
        final OthelloPlayerBuilder builder = new OthelloPlayerBuilderImpl();
        final OthelloPlayer player = builder.changeName("B").changeUsingBlackTokens(true).build(state);
        assertThat(player.getName(), is(equalTo("B")));
        assertThat(player.isUsingBlackTokens(), is(equalTo(true)));
    }

    /**
     * Tries to create an Othello player which uses black tokens.
     */
    @Test
    void testCreateWhitePlayer() throws GameException {
        final OthelloBoard board = new OthelloBoardStub();
        final OthelloState state = new OthelloStateStub(board);
        final OthelloPlayerBuilder builder = new OthelloPlayerBuilderImpl();
        final OthelloPlayer player = builder.changeName("C").changeUsingBlackTokens(false).build(state);
        assertThat(player.getName(), is(equalTo("C")));
        assertThat(player.isUsingBlackTokens(), is(equalTo(false)));
    }
}
