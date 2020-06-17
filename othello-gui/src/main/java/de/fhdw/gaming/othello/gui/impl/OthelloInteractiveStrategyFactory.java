/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-strategy-examples.
 *
 * Othello-strategy-examples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Othello-strategy-examples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with othello-strategy-examples.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.othello.gui.impl;

import de.fhdw.gaming.othello.core.domain.OthelloStrategy;
import de.fhdw.gaming.othello.core.domain.factory.OthelloStrategyFactory;
import de.fhdw.gaming.othello.core.moves.factory.OthelloMoveFactory;

/**
 * Implements {@link OthelloStrategyFactory}.
 */
public final class OthelloInteractiveStrategyFactory implements OthelloStrategyFactory {

    @Override
    public OthelloStrategy create(final OthelloMoveFactory moveFactory) {
        return new OthelloInteractiveStrategy(moveFactory);
    }
}
