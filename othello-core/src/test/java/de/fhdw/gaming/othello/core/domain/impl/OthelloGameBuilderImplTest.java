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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloGame;
import de.fhdw.gaming.othello.core.domain.OthelloGameBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloPlayerBuilder;

/**
 * Tests {@link class OthelloGameBuilderImpl}.
 */
class OthelloGameBuilderImplTest {

    /**
     * Tries to create an Othello game with as much defaults as possible.
     */
    @Test
    void testCreateGameWithDefaults() throws GameException, InterruptedException {
        final OthelloGameBuilder builder = new OthelloGameBuilderImpl();
        final OthelloPlayerBuilder playerBuilderA = builder.createPlayerBuilder().changeName("A");
        final OthelloPlayerBuilder playerBuilderB = builder.createPlayerBuilder().changeName("B")
                .changeUsingBlackTokens(false);
        try (OthelloGame game = builder.addPlayerBuilder(playerBuilderA, new OthelloStrategyStub())
                .addPlayerBuilder(playerBuilderB, new OthelloStrategyStub()).build(1)) {

            assertThat(game.getBoardSize(), is(equalTo(8)));
            assertThat(game.getPlayers().keySet(), containsInAnyOrder("A", "B"));
            assertThat(game.getPlayers().get("A").isUsingBlackTokens(), is(equalTo(true)));
            assertThat(game.getPlayers().get("B").isUsingBlackTokens(), is(equalTo(false)));
        }
    }

    /**
     * Tries to create an Othello game with as much defaults as possible.
     */
    @Test
    void testCreateGameWithCustomizations() throws GameException, InterruptedException {
        final OthelloGameBuilder builder = new OthelloGameBuilderImpl();
        final OthelloPlayerBuilder playerBuilderA = builder.createPlayerBuilder().changeName("A")
                .changeUsingBlackTokens(false);
        final OthelloPlayerBuilder playerBuilderB = builder.createPlayerBuilder().changeName("B")
                .changeUsingBlackTokens(true);
        try (OthelloGame game = builder.changeBoardSize(6).addPlayerBuilder(playerBuilderA, new OthelloStrategyStub())
                .addPlayerBuilder(playerBuilderB, new OthelloStrategyStub()).build(1)) {

            assertThat(game.getBoardSize(), is(equalTo(6)));
            assertThat(game.getPlayers().keySet(), containsInAnyOrder("A", "B"));
            assertThat(game.getPlayers().get("A").isUsingBlackTokens(), is(equalTo(false)));
            assertThat(game.getPlayers().get("B").isUsingBlackTokens(), is(equalTo(true)));
        }
    }

    /**
     * Tries to create an Othello game with too few players.
     */
    @Test
    void testCreateGameWithTooFewPlayers() throws GameException {
        final OthelloGameBuilder builder = new OthelloGameBuilderImpl();
        assertThrows(GameException.class, () -> builder.build(1));
    }

    /**
     * Tries to create an Othello game with two black players.
     */
    @Test
    void testCreateGameWithTwoBlackPlayers() throws GameException {
        final OthelloGameBuilder builder = new OthelloGameBuilderImpl();
        final OthelloPlayerBuilder playerBuilderA = builder.createPlayerBuilder().changeName("A");
        final OthelloPlayerBuilder playerBuilderB = builder.createPlayerBuilder().changeName("B");
        builder.addPlayerBuilder(playerBuilderA, new OthelloStrategyStub());
        assertThrows(GameException.class, () -> builder.addPlayerBuilder(playerBuilderB, new OthelloStrategyStub()));
    }

    /**
     * Tries to create an Othello game with two white players.
     */
    @Test
    void testCreateGameWithTwoWhitePlayers() throws GameException {
        final OthelloGameBuilder builder = new OthelloGameBuilderImpl();
        final OthelloPlayerBuilder playerBuilderA = builder.createPlayerBuilder().changeName("A")
                .changeUsingBlackTokens(false);
        final OthelloPlayerBuilder playerBuilderB = builder.createPlayerBuilder().changeName("B")
                .changeUsingBlackTokens(false);
        builder.addPlayerBuilder(playerBuilderA, new OthelloStrategyStub());
        assertThrows(GameException.class, () -> builder.addPlayerBuilder(playerBuilderB, new OthelloStrategyStub()));
    }
}
