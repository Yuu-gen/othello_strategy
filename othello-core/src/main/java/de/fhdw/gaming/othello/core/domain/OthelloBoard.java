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

import java.util.List;
import java.util.Map;

import de.fhdw.gaming.core.domain.Stateful;

/**
 * Represents the Othello board.
 * <p>
 * An Othello board is a square and hence possesses an identical non-zero positive number of rows and columns. Each
 * combination of a row and a column is described by a {@link OthelloPosition position}. At each position, there is a
 * {@link OthelloField field} which describes whether the field is empty or occupied by a token.
 */
public interface OthelloBoard extends Stateful {

    /**
     * Returns the number of rows (or columns) of this board.
     */
    int getSize();

    /**
     * Checks whether this board contains a field at the position passed.
     *
     * @param position The position.
     * @return {@code true} if this board contains a field at the position passed, else {@code false}.
     */
    boolean hasFieldAt(OthelloPosition position);

    /**
     * Returns the field at the given position.
     *
     * @param position The position of the field to return.
     * @return The field.
     * @throws IllegalArgumentException if the position is out of range, i.e. if it does not denote a field.
     */
    OthelloField getFieldAt(OthelloPosition position);

    /**
     * Returns all fields of this board line by line.
     * <p>
     * The list(s) returned are possibly immutable, so do not modify them.
     */
    List<List<? extends OthelloField>> getFields();

    /**
     * Returns all fields of a given state.
     * <p>
     * The map returned returned is possibly immutable, so do not modify it.
     *
     * @param fieldState The state of the fields to return.
     * @return The fields.
     */
    Map<OthelloPosition, ? extends OthelloField> getFieldsBeing(OthelloFieldState fieldState);

    @Override
    OthelloBoard deepCopy();
}
