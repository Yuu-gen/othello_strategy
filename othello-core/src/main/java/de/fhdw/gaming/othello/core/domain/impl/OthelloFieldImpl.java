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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloDirection;
import de.fhdw.gaming.othello.core.domain.OthelloField;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;

/**
 * Implements {@link OthelloField}.
 */
final class OthelloFieldImpl implements OthelloField {

    /**
     * The board this field belongs to.
     */
    private final OthelloBoardImpl board;
    /**
     * The position at which this field is placed on the board.
     */
    private final OthelloPosition position;
    /**
     * The state of this field.
     */
    private OthelloFieldState state;

    /**
     * Creates an Othello field.
     *
     * @param board    The board this field belongs to.
     * @param position The position at which this field is placed on the board.
     * @param state    The state of this field.
     */
    OthelloFieldImpl(final OthelloBoardImpl board, final OthelloPosition position, final OthelloFieldState state) {
        this.board = Objects.requireNonNull(board, "board");
        this.position = Objects.requireNonNull(position, "position");
        this.state = state;
    }

    @Override
    public String toString() {
        return String.format("OthelloField[position=%s, state=%s]", this.position, this.state);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Does not compare the boards the fields belong to, respectively.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof OthelloFieldImpl) {
            final OthelloFieldImpl other = (OthelloFieldImpl) obj;
            return this.position.equals(other.position) && this.state.equals(other.state);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.position, this.state);
    }

    @Override
    public OthelloBoard getBoard() {
        return this.board;
    }

    @Override
    public OthelloPosition getPosition() {
        return this.position;
    }

    @Override
    public OthelloFieldState getState() {
        return this.state;
    }

    /**
     * Sets the current state of this field. Does not change the states of neighbour fields.
     *
     * @param newState The new state.
     * @throws IllegalArgumentException if the new state is {@link OthelloFieldState#EMPTY} and different from the
     *                                  previous state.
     */
    void setState(final OthelloFieldState newState) throws IllegalArgumentException {
        final OthelloFieldState oldState = this.state;
        if (oldState.equals(newState)) {
            // nothing to do
            return;
        }

        final boolean wasEmpty = oldState.equals(OthelloFieldState.EMPTY);
        final boolean isEmpty = newState.equals(OthelloFieldState.EMPTY);

        if (!wasEmpty && isEmpty) {
            throw new IllegalArgumentException(
                    String.format("The field at %s cannot become empty again.", this.position));
        }

        this.state = newState;
        this.board.fieldChangedState(this, oldState);
    }

    @Override
    public boolean hasNeighbour(final OthelloDirection direction) {
        return this.board.hasFieldAt(direction.step(this.position));
    }

    @Override
    public OthelloFieldImpl getNeighbour(final OthelloDirection direction) {
        if (!this.hasNeighbour(direction)) {
            throw new IllegalArgumentException(String.format("No %s neighbour at %s.", direction, this.position));
        }

        return this.board.getFieldAt(direction.step(this.position));
    }

    @Override
    public boolean isActive(final boolean placingBlackToken) {
        if (!this.state.equals(OthelloFieldState.EMPTY)) {
            return false;
        }

        final OthelloFieldState delimiterState = placingBlackToken ? OthelloFieldState.BLACK : OthelloFieldState.WHITE;
        for (final OthelloDirection direction : OthelloDirection.values()) {
            if (!this.getLineOfTokens(direction, delimiterState).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<OthelloFieldImpl> getLineOfTokens(final OthelloDirection direction,
            final OthelloFieldState delimiterState) {

        final Set<OthelloFieldImpl> fields = new LinkedHashSet<>();
        OthelloFieldImpl currentField = this;
        while (currentField.hasNeighbour(direction)) {
            currentField = currentField.getNeighbour(direction);
            if (currentField.getState().equals(delimiterState)) {
                // properly delimited line of tokens found
                return fields;
            } else if (currentField.getState().equals(OthelloFieldState.EMPTY)) {
                // line of tokens delimited by empty field
                return Collections.emptySet();
            } else {
                assert currentField.getState().equals(delimiterState.inverse());
                fields.add(currentField);
            }
        }

        // line of tokens delimited by board
        return Collections.emptySet();
    }

    @Override
    public void placeToken(final boolean blackToken) throws GameException {
        final OthelloFieldState newState = blackToken ? OthelloFieldState.BLACK : OthelloFieldState.WHITE;

        // a new token can be placed only on empty fields
        if (!this.state.equals(OthelloFieldState.EMPTY)) {
            throw new GameException(
                    String.format(
                            "Placing a token being %s on the non-empty field at %s"
                                    + " is not allowed according to the rules " + "of the game.",
                            newState,
                            this.position));
        }

        // a new token needs to cause at least one other token to be flipped
        final Set<OthelloFieldImpl> fieldsToBeFlipped = new LinkedHashSet<>();
        for (final OthelloDirection direction : OthelloDirection.values()) {
            fieldsToBeFlipped.addAll(this.getLineOfTokens(direction, newState));
        }
        if (fieldsToBeFlipped.isEmpty()) {
            throw new GameException(
                    String.format(
                            "Placing a token being %s on the field at %s"
                                    + " is not allowed as no other token can be flipped.",
                            newState,
                            this.position));
        }

        // placement is valid, change state
        this.setState(newState);

        for (final OthelloFieldImpl fieldToBeFlipped : fieldsToBeFlipped) {
            assert !fieldToBeFlipped.getState().equals(OthelloFieldState.EMPTY);
            fieldToBeFlipped.setState(newState);
        }
    }
}
