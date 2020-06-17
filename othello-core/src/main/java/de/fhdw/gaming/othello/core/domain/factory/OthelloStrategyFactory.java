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
package de.fhdw.gaming.othello.core.domain.factory;

import de.fhdw.gaming.othello.core.domain.OthelloStrategy;
import de.fhdw.gaming.othello.core.moves.factory.OthelloMoveFactory;

/**
 * Represents a factory creating Othello strategies.
 */
@FunctionalInterface
public interface OthelloStrategyFactory {

    /**
     * Creates an Othello strategy.
     *
     * @param moveFactory The {@link OthelloMoveFactory} to use.
     * @return The Othello strategy.
     */
    OthelloStrategy create(OthelloMoveFactory moveFactory);
}
