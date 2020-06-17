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

import de.fhdw.gaming.core.domain.GameException;

/**
 * A builder which allows to create an Othello player.
 */
public interface OthelloPlayerBuilder {

    /**
     * Changes the name of the player.
     * <p>
     * There is no default.
     *
     * @param newName The name of the player.
     * @return {@code this}
     */
    OthelloPlayerBuilder changeName(String newName);

    /**
     * Determines whether the player is using black or white tokens.<
     * <p>
     * The default value if not called is {@code true}.
     *
     * @param newUsingBlackTokens If {@code true}, the player will be using black tokens, else she will be using white
     *                            tokens.
     * @return {@code this}
     */
    OthelloPlayerBuilder changeUsingBlackTokens(boolean newUsingBlackTokens);

    /**
     * Returns {@code true} if this player builder uses the black tokens, and {@code false} if she uses the white
     * tokens.
     */
    boolean isUsingBlackTokens();

    /**
     * Builds the player.
     *
     * @param state The Othello game state.
     * @return The Othello player.
     * @throws GameException if creating the player is not allowed by the rules of the game.
     */
    OthelloPlayer build(OthelloState state) throws GameException;
}
