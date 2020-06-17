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

import java.util.Set;

import de.fhdw.gaming.core.domain.GameException;

/**
 * Represents a single field on a {@link OthelloBoard board}.
 * <p>
 * A field has a {@link OthelloPosition position}. This position is fixed and does not change.
 * <p>
 * A field has a {@link OthelloFieldState state}. The state may change, but only the following state transitions are
 * allowed:
 * <ul>
 * <li>{@link OthelloFieldState#EMPTY EMPTY} -&gt; {@link OthelloFieldState#BLACK BLACK}</li>
 * <li>{@link OthelloFieldState#EMPTY EMPTY} -&gt; {@link OthelloFieldState#WHITE WHITE}</li>
 * <li>{@link OthelloFieldState#WHITE WHITE}-&gt; {@link OthelloFieldState#BLACK BLACK}</li>
 * <li>{@link OthelloFieldState#BLACK BLACK} -&gt; {@link OthelloFieldState#WHITE WHITE}</li>
 * </ul>
 * In other words, a non-empty field may never become empty again.
 * <p>
 * The state of a field cannot be changed directly. Instead, a conforming move has to be submitted to the rule engine.
 */
public interface OthelloField {

    /**
     * Returns the board this field belongs to.
     */
    OthelloBoard getBoard();

    /**
     * Returns the position of this field.
     */
    OthelloPosition getPosition();

    /**
     * Returns the current state of this field.
     */
    OthelloFieldState getState();

    /**
     * Determines whether there is a neighbour field in a given direction.
     *
     * @param direction The direction to use.
     * @return {@code true} if there is a neighbour field in the given direction, otherwise {@code false}.
     */
    boolean hasNeighbour(OthelloDirection direction);

    /**
     * Returns a neighbour field in a given direction.
     *
     * @param direction The direction to use.
     * @return The corresponding neighbour field.
     * @throws IllegalArgumentException if there is no neighbour field in the given direction.
     */
    OthelloField getNeighbour(OthelloDirection direction) throws IllegalArgumentException;

    /**
     * Determines whether this is an active field for placing a token of the given colour. For this to be true, this
     * field needs to be {@link OthelloFieldState#EMPTY empty}, and it has to start at least one continuous line of
     * tokens of the other colour, delimited by a token of the same colour.
     *
     * @param placingBlackToken {@code true} if checking for an active black field, and {@code false} if checking for an
     *                          active white field.
     * @return {@code true} if this is an active field for the given colour, else {@code false}.
     */
    boolean isActive(boolean placingBlackToken);

    /**
     * Determines whether there is a line of tokens in the given direction that could be enclosed by a token.
     *
     * @param direction      The direction.
     * @param delimiterState The state of the field delimiting the line of tokens.
     * @return The fields containing the tokens in the line (excluding this field and the delimiting field). This is
     *         always a valid set, but may be empty if no line of tokens could be found in the given direction. Note
     *         that the set returned is not necessarily mutable.
     */
    Set<? extends OthelloField> getLineOfTokens(OthelloDirection direction, OthelloFieldState delimiterState);

    /**
     * Places a token on this field. Requires this field to be active. Computes and changes the state of neighbour
     * fields according to the rules of the game.
     *
     * @param blackToken {@code true} if a black token is placed, and {@code false} if a white token is placed.
     * @throws GameException if placing a token of the given colour is not allowed according to the rules of the game.
     */
    void placeToken(boolean blackToken) throws GameException;
}
