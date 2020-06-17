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
import java.util.List;
import java.util.Map;

import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloField;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;

/**
 * Stub implementation of{@link OthelloBoard} for testing.
 */
public final class OthelloBoardStub implements OthelloBoard {

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean hasFieldAt(final OthelloPosition position) {
        return false;
    }

    @Override
    public OthelloField getFieldAt(final OthelloPosition position) {
        throw new IllegalArgumentException("position");
    }

    @Override
    public List<List<? extends OthelloField>> getFields() {
        return Collections.emptyList();
    }

    @Override
    public Map<OthelloPosition, ? extends OthelloField> getFieldsBeing(final OthelloFieldState fieldState) {
        return Collections.emptyMap();
    }

    @Override
    public OthelloBoard deepCopy() {
        return this;
    }
}
