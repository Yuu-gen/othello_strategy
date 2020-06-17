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
package de.fhdw.gaming.othello.core.moves.factory;

import de.fhdw.gaming.othello.core.domain.OthelloPosition;
import de.fhdw.gaming.othello.core.moves.OthelloMove;

/**
 * Allows to create Othello moves.
 */
public interface OthelloMoveFactory {

    /**
     * Creates a move that places a token on the board.
     *
     * @param placingBlackToken {@code true} if a black token is placed, and {@code false} if a white token is placed.
     * @param tokenPosition     The position of the token placed on the board.
     */
    OthelloMove createPlaceTokenMove(boolean placingBlackToken, OthelloPosition tokenPosition);

    /**
     * Creates a "move" that skips the current move.
     *
     * @param placingBlackToken {@code true} if a black token should be placed, and {@code false} if a white token
     *                          should be placed.
     */
    OthelloMove createSkipMove(boolean placingBlackToken);
}
