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
package de.fhdw.gaming.othello.core.domain;

import de.fhdw.gaming.core.domain.GameBuilder;
import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.domain.ObserverFactoryProvider;

/**
 * A builder which allows to create an Othello game.
 */
public interface OthelloGameBuilder extends GameBuilder {

    /**
     * The default number of rows (and columns) of an Othello board.
     */
    int DEFAULT_BOARD_SIZE = 8;

    /**
     * Creates an {@link OthelloPlayerBuilder} which allows to create and add a player to the game together with her
     * strategy.
     */
    OthelloPlayerBuilder createPlayerBuilder();

    /**
     * Adds a player builder and the corresponding strategy.
     *
     * @param playerBuilder The builder player used for creating the player.
     * @param strategy      The player's strategy.
     * @throws GameException if adding the player is not allowed by the rules of the game.
     */
    OthelloGameBuilder addPlayerBuilder(OthelloPlayerBuilder playerBuilder, OthelloStrategy strategy)
            throws GameException;

    /**
     * Changes the number of rows (and columns) of the board.
     * <p>
     * If this operation is called multiple times, only the last board size will be retained. If not called, the default
     * size of {@link #DEFAULT_BOARD_SIZE} rows and columns is used.
     *
     * @param newBoardSize The new number of rows (and columns) of the board.
     * @return {@code this}
     */
    OthelloGameBuilder changeBoardSize(int newBoardSize);

    /**
     * Changes the {@link ObserverFactoryProvider}.
     *
     * @param newObserverFactoryProvider The new {@link ObserverFactoryProvider}.
     * @return {@code this}
     */
    OthelloGameBuilder changeObserverFactoryProvider(ObserverFactoryProvider newObserverFactoryProvider);

    @Override
    OthelloGame build(int id) throws GameException, InterruptedException;
}
