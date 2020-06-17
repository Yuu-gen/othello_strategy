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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloField;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;

/**
 * Implements {@link OthelloBoard}.
 */
final class OthelloBoardImpl implements OthelloBoard {

    /**
     * The minimum number of rows (and columns).
     */
    private static final int MINIMUM_BOARD_SIZE = 4;

    /**
     * The fields of the board.
     */
    private final List<List<OthelloFieldImpl>> fields;
    /**
     * The fields sorted by state.
     */
    private final Map<OthelloFieldState, Map<OthelloPosition, OthelloFieldImpl>> fieldsByState;

    /**
     * Creates an Othello board.
     *
     * @param size The number of rows (and columns). It must be an even positive number greater than or equal to 4.
     * @throws IllegalArgumentException if the size does not meet the requirements.
     */
    OthelloBoardImpl(final int size) throws IllegalArgumentException {
        if (size < OthelloBoardImpl.MINIMUM_BOARD_SIZE || size % 2 != 0) {
            throw new IllegalArgumentException(
                    String.format("The board size %d is not an even positive number.", size));
        }

        this.fieldsByState = new LinkedHashMap<>();
        final Map<OthelloPosition, OthelloFieldImpl> emptyFields = new LinkedHashMap<>();
        this.fieldsByState.put(OthelloFieldState.EMPTY, emptyFields);
        this.fieldsByState.put(OthelloFieldState.BLACK, new LinkedHashMap<>());
        this.fieldsByState.put(OthelloFieldState.WHITE, new LinkedHashMap<>());

        this.fields = new ArrayList<>(size);
        for (int rowIndex = 0; rowIndex < size; ++rowIndex) {
            final List<OthelloFieldImpl> row = new ArrayList<>(size);
            for (int columnIndex = 0; columnIndex < size; ++columnIndex) {
                final OthelloPosition position = OthelloPosition.of(rowIndex, columnIndex);
                final OthelloFieldImpl field = new OthelloFieldImpl(this, position, OthelloFieldState.EMPTY);
                row.add(field);
                emptyFields.put(position, field);
            }
            this.fields.add(row);
        }

        this.setup();
    }

    /**
     * Copies an Othello board.
     *
     * @param source The board to copy.
     */
    private OthelloBoardImpl(final OthelloBoardImpl source) {
        Objects.requireNonNull(source, "source");

        this.fieldsByState = new LinkedHashMap<>();
        this.fieldsByState.put(OthelloFieldState.EMPTY, new LinkedHashMap<>());
        this.fieldsByState.put(OthelloFieldState.BLACK, new LinkedHashMap<>());
        this.fieldsByState.put(OthelloFieldState.WHITE, new LinkedHashMap<>());

        final int size = source.getSize();
        this.fields = new ArrayList<>(size);
        for (int rowIndex = 0; rowIndex < size; ++rowIndex) {
            final List<OthelloFieldImpl> originRow = source.fields.get(rowIndex);
            final List<OthelloFieldImpl> row = new ArrayList<>(size);
            for (int columnIndex = 0; columnIndex < size; ++columnIndex) {
                final OthelloFieldImpl originField = originRow.get(columnIndex);
                final OthelloFieldImpl field = new OthelloFieldImpl(
                        this,
                        OthelloPosition.of(rowIndex, columnIndex),
                        originField.getState());
                row.add(field);
                this.fieldsByState.get(field.getState()).put(field.getPosition(), field);
            }
            this.fields.add(row);
        }
    }

    /**
     * Sets up the board by placing the first four tokens into the centre of the board.
     */
    private void setup() {
        final int start = this.getSize() / 2 - 1;
        this.fields.get(start).get(start).setState(OthelloFieldState.WHITE);
        this.fields.get(start).get(start + 1).setState(OthelloFieldState.BLACK);
        this.fields.get(start + 1).get(start).setState(OthelloFieldState.BLACK);
        this.fields.get(start + 1).get(start + 1).setState(OthelloFieldState.WHITE);
    }

    @Override
    public String toString() {
        return String.format("OthelloBoard[size=%d, fields=%s]", this.fields.size(), this.fields);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof OthelloBoardImpl) {
            final OthelloBoardImpl other = (OthelloBoardImpl) obj;
            return this.fields.equals(other.fields);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.fields.hashCode();
    }

    @Override
    public int getSize() {
        return this.fields.size();
    }

    @Override
    public boolean hasFieldAt(final OthelloPosition position) {
        final int size = this.getSize();
        final int row = position.getRow();
        final int column = position.getColumn();
        return row >= 0 && row < size && column >= 0 && column < size;
    }

    @Override
    public OthelloFieldImpl getFieldAt(final OthelloPosition position) {
        if (!this.hasFieldAt(position)) {
            throw new IllegalArgumentException(String.format("Position %s out of range.", position));
        }

        return this.fields.get(position.getRow()).get(position.getColumn());
    }

    @Override
    public List<List<? extends OthelloField>> getFields() {
        final List<List<? extends OthelloField>> result = new ArrayList<>();
        this.fields.forEach((final List<OthelloFieldImpl> row) -> result.add(new ArrayList<>(row)));
        return result;
    }

    @Override
    public Map<OthelloPosition, OthelloFieldImpl> getFieldsBeing(final OthelloFieldState fieldState) {
        return Collections.unmodifiableMap(this.fieldsByState.get(fieldState));
    }

    @Override
    public OthelloBoardImpl deepCopy() {
        return new OthelloBoardImpl(this);
    }

    /**
     * This operation is called by a {@link OthelloFieldImpl} when a field changes its state.
     *
     * @param field    The field that changed its state.
     * @param oldState The old state of the field.
     */
    void fieldChangedState(final OthelloFieldImpl field, final OthelloFieldState oldState) {
        this.fieldsByState.get(oldState).remove(field.getPosition());
        this.fieldsByState.get(field.getState()).put(field.getPosition(), field);
    }
}
