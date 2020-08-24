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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import de.fhdw.gaming.core.domain.DefaultObserverFactoryProvider;
import de.fhdw.gaming.core.domain.GameBuilder;
import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.domain.ObserverFactoryProvider;
import de.fhdw.gaming.othello.core.domain.OthelloGame;
import de.fhdw.gaming.othello.core.domain.OthelloGameBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloPlayerBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.core.domain.OthelloStrategy;
import de.fhdw.gaming.othello.core.moves.impl.AbstractOthelloMove;

/**
 * Implements {@link OthelloGameBuilder}.
 */
final class OthelloGameBuilderImpl implements OthelloGameBuilder {

    /**
     * The {@link ObserverFactoryProvider}.
     */
    private ObserverFactoryProvider observerFactoryProvider;
    /**
     * The builder for the player using black tokens.
     */
    private Optional<OthelloPlayerBuilder> blackPlayer;
    /**
     * The strategy of the player using black tokens.
     */
    private Optional<OthelloStrategy> blackPlayerStrategy;
    /**
     * The builder for the player using white tokens.
     */
    private Optional<OthelloPlayerBuilder> whitePlayer;
    /**
     * The strategy of the player using white tokens.
     */
    private Optional<OthelloStrategy> whitePlayerStrategy;
    /**
     * The maximum computation time per move in seconds.
     */
    private int maxComputationTimePerMove;
    /**
     * The number of rows (and columns) of the board.
     */
    private int boardSize;

    /**
     * Creates an Othello game builder.
     */
    OthelloGameBuilderImpl() {
        this.observerFactoryProvider = new DefaultObserverFactoryProvider();
        this.blackPlayer = Optional.empty();
        this.blackPlayerStrategy = Optional.empty();
        this.whitePlayer = Optional.empty();
        this.whitePlayerStrategy = Optional.empty();
        this.maxComputationTimePerMove = GameBuilder.DEFAULT_MAX_COMPUTATION_TIME_PER_MOVE;
        this.boardSize = OthelloGameBuilder.DEFAULT_BOARD_SIZE;
    }

    @Override
    public OthelloPlayerBuilder createPlayerBuilder() {
        return new OthelloPlayerBuilderImpl();
    }

    @Override
    public OthelloGameBuilder addPlayerBuilder(final OthelloPlayerBuilder playerBuilder, final OthelloStrategy strategy)
            throws GameException {

        Objects.requireNonNull(playerBuilder);
        if (playerBuilder.isUsingBlackTokens() && this.blackPlayer.isEmpty()) {
            this.blackPlayer = Optional.of(playerBuilder);
            this.blackPlayerStrategy = Optional.of(Objects.requireNonNull(strategy, "blackPlayerStrategy"));
        } else if (!playerBuilder.isUsingBlackTokens() && this.whitePlayer.isEmpty()) {
            this.whitePlayer = Optional.of(playerBuilder);
            this.whitePlayerStrategy = Optional.of(Objects.requireNonNull(strategy, "whitePlayerStrategy"));
        } else {
            throw new GameException(
                    String.format(
                            "Adding player %s is not allowed as a player using the same tokens has already been added.",
                            playerBuilder));
        }
        return this;
    }

    @Override
    public OthelloGameBuilder changeMaximumComputationTimePerMove(final int newMaxComputationTimePerMove) {
        this.maxComputationTimePerMove = newMaxComputationTimePerMove;
        return this;
    }

    @Override
    public OthelloGameBuilder changeBoardSize(final int newBoardSize) {
        this.boardSize = newBoardSize;
        return this;
    }

    @Override
    public OthelloGameBuilder changeObserverFactoryProvider(final ObserverFactoryProvider newObserverFactoryProvider) {
        this.observerFactoryProvider = newObserverFactoryProvider;
        return this;
    }

    @Override
    public OthelloGame build(final int id) throws GameException, InterruptedException {
        if (!this.blackPlayer.isPresent() || !this.whitePlayer.isPresent()) {
            throw new GameException("An Othello game needs two players.");
        }

        final OthelloBoardImpl board = new OthelloBoardImpl(this.boardSize);
        final OthelloState initialState = new OthelloStateImpl(
                board,
                this.blackPlayer.get(),
                this.whitePlayer.get(),
                true);

        final Map<String, OthelloStrategy> strategies = new LinkedHashMap<>();
        strategies.put(initialState.getBlackPlayer().getName(), this.blackPlayerStrategy.orElseThrow());
        strategies.put(initialState.getWhitePlayer().getName(), this.whitePlayerStrategy.orElseThrow());
        return new OthelloGameImpl(
                id,
                initialState,
                strategies,
                this.maxComputationTimePerMove,
                AbstractOthelloMove.class::isInstance,
                this.observerFactoryProvider);
    }
}
