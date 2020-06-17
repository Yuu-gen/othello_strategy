/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-core.
 *
 * Othello-core is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Othello-core is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * othello-core. If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.othello.core.moves.impl;

import de.fhdw.gaming.othello.core.moves.OthelloMove;

/**
 * Represents a move allowed by the rules of the game.
 * <p>
 * The purpose of this class is solely to be able to check whether an {@link OthelloMove} implementation is allowed by
 * the rules of the game. As this class is not public, custom strategies are unable to create {@link OthelloMove}
 * objects that inherit from this class, so custom moves can be distinguished from possible moves easily.
 */
public abstract class AbstractOthelloMove implements OthelloMove {

    /**
     * Protected constructor.
     */
    protected AbstractOthelloMove() {
        // nothing to do
    }
}
