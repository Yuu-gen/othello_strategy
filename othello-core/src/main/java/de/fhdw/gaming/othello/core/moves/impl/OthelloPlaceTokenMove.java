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

import java.util.Objects;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;
import de.fhdw.gaming.othello.core.domain.OthelloState;

/**
 * Represents a move which places a token on the board.
 * <p>
 * Note that this move is only allowed if the colour of the token placed belongs to the corresponding player, and if the
 * move leads to at least one other token to be flipped.
 */
final class OthelloPlaceTokenMove extends AbstractOthelloMove {

    /**
     * {@code true} if a black token is placed, and {@code false} if a white token is placed.
     */
    private final boolean placingBlackToken;
    /**
     * The position of the token placed on the board.
     */
    private final OthelloPosition tokenPosition;

    /**
     * Creates an {@link OthelloPlaceTokenMove} object.
     *
     * @param placingBlackToken {@code true} if a black token is placed, and {@code false} if a white token is placed.
     * @param tokenPosition     The position of the token placed on the board.
     */
    OthelloPlaceTokenMove(final boolean placingBlackToken, final OthelloPosition tokenPosition) {
        this.placingBlackToken = placingBlackToken;
        this.tokenPosition = Objects.requireNonNull(tokenPosition, "tokenPosition");
    }

    /**
     * Returns {@code true} if a black token is placed, and {@code false} if a white token is placed.
     */
    boolean isPlacingBlackToken() {
        return this.placingBlackToken;
    }

    /**
     * Returns the position of the token placed on the board.
     */
    OthelloPosition getTokenPosition() {
        return this.tokenPosition;
    }

    @Override
    public void applyTo(final OthelloState state, final OthelloPlayer player) throws GameException {
        if (this.isPlacingBlackToken() != player.isUsingBlackTokens()) {
            throw new GameException(
                    String.format(
                            "Player %s cannot place a %s token.",
                            player,
                            this.isPlacingBlackToken() ? OthelloFieldState.BLACK : OthelloFieldState.WHITE));
        }

        state.getBoard().getFieldAt(this.tokenPosition).placeToken(this.placingBlackToken);
        state.moveCompleted(false);
    }

    @Override
    public String toString() {
        return String.format(
                "Placing %s token on field at %s",
                this.placingBlackToken ? "black" : "white",
                this.tokenPosition);
    }
}
