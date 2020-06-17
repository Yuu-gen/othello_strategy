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

import java.util.Map;

import de.fhdw.gaming.core.domain.DefaultGame;
import de.fhdw.gaming.core.domain.ObserverFactoryProvider;
import de.fhdw.gaming.othello.core.domain.OthelloGame;
import de.fhdw.gaming.othello.core.domain.OthelloMoveChecker;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.core.domain.OthelloStrategy;
import de.fhdw.gaming.othello.core.moves.OthelloMove;

/**
 * Implements the Othello game.
 */
final class OthelloGameImpl extends DefaultGame<OthelloPlayer, OthelloState, OthelloMove, OthelloStrategy>
        implements OthelloGame {

    /**
     * The number of rows (and columns) of the board.
     */
    private final int boardSize;

    /**
     * Creates an Othello game.
     *
     * @param id                      The ID of this game.
     * @param initialState            The initial state of the game.
     * @param strategies              The players' strategies.
     * @param moveChecker             The move checker.
     * @param observerFactoryProvider The {@link ObserverFactoryProvider}.
     * @throws IllegalArgumentException if the player sets do not match.
     * @throws InterruptedException     if creating the game has been interrupted.
     */
    OthelloGameImpl(final int id, final OthelloState initialState, final Map<String, OthelloStrategy> strategies,
            final OthelloMoveChecker moveChecker, final ObserverFactoryProvider observerFactoryProvider)
            throws IllegalArgumentException, InterruptedException {

        super(id, initialState, strategies, moveChecker, observerFactoryProvider);
        this.boardSize = initialState.getBoard().getSize();
    }

    @Override
    public String toString() {
        return String.format("OthelloGame[id=%d, boardSize=%d, %s]", this.getId(), this.boardSize, this.gameToString());
    }

    @Override
    public int getBoardSize() {
        return this.boardSize;
    }
}
