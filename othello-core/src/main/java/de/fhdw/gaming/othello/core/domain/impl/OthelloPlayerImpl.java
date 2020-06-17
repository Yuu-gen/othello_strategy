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
package de.fhdw.gaming.othello.core.domain.impl;

import java.util.Objects;

import de.fhdw.gaming.core.domain.PlayerState;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloState;

/**
 * Implements {@link OthelloPlayer}.
 */
final class OthelloPlayerImpl implements OthelloPlayer {

    /**
     * The associated game state.
     */
    private final OthelloState gameState;
    /**
     * The name of this player.
     */
    private final String name;
    /**
     * {@code true} if this player uses the black tokens, or {@code false} if she uses the white tokens.
     */
    private final boolean usingBlackTokens;

    /**
     * Creates an Othello player.
     *
     * @param gameState        The associated game state.
     * @param name             The name of the player.
     * @param usingBlackTokens {@code true} if this player uses the black tokens, or {@code false} if she uses the white
     *                         tokens.
     */
    OthelloPlayerImpl(final OthelloState gameState, final String name, final boolean usingBlackTokens) {
        this.gameState = gameState;
        this.name = Objects.requireNonNull(name, "name");
        this.usingBlackTokens = usingBlackTokens;
    }

    @Override
    public String toString() {
        return String.format(
                "OthelloPlayer[name=%s, tokens=%s, state=%s]",
                this.name,
                this.usingBlackTokens ? OthelloFieldState.BLACK.toString() : OthelloFieldState.WHITE.toString(),
                this.getState());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof OthelloPlayerImpl) {
            final OthelloPlayerImpl other = (OthelloPlayerImpl) obj;
            return this.name.equals(other.name) && this.usingBlackTokens == other.usingBlackTokens
                    && this.getState().equals(other.getState());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.usingBlackTokens);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isUsingBlackTokens() {
        return this.usingBlackTokens;
    }

    @Override
    public PlayerState getState() {
        return this.gameState.getPlayerState(this.name);
    }

    @Override
    public OthelloPlayer deepCopy(final OthelloState newGameState) {
        return new OthelloPlayerImpl(newGameState, this.name, this.usingBlackTokens);
    }
}
