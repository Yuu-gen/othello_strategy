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
package de.fhdw.gaming.othello.core.domain;

import de.fhdw.gaming.core.domain.State;

/**
 * Represents the state of an Othello game.
 */
public interface OthelloState extends State<OthelloPlayer, OthelloState> {

    /**
     * Returns the board.
     */
    OthelloBoard getBoard();

    /**
     * Returns the player using black tokens.
     */
    OthelloPlayer getBlackPlayer();

    /**
     * Returns the player using white tokens..
     */
    OthelloPlayer getWhitePlayer();

    /**
     * Returns the currently active player, i.e. the player that needs to make the next move.
     */
    OthelloPlayer getCurrentPlayer();

    /**
     * Indicates that a move has been completed.
     *
     * @param skipMove {@code true} if the last move was a skip move, else {@code false}.
     */
    void moveCompleted(boolean skipMove);

    /**
     * Returns the number of consecutive skip moves.
     */
    int getNumberOfConsecutiveSkips();
}
