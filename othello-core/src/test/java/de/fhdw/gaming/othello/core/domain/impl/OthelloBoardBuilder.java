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

import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;

/**
 * Builds a board.
 */
public final class OthelloBoardBuilder {

    /**
     * The board to be built.
     */
    private final OthelloBoardImpl board;

    /**
     * Creates an initial board.
     *
     * @param size The number of rows (and columns) of the board.
     */
    public OthelloBoardBuilder(final int size) {
        this.board = new OthelloBoardImpl(size);
    }

    /**
     * Returns the resulting board.
     */
    public OthelloBoard build() {
        return this.board;
    }

    /**
     * Sets the state of a field.
     *
     * @param row      The row of the field.
     * @param column   The column of the field.
     * @param newState The new state of the field.
     * @return {@code this}
     */
    public OthelloBoardBuilder changeFieldState(final int row, final int column, final OthelloFieldState newState) {
        this.board.getFieldAt(OthelloPosition.of(row, column)).setState(newState);
        return this;
    }
}
