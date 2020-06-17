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
package de.fhdw.gaming.othello.strategy.examples.maxFlips;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloDirection;
import de.fhdw.gaming.othello.core.domain.OthelloField;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.core.domain.OthelloStrategy;
import de.fhdw.gaming.othello.core.moves.OthelloMove;
import de.fhdw.gaming.othello.core.moves.factory.OthelloMoveFactory;

/**
 * Implements {@link OthelloStrategy} by choosing some move that flips a maximum number of tokens.
 */
public final class OthelloMaxFlipsMoveStrategy implements OthelloStrategy {

    /**
     * The factory for creating Othello moves.
     */
    private final OthelloMoveFactory moveFactory;

    /**
     * Creates an {@link OthelloMaxFlipsMoveStrategy}.
     *
     * @param moveFactory The factory for creating Othello moves.
     */
    OthelloMaxFlipsMoveStrategy(final OthelloMoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    /**
     * Chooses a move that flips a maximum number of tokens.
     */
    @Override
    public Optional<OthelloMove> computeNextMove(final int gameId, final OthelloPlayer player, final OthelloState state)
            throws GameException {

        final Map<OthelloField, Integer> tokensFlipped = new LinkedHashMap<>();
        final boolean usingBlackTokens = player.isUsingBlackTokens();
        final OthelloFieldState fieldState = usingBlackTokens ? OthelloFieldState.BLACK : OthelloFieldState.WHITE;

        for (final OthelloField field : state.getBoard().getFieldsBeing(OthelloFieldState.EMPTY).values()) {
            if (field.isActive(usingBlackTokens)) {
                tokensFlipped.put(field, this.getNumberOfTokensFlipped(field, fieldState));
            }
        }

        final List<OthelloField> bestFields = tokensFlipped.entrySet().stream()
                .sorted((item1, item2) -> item2.getValue().compareTo(item1.getValue())).map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (bestFields.isEmpty()) {
            return Optional.of(this.moveFactory.createSkipMove(usingBlackTokens));
        } else {
            final int index = new Random().nextInt(bestFields.size());
            return Optional
                    .of(this.moveFactory.createPlaceTokenMove(usingBlackTokens, bestFields.get(index).getPosition()));
        }
    }

    @Override
    public String toString() {
        return OthelloMaxFlipsMoveStrategy.class.getSimpleName();
    }

    /**
     * Counts the number of tokens that would be flipped if a token was placed on passed start field.
     *
     * @param field          The start field.
     * @param delimiterState The state of the field delimiting the line of tokens.
     * @return The number of tokens that would be flipped.
     */
    private int getNumberOfTokensFlipped(final OthelloField field, final OthelloFieldState delimiterState) {
        int tokensFlipped = 0;
        for (final OthelloDirection direction : OthelloDirection.values()) {
            tokensFlipped += field.getLineOfTokens(direction, delimiterState).size();
        }
        return tokensFlipped;
    }
}
