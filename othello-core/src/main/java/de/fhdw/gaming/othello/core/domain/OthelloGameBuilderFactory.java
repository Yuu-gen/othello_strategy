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

import de.fhdw.gaming.core.domain.GameBuilderFactory;
import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.ui.InputProvider;

/**
 * An Othello {@link GameBuilderFactory} which allows to create an Othello game builder.
 */
public interface OthelloGameBuilderFactory extends GameBuilderFactory {

    /**
     * Parameter for the number of rows (and columns) of the board.
     */
    String PARAM_BOARD_SIZE = "boardSize";
    /**
     * Parameter that determines if a player is using black or white tokens.
     */
    String PARAM_PLAYER_USING_BLACK_TOKENS = "playerUsingBlackTokens";

    @Override
    OthelloGameBuilder createGameBuilder(InputProvider inputProvider) throws GameException;
}
