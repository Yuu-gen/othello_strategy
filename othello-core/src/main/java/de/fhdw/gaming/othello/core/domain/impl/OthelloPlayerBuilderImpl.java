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

import java.util.Optional;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloPlayerBuilder;
import de.fhdw.gaming.othello.core.domain.OthelloState;

/**
 * Implements {@link OthelloPlayerBuilder}.
 */
final class OthelloPlayerBuilderImpl implements OthelloPlayerBuilder {

    /**
     * The name of the player.
     */
    private Optional<String> name;
    /**
     * If {@code true}, the player will be using black tokens, else she will be using white tokens.
     */
    private boolean usingBlackTokens;

    /**
     * Creates an {@link OthelloPlayerBuilderImpl}.
     */
    OthelloPlayerBuilderImpl() {
        this.name = Optional.empty();
        this.usingBlackTokens = true;
    }

    @Override
    public OthelloPlayerBuilderImpl changeName(final String newName) {
        this.name = Optional.of(newName);
        return this;
    }

    @Override
    public OthelloPlayerBuilderImpl changeUsingBlackTokens(final boolean newUsingBlackTokens) {
        this.usingBlackTokens = newUsingBlackTokens;
        return this;
    }

    @Override
    public boolean isUsingBlackTokens() {
        return this.usingBlackTokens;
    }

    @Override
    public OthelloPlayer build(final OthelloState state) throws GameException {
        return new OthelloPlayerImpl(state, this.name.orElseThrow(), this.usingBlackTokens);
    }
}
