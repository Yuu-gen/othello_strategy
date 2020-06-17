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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fhdw.gaming.core.domain.GameBuilderFactory;
import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.domain.PlayerState;
import de.fhdw.gaming.core.ui.InputProviderException;
import de.fhdw.gaming.core.ui.util.ChainedInputProvider;
import de.fhdw.gaming.core.ui.util.NonInteractiveInputProvider;
import de.fhdw.gaming.othello.core.domain.OthelloField;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloGame;
import de.fhdw.gaming.othello.core.domain.OthelloGameBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloGameBuilderFactory;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloPlayerBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.core.domain.OthelloStrategy;
import de.fhdw.gaming.othello.core.domain.factory.OthelloStrategyFactory;
import de.fhdw.gaming.othello.core.domain.factory.OthelloStrategyFactoryProvider;
import de.fhdw.gaming.othello.core.moves.OthelloMove;
import de.fhdw.gaming.othello.core.moves.factory.OthelloMoveFactory;
import de.fhdw.gaming.othello.core.moves.impl.OthelloDefaultMoveFactory;

/**
 * Tests {@link OthelloGameImpl}.
 */
class OthelloGameImplTest {

    /**
     * Name of player using black tokens.
     */
    private static final String BLACK_PLAYER_NAME = "Black";
    /**
     * Name of player using white tokens.
     */
    private static final String WHITE_PLAYER_NAME = "White";

    /**
     * The move factory.
     */
    private OthelloMoveFactory moveFactory;
    /**
     * The {@link OthelloStrategyFactoryProvider}.
     */
    private OthelloStrategyFactoryProvider strategyFactoryProvider;

    /**
     * Sets up the state.
     */
    @BeforeEach
    void setUp() throws GameException {
        this.moveFactory = new OthelloDefaultMoveFactory();
        this.strategyFactoryProvider = new OthelloStrategyFactoryProvider() {

            @Override
            public List<OthelloStrategyFactory> getStrategyFactories() {
                return Collections.singletonList((final OthelloMoveFactory factory) -> new OthelloStrategyStub());
            }
        };
    }

    /**
     * Tests {@link OthelloGameImpl#toString()}.
     */
    @Test
    void testToString() throws GameException, InterruptedException {
        final OthelloGameBuilder gameBuilder = new OthelloGameBuilderImpl().changeBoardSize(4);
        final OthelloPlayerBuilder blackPlayerBuilder = gameBuilder.createPlayerBuilder()
                .changeName(OthelloGameImplTest.BLACK_PLAYER_NAME).changeUsingBlackTokens(true);
        final OthelloPlayerBuilder whitePlayerBuilder = gameBuilder.createPlayerBuilder()
                .changeName(OthelloGameImplTest.WHITE_PLAYER_NAME).changeUsingBlackTokens(false);
        gameBuilder.addPlayerBuilder(blackPlayerBuilder, new BlackPlayerStrategy());
        gameBuilder.addPlayerBuilder(whitePlayerBuilder, new WhitePlayerStrategy());
        try (OthelloGame game = gameBuilder.build(1)) {
            assertThat(
                    game.toString(),
                    is(
                            equalTo(
                                    "OthelloGame[id=1, boardSize=4, state=OthelloState[board=OthelloBoard["
                                            + "size=4, fields=[[" + "OthelloField[position=A1, state=empty], "
                                            + "OthelloField[position=B1, state=empty], "
                                            + "OthelloField[position=C1, state=empty], "
                                            + "OthelloField[position=D1, state=empty]], ["
                                            + "OthelloField[position=A2, state=empty], "
                                            + "OthelloField[position=B2, state=white], "
                                            + "OthelloField[position=C2, state=black], "
                                            + "OthelloField[position=D2, state=empty]], ["
                                            + "OthelloField[position=A3, state=empty], "
                                            + "OthelloField[position=B3, state=black], "
                                            + "OthelloField[position=C3, state=white], "
                                            + "OthelloField[position=D3, state=empty]], ["
                                            + "OthelloField[position=A4, state=empty], "
                                            + "OthelloField[position=B4, state=empty], "
                                            + "OthelloField[position=C4, state=empty], "
                                            + "OthelloField[position=D4, state=empty]]]], "
                                            + "blackPlayer=OthelloPlayer[name=Black, tokens=black, state=PLAYING], "
                                            + "whitePlayer=OthelloPlayer[name=White, tokens=white, state=PLAYING], "
                                            + "currentPlayer=black, " + "numberOfConsecutiveSkips=0], strategies={"
                                            + "Black=BlackPlayerStrategy[movesDone=0], "
                                            + "White=WhitePlayerStrategy[movesDone=0]}]")));
        }
    }

    /**
     * Tests a complete game on a 4x4 board using moves, created by the game builder.
     */
    @Test
    void testCompleteGameCreatedByBuilder() throws GameException, InterruptedException {
        final OthelloGameBuilder gameBuilder = new OthelloGameBuilderImpl().changeBoardSize(4);
        final OthelloPlayerBuilder blackPlayerBuilder = gameBuilder.createPlayerBuilder()
                .changeName(OthelloGameImplTest.BLACK_PLAYER_NAME).changeUsingBlackTokens(true);
        final OthelloPlayerBuilder whitePlayerBuilder = gameBuilder.createPlayerBuilder()
                .changeName(OthelloGameImplTest.WHITE_PLAYER_NAME).changeUsingBlackTokens(false);
        gameBuilder.addPlayerBuilder(blackPlayerBuilder, new BlackPlayerStrategy());
        gameBuilder.addPlayerBuilder(whitePlayerBuilder, new WhitePlayerStrategy());

        try (OthelloGame game = gameBuilder.build(1)) {
            game.start();
            while (!game.isFinished()) {
                game.makeMove();
            }

            final Map<String, OthelloPlayer> players = game.getPlayers();
            final OthelloPlayer blackPlayer = players.get(OthelloGameImplTest.BLACK_PLAYER_NAME);
            final OthelloPlayer whitePlayer = players.get(OthelloGameImplTest.WHITE_PLAYER_NAME);

            assertThat(blackPlayer.getState(), is(equalTo(PlayerState.WON)));
            assertThat(whitePlayer.getState(), is(equalTo(PlayerState.LOST)));
        }
    }

    /**
     * Tests a complete game on a 4x4 board using moves, created by the game factory. Black player is added first.
     */
    @Test
    void testCompleteGameCreatedByFactoryAddingBlackPlayerFirst()
            throws GameException, InterruptedException, InputProviderException {

        final ChainedInputProvider inputProvider = new ChainedInputProvider(
                new NonInteractiveInputProvider().fixedInteger(OthelloGameBuilderFactoryImpl.PARAM_BOARD_SIZE, 4),
                (final Map<String, Object> gameDataSet) -> new ChainedInputProvider(
                        new NonInteractiveInputProvider()
                                .fixedString(
                                        GameBuilderFactory.PARAM_PLAYER_NAME,
                                        OthelloGameImplTest.BLACK_PLAYER_NAME)
                                .fixedBoolean(OthelloGameBuilderFactoryImpl.PARAM_PLAYER_USING_BLACK_TOKENS, true)
                                .fixedObject(GameBuilderFactory.PARAM_PLAYER_STRATEGY, new BlackPlayerStrategy()),
                        (final Map<String, Object> firstPlayerDataSet) -> new NonInteractiveInputProvider()
                                .fixedString(
                                        GameBuilderFactory.PARAM_PLAYER_NAME,
                                        OthelloGameImplTest.WHITE_PLAYER_NAME)
                                .fixedBoolean(OthelloGameBuilderFactoryImpl.PARAM_PLAYER_USING_BLACK_TOKENS, false)
                                .fixedObject(GameBuilderFactory.PARAM_PLAYER_STRATEGY, new WhitePlayerStrategy())));

        final OthelloGameBuilderFactory gameFactory = new OthelloGameBuilderFactoryImpl(this.strategyFactoryProvider);
        try (OthelloGame game = gameFactory.createGameBuilder(inputProvider).build(1)) {
            game.start();
            while (!game.isFinished()) {
                game.makeMove();
            }

            final Map<String, OthelloPlayer> players = game.getPlayers();
            final OthelloPlayer blackPlayer = players.get(OthelloGameImplTest.BLACK_PLAYER_NAME);
            final OthelloPlayer whitePlayer = players.get(OthelloGameImplTest.WHITE_PLAYER_NAME);

            assertThat(blackPlayer.getState(), is(equalTo(PlayerState.WON)));
            assertThat(whitePlayer.getState(), is(equalTo(PlayerState.LOST)));
        }
    }

    /**
     * Tests a complete game on a 4x4 board using moves, created by the game factory. White player is added first.
     */
    @Test
    void testCompleteGameCreatedByFactoryAddingWhitePlayerFirst()
            throws GameException, InterruptedException, InputProviderException {

        final ChainedInputProvider inputProvider = new ChainedInputProvider(
                new NonInteractiveInputProvider().fixedInteger(OthelloGameBuilderFactoryImpl.PARAM_BOARD_SIZE, 4),
                (final Map<String, Object> gameDataSet) -> new ChainedInputProvider(
                        new NonInteractiveInputProvider()
                                .fixedString(
                                        GameBuilderFactory.PARAM_PLAYER_NAME,
                                        OthelloGameImplTest.WHITE_PLAYER_NAME)
                                .fixedBoolean(OthelloGameBuilderFactoryImpl.PARAM_PLAYER_USING_BLACK_TOKENS, false)
                                .fixedObject(GameBuilderFactory.PARAM_PLAYER_STRATEGY, new WhitePlayerStrategy()),
                        (final Map<String, Object> firstPlayerDataSet) -> new NonInteractiveInputProvider()
                                .fixedString(
                                        GameBuilderFactory.PARAM_PLAYER_NAME,
                                        OthelloGameImplTest.BLACK_PLAYER_NAME)
                                .fixedBoolean(OthelloGameBuilderFactoryImpl.PARAM_PLAYER_USING_BLACK_TOKENS, true)
                                .fixedObject(GameBuilderFactory.PARAM_PLAYER_STRATEGY, new BlackPlayerStrategy())));

        final OthelloGameBuilderFactory gameFactory = new OthelloGameBuilderFactoryImpl(this.strategyFactoryProvider);
        try (OthelloGame game = gameFactory.createGameBuilder(inputProvider).build(1)) {
            game.start();
            while (!game.isFinished()) {
                game.makeMove();
            }

            final Map<String, OthelloPlayer> players = game.getPlayers();
            final OthelloPlayer blackPlayer = players.get(OthelloGameImplTest.BLACK_PLAYER_NAME);
            final OthelloPlayer whitePlayer = players.get(OthelloGameImplTest.WHITE_PLAYER_NAME);

            assertThat(blackPlayer.getState(), is(equalTo(PlayerState.WON)));
            assertThat(whitePlayer.getState(), is(equalTo(PlayerState.LOST)));
        }
    }

    /**
     * Tests that a cheating strategy using custom moves is rejected.
     */
    @Test
    void testCheatingStrategyBlack() throws GameException, InterruptedException {
        final OthelloGameBuilder gameBuilder = new OthelloGameBuilderImpl().changeBoardSize(4);
        final OthelloPlayerBuilder blackPlayerBuilder = gameBuilder.createPlayerBuilder()
                .changeName(OthelloGameImplTest.BLACK_PLAYER_NAME).changeUsingBlackTokens(true);
        final OthelloPlayerBuilder whitePlayerBuilder = gameBuilder.createPlayerBuilder()
                .changeName(OthelloGameImplTest.WHITE_PLAYER_NAME).changeUsingBlackTokens(false);
        gameBuilder.addPlayerBuilder(blackPlayerBuilder, new CheatingStrategy());
        gameBuilder.addPlayerBuilder(whitePlayerBuilder, new WhitePlayerStrategy());

        try (OthelloGame game = gameBuilder.build(1)) {
            game.start();
            game.makeMove();

            final Map<String, OthelloPlayer> players = game.getPlayers();
            final OthelloPlayer blackPlayer = players.get(OthelloGameImplTest.BLACK_PLAYER_NAME);
            final OthelloPlayer whitePlayer = players.get(OthelloGameImplTest.WHITE_PLAYER_NAME);

            assertThat(game.isFinished(), is(equalTo(true)));
            assertThat(blackPlayer.getState(), is(equalTo(PlayerState.LOST)));
            assertThat(whitePlayer.getState(), is(equalTo(PlayerState.WON)));
        }
    }

    /**
     * Tests that a cheating strategy using custom moves is rejected.
     */
    @Test
    void testCheatingStrategyWhite() throws GameException, InterruptedException {
        final OthelloGameBuilder gameBuilder = new OthelloGameBuilderImpl().changeBoardSize(4);
        final OthelloPlayerBuilder blackPlayerBuilder = gameBuilder.createPlayerBuilder()
                .changeName(OthelloGameImplTest.BLACK_PLAYER_NAME).changeUsingBlackTokens(true);
        final OthelloPlayerBuilder whitePlayerBuilder = gameBuilder.createPlayerBuilder()
                .changeName(OthelloGameImplTest.WHITE_PLAYER_NAME).changeUsingBlackTokens(false);
        gameBuilder.addPlayerBuilder(blackPlayerBuilder, new BlackPlayerStrategy());
        gameBuilder.addPlayerBuilder(whitePlayerBuilder, new CheatingStrategy());

        try (OthelloGame game = gameBuilder.build(1)) {
            game.start();
            game.makeMove();
            game.makeMove();

            final Map<String, OthelloPlayer> players = game.getPlayers();
            final OthelloPlayer blackPlayer = players.get(OthelloGameImplTest.BLACK_PLAYER_NAME);
            final OthelloPlayer whitePlayer = players.get(OthelloGameImplTest.WHITE_PLAYER_NAME);

            assertThat(game.isFinished(), is(equalTo(true)));
            assertThat(blackPlayer.getState(), is(equalTo(PlayerState.WON)));
            assertThat(whitePlayer.getState(), is(equalTo(PlayerState.LOST)));
        }
    }

    /**
     * Places a token on a field.
     *
     * @param row     The row.
     * @param column  The column.
     * @param isBlack {@code true} if the token is black, else {@code false}.
     */
    private OthelloMove placeMove(final int row, final int column, final boolean isBlack) throws GameException {
        return this.moveFactory.createPlaceTokenMove(isBlack, pos(row, column));
    }

    /**
     * Skips a move.
     *
     * @param isBlack {@code true} if the player using black tokens skips a move, else {@code false}.
     */
    private OthelloMove skipMove(final boolean isBlack) throws GameException {
        return this.moveFactory.createSkipMove(isBlack);
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

    /**
     * The moves of the player using black tokens.
     */
    private final class BlackPlayerStrategy implements OthelloStrategy {

        /**
         * The number of moves already done.
         */
        private int movesDone;

        @Override
        public String toString() {
            return String.format("BlackPlayerStrategy[movesDone=%d]", this.movesDone);
        }

        @Override
        public Optional<OthelloMove> computeNextMove(final int gameId, final OthelloPlayer player,
                final OthelloState state) throws GameException {

            try {
                switch (this.movesDone) {
                case 0:
                    return Optional.of(OthelloGameImplTest.this.placeMove(0, 1, true));
                case 1:
                    return Optional.of(OthelloGameImplTest.this.placeMove(0, 3, true));
                case 2:
                    return Optional.of(OthelloGameImplTest.this.placeMove(3, 0, true));
                case 3:
                    return Optional.of(OthelloGameImplTest.this.placeMove(1, 0, true));
                case 4:
                    return Optional.of(OthelloGameImplTest.this.placeMove(3, 3, true));
                case 5:
                    return Optional.of(OthelloGameImplTest.this.skipMove(true));
                default:
                    return Optional.empty();
                }
            } finally {
                ++this.movesDone;
            }
        }
    }

    /**
     * The moves of the player using white tokens.
     */
    private final class WhitePlayerStrategy implements OthelloStrategy {

        /**
         * The number of moves already done.
         */
        private int movesDone;

        @Override
        public String toString() {
            return String.format("WhitePlayerStrategy[movesDone=%d]", this.movesDone);
        }

        @Override
        public Optional<OthelloMove> computeNextMove(final int gameId, final OthelloPlayer player,
                final OthelloState state) throws GameException {

            try {
                switch (this.movesDone) {
                case 0:
                    return Optional.of(OthelloGameImplTest.this.placeMove(0, 2, false));
                case 1:
                    return Optional.of(OthelloGameImplTest.this.placeMove(2, 0, false));
                case 2:
                    return Optional.of(OthelloGameImplTest.this.placeMove(0, 0, false));
                case 3:
                    return Optional.of(OthelloGameImplTest.this.skipMove(false));
                case 4:
                    return Optional.of(OthelloGameImplTest.this.skipMove(false));
                default:
                    return Optional.empty();
                }
            } finally {
                ++this.movesDone;
            }
        }
    }

    /**
     * A cheating strategy trying to place as many tokens on the board in one move as possible.
     */
    private final class CheatingStrategy implements OthelloStrategy {

        @Override
        public Optional<OthelloMove> computeNextMove(final int gameId, final OthelloPlayer player,
                final OthelloState state) throws GameException {

            return Optional.of(new OthelloMove() {

                @Override
                public void applyTo(final OthelloState state, final OthelloPlayer player) throws GameException {
                    final boolean usingBlackTokens = player.isUsingBlackTokens();
                    boolean tryToPlaceNextToken = true;
                    while (tryToPlaceNextToken) {
                        tryToPlaceNextToken = false;
                        for (final OthelloField field : state.getBoard().getFieldsBeing(OthelloFieldState.EMPTY)
                                .values()) {

                            if (field.isActive(usingBlackTokens)) {
                                field.placeToken(usingBlackTokens);
                                tryToPlaceNextToken = true;
                                break;
                            }
                        }
                    }
                }
            });
        }
    }
}
