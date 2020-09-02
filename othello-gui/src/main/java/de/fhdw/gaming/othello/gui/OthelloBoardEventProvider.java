/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-gui.
 *
 * Othello-gui is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Othello-gui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * othello-gui. If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.othello.gui;

import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.gui.event.OthelloBoardEvent;

/**
 * Provides input from the user interacting with an Othello board.
 */
public interface OthelloBoardEventProvider {

    /**
     * Waits for a user event and returns it.
     *
     * @param player It is this player's turn.
     * @param state  A copy of the game state.
     * @return The {@link OthelloBoardEvent} generated by the user.
     */
    OthelloBoardEvent waitForEvent(OthelloPlayer player, OthelloState state);

    /**
     * If the provider is currently waiting for a user event, the wait operation will be cancelled.
     */
    void cancelWaiting();
}