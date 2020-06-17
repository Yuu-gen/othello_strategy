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

/**
 * Represents a field position on a board. It contains a row number and a column number (both zero-based).
 */
public final class OthelloPosition {

    /**
     * The row index.
     */
    private final int row;
    /**
     * The column index.
     */
    private final int column;

    /**
     * Creates a position on an Othello board.
     *
     * @param row    The row number (zero based).
     * @param column The column number (zero-based).
     */
    private OthelloPosition(final int row, final int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Creates a position on an Othello board.
     *
     * @param row    The row number (zero based).
     * @param column The column number (zero-based).
     * @return The position.
     */
    public static OthelloPosition of(final int row, final int column) {
        return new OthelloPosition(row, column);
    }

    /**
     * Returns the zero-based row number.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the zero-based column number.
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Creates a position relative to this one. The position is not checked against any bounds.
     *
     * @param rowOffset    The row offset. May be negative, positive, or zero.
     * @param columnOffset The column offset. May be negative, positive, or zero.
     * @return The resulting position.
     */
    public OthelloPosition offset(final int rowOffset, final int columnOffset) {
        return OthelloPosition.of(this.row + rowOffset, this.column + columnOffset);
    }

    @Override
    public String toString() {
        return String.format("%c%d", (char) (this.column + 'A'), this.row + 1);
    }

    @Override
    public int hashCode() {
        return this.row ^ this.column;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof OthelloPosition) {
            final OthelloPosition other = (OthelloPosition) obj;
            return this.column == other.column && this.row == other.row;
        }
        return false;
    }
}
