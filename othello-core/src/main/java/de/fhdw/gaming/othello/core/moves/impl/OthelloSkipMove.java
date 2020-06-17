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

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloField;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloState;

/**
 * Represents a "move" which does nothing.
 * <p>
 * Note that this move is only allowed if the player is not able to do anything else.
 */
final class OthelloSkipMove extends AbstractOthelloMove {

    /**
     * {@code true} if a black token should be placed, and {@code false} if a white token should be placed.
     */
    private final boolean placingBlackToken;

    /**
     * Creates an {@link OthelloSkipMove} object.
     *
     * @param placingBlackToken {@code true} if a black token should be placed, and {@code false} if a white token
     *                          should be placed.
     */
    OthelloSkipMove(final boolean placingBlackToken) {
        this.placingBlackToken = placingBlackToken;
    }

    /**
     * Returns {@code true} if a black token should be placed, and {@code false} if a white token should be placed.
     */
    boolean isPlacingBlackToken() {
        return this.placingBlackToken;
    }

    @Override
    public void applyTo(final OthelloState state, final OthelloPlayer player) throws GameException {
        if (this.isPlacingBlackToken() != player.isUsingBlackTokens()) {
            throw new GameException(
                    String.format(
                            "Player %s cannot skip a %s move.",
                            player,
                            this.isPlacingBlackToken() ? OthelloFieldState.BLACK : OthelloFieldState.WHITE));
        }

        for (final OthelloField field : state.getBoard().getFieldsBeing(OthelloFieldState.EMPTY).values()) {
            if (field.isActive(this.placingBlackToken)) {
                final OthelloFieldState fieldState = this.placingBlackToken ? OthelloFieldState.BLACK
                        : OthelloFieldState.WHITE;
                throw new GameException(
                        String.format(
                                "Illegal skip move as placing a token being %s on the field at %s is possible.",
                                fieldState,
                                field.getPosition()));
            }
        }

        state.moveCompleted(true);
    }

    @Override
    public String toString() {
        return String.format("Skipping %s move", this.placingBlackToken ? "black" : "white");
    }
}
