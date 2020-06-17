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
package de.fhdw.gaming.othello.strategy.examples.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloField;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.core.domain.OthelloStrategy;
import de.fhdw.gaming.othello.core.moves.OthelloMove;
import de.fhdw.gaming.othello.core.moves.factory.OthelloMoveFactory;

/**
 * Implements {@link OthelloStrategy} by randomly choosing the next possible move.
 */
public final class OthelloRandomMoveStrategy implements OthelloStrategy {

    /**
     * The factory for creating Othello moves.
     */
    private final OthelloMoveFactory moveFactory;

    /**
     * Creates an {@link OthelloRandomMoveStrategy}.
     *
     * @param moveFactory The factory for creating Othello moves.
     */
    OthelloRandomMoveStrategy(final OthelloMoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    /**
     * Chooses some random move.
     */
    @Override
    public Optional<OthelloMove> computeNextMove(final int gameId, final OthelloPlayer player, final OthelloState state)
            throws GameException {

        final boolean usingBlackTokens = player.isUsingBlackTokens();
        final List<OthelloField> fields = new ArrayList<>();
        for (final OthelloField field : state.getBoard().getFieldsBeing(OthelloFieldState.EMPTY).values()) {
            if (field.isActive(usingBlackTokens)) {
                fields.add(field);
            }
        }

        if (fields.isEmpty()) {
            return Optional.of(this.moveFactory.createSkipMove(usingBlackTokens));
        } else {
            final int index = new Random().nextInt(fields.size());
            final OthelloField field = fields.get(index);
            return Optional.of(this.moveFactory.createPlaceTokenMove(usingBlackTokens, field.getPosition()));
        }
    }

    @Override
    public String toString() {
        return OthelloRandomMoveStrategy.class.getSimpleName();
    }
}
