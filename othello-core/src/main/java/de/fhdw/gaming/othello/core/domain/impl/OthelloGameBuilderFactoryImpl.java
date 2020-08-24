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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import de.fhdw.gaming.core.domain.GameBuilder;
import de.fhdw.gaming.core.domain.GameBuilderFactory;
import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.domain.Strategy;
import de.fhdw.gaming.core.ui.InputProvider;
import de.fhdw.gaming.core.ui.InputProviderException;
import de.fhdw.gaming.core.ui.type.validator.MaxValueValidator;
import de.fhdw.gaming.core.ui.type.validator.MinValueValidator;
import de.fhdw.gaming.core.ui.type.validator.PatternValidator;
import de.fhdw.gaming.core.ui.type.validator.Validator;
import de.fhdw.gaming.othello.core.domain.OthelloGameBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloGameBuilderFactory;
import de.fhdw.gaming.othello.core.domain.OthelloPlayerBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloStrategy;
import de.fhdw.gaming.othello.core.domain.factory.OthelloDefaultStrategyFactoryProvider;
import de.fhdw.gaming.othello.core.domain.factory.OthelloStrategyFactory;
import de.fhdw.gaming.othello.core.domain.factory.OthelloStrategyFactoryProvider;
import de.fhdw.gaming.othello.core.moves.factory.OthelloMoveFactory;
import de.fhdw.gaming.othello.core.moves.impl.OthelloDefaultMoveFactory;

/**
 * Implements {@link GameBuilderFactory} by creating an Othello game builder.
 */
public final class OthelloGameBuilderFactoryImpl implements OthelloGameBuilderFactory {

    /**
     * Parameter for the number of rows (and columns) of the board.
     */
    static final String PARAM_BOARD_SIZE = "boardSize";
    /**
     * Parameter for the maximum computation time per move in seconds.
     */
    static final String PARAM_MAX_COMPUTATION_TIME_PER_MOVE = "maxComputationTimePerMove";
    /**
     * Parameter that determines if a player is using black or white tokens.
     */
    static final String PARAM_PLAYER_USING_BLACK_TOKENS = "playerUsingBlackTokens";

    /**
     * The number of players.
     */
    private static final int NUMBER_OF_PLAYERS = 2;
    /**
     * Minimum number of rows (and columns) of the board.
     */
    private static final int MIN_BOARD_SIZE = 4;
    /**
     * Maximum number of rows (and columns) of the board.
     */
    private static final int MAX_BOARD_SIZE = 16;
    /**
     * Smallest allowed maximum computation time per move in seconds.
     */
    private static final int MIN_MAX_COMPUTATION_TIME_PER_MOVE = 1;
    /**
     * Largest allowed maximum computation time per move in seconds.
     */
    private static final int MAX_MAX_COMPUTATION_TIME_PER_MOVE = 3600;

    /**
     * All available Othello strategies.
     */
    private final Set<OthelloStrategy> strategies;

    /**
     * Creates an Othello game factory. Othello strategies are loaded by using the {@link java.util.ServiceLoader}.
     * <p>
     * This constructor is meant to be used by the {@link java.util.ServiceLoader}.
     */
    public OthelloGameBuilderFactoryImpl() {
        this(new OthelloDefaultStrategyFactoryProvider());
    }

    /**
     * Creates an Othello game factory.
     *
     * @param strategyFactoryProvider The {@link OthelloStrategyFactoryProvider} for loading Othello strategies.
     */
    OthelloGameBuilderFactoryImpl(final OthelloStrategyFactoryProvider strategyFactoryProvider) {
        final OthelloMoveFactory moveFactory = new OthelloDefaultMoveFactory();

        final List<OthelloStrategyFactory> factories = strategyFactoryProvider.getStrategyFactories();
        this.strategies = new LinkedHashSet<>();
        for (final OthelloStrategyFactory factory : factories) {
            this.strategies.add(factory.create(moveFactory));
        }
    }

    @Override
    public String getName() {
        return "Othello";
    }

    @Override
    public int getMinimumNumberOfPlayers() {
        return OthelloGameBuilderFactoryImpl.NUMBER_OF_PLAYERS;
    }

    @Override
    public int getMaximumNumberOfPlayers() {
        return OthelloGameBuilderFactoryImpl.NUMBER_OF_PLAYERS;
    }

    @Override
    public List<? extends Strategy<?, ?, ?>> getStrategies() {
        return new ArrayList<>(this.strategies);
    }

    @Override
    public OthelloGameBuilder createGameBuilder(final InputProvider inputProvider) throws GameException {
        try {
            final OthelloGameBuilder gameBuilder = new OthelloGameBuilderImpl();

            @SuppressWarnings("unchecked")
            final Map<String,
                    Object> gameData = inputProvider.needInteger(
                            OthelloGameBuilderFactoryImpl.PARAM_BOARD_SIZE,
                            "Number of rows (and columns)",
                            Optional.of(OthelloGameBuilder.DEFAULT_BOARD_SIZE),
                            new MinValueValidator<>(OthelloGameBuilderFactoryImpl.MIN_BOARD_SIZE),
                            new MaxValueValidator<>(OthelloGameBuilderFactoryImpl.MAX_BOARD_SIZE),
                            new Validator<Integer>() {

                                @Override
                                public Integer validate(final Integer value) throws InputProviderException {
                                    if (value.intValue() % 2 != 0) {
                                        throw new InputProviderException(this.getInfo());
                                    } else {
                                        return value;
                                    }
                                }

                                @Override
                                public String getInfo() {
                                    return "The value must be an even number.";
                                }
                            })
                            .needInteger(
                                    OthelloGameBuilderFactoryImpl.PARAM_MAX_COMPUTATION_TIME_PER_MOVE,
                                    "Maximum computation time per move in seconds",
                                    Optional.of(GameBuilder.DEFAULT_MAX_COMPUTATION_TIME_PER_MOVE),
                                    new MinValueValidator<>(
                                            OthelloGameBuilderFactoryImpl.MIN_MAX_COMPUTATION_TIME_PER_MOVE),
                                    new MaxValueValidator<>(
                                            OthelloGameBuilderFactoryImpl.MAX_MAX_COMPUTATION_TIME_PER_MOVE))
                            .requestData("Board properties");

            gameBuilder.changeBoardSize((Integer) gameData.get(OthelloGameBuilderFactoryImpl.PARAM_BOARD_SIZE));
            gameBuilder.changeMaximumComputationTimePerMove(
                    (Integer) gameData.get(OthelloGameBuilderFactoryImpl.PARAM_MAX_COMPUTATION_TIME_PER_MOVE));

            final InputProvider firstPlayerInputProvider = inputProvider.getNext(gameData);
            final Map<String, Object> firstPlayerData = this
                    .requestPlayerData(firstPlayerInputProvider, "Player 1", Optional.empty());
            final OthelloPlayerBuilder firstPlayerBuilder = this
                    .initPlayerBuilder(gameBuilder.createPlayerBuilder(), firstPlayerData);
            final OthelloStrategy firstPlayerStrategy = this.getStrategy(firstPlayerData);
            gameBuilder.addPlayerBuilder(firstPlayerBuilder, firstPlayerStrategy);

            final InputProvider secondPlayerInputProvider = firstPlayerInputProvider.getNext(firstPlayerData);
            final Map<String,
                    Object> secondPlayerData = this.requestPlayerData(
                            secondPlayerInputProvider,
                            "Player 2",
                            Optional.of(
                                    !(Boolean) firstPlayerData
                                            .get(OthelloGameBuilderFactoryImpl.PARAM_PLAYER_USING_BLACK_TOKENS)));
            final OthelloPlayerBuilder secondPlayerBuilder = this
                    .initPlayerBuilder(gameBuilder.createPlayerBuilder(), secondPlayerData);
            final OthelloStrategy secondPlayerStrategy = this.getStrategy(secondPlayerData);
            gameBuilder.addPlayerBuilder(secondPlayerBuilder, secondPlayerStrategy);

            return gameBuilder;
        } catch (final InputProviderException e) {
            throw new GameException(String.format("Creating Othello game was aborted: %s", e.getMessage()), e);
        }
    }

    /**
     * Initialises an Othello player builder.
     *
     * @param inputProvider    The input provider.
     * @param title            The title for the UI.
     * @param usingBlackTokens If set, determines the mandatory colour of the player. Otherwise, the colour is initially
     *                         set to black, and the user can change it at will.
     * @return {@code playerBuilder}.
     * @throws InputProviderException if the operation has been aborted prematurely (e.g. if the user cancelled a
     *                                dialog).
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> requestPlayerData(final InputProvider inputProvider, final String title,
            final Optional<Boolean> usingBlackTokens) throws GameException, InputProviderException {

        inputProvider
                .needString(
                        GameBuilderFactory.PARAM_PLAYER_NAME,
                        "Name",
                        Optional.empty(),
                        new PatternValidator(Pattern.compile("\\S+(\\s+\\S+)*")))
                .needBoolean(
                        OthelloGameBuilderFactoryImpl.PARAM_PLAYER_USING_BLACK_TOKENS,
                        "Black colour",
                        Optional.of(Boolean.TRUE))
                .needObject(GameBuilderFactory.PARAM_PLAYER_STRATEGY, "Strategy", Optional.empty(), this.strategies);

        if (usingBlackTokens.isPresent()) {
            inputProvider.fixedBoolean(
                    OthelloGameBuilderFactoryImpl.PARAM_PLAYER_USING_BLACK_TOKENS,
                    usingBlackTokens.get());
        }

        return inputProvider.requestData(title);
    }

    /**
     * Initialises an Othello player builder.
     *
     * @param playerBuilder The player builder.
     * @param playerData    The requested player data.
     * @return {@code playerBuilder}.
     * @throws InputProviderException if the operation has been aborted prematurely (e.g. if the user cancelled a
     *                                dialog).
     */
    private OthelloPlayerBuilder initPlayerBuilder(final OthelloPlayerBuilder playerBuilder,
            final Map<String, Object> playerData) throws GameException, InputProviderException {

        return playerBuilder.changeName((String) playerData.get(GameBuilderFactory.PARAM_PLAYER_NAME))
                .changeUsingBlackTokens(
                        (Boolean) playerData.get(OthelloGameBuilderFactoryImpl.PARAM_PLAYER_USING_BLACK_TOKENS));
    }

    /**
     * Returns an Othello strategy.
     *
     * @param playerData The requested player data.
     * @return The Othello strategy.
     */
    private OthelloStrategy getStrategy(final Map<String, Object> playerData) {
        return (OthelloStrategy) playerData.get(GameBuilderFactory.PARAM_PLAYER_STRATEGY);
    }
}
