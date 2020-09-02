/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-strategy-template.
 *
 * Othello-strategy-template is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Othello-strategy-template is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with othello-strategy-template.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.othello.strategy.ITN_YV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloDirection;
import de.fhdw.gaming.othello.core.domain.OthelloField;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.core.domain.OthelloStrategy;
import de.fhdw.gaming.othello.core.moves.OthelloMove;
import de.fhdw.gaming.othello.core.moves.factory.OthelloMoveFactory;

/**
 * Implements {@link OthelloStrategy}.
 * <p>
 * TODO: Describe what it does.
 */
public final class OthelloMyStrategy implements OthelloStrategy {

    /**
     * The factory for creating Othello moves.
     */
    private final OthelloMoveFactory moveFactory;

    /**
     * Creates an {@link OthelloMyStrategy}.
     *
     * @param moveFactory The factory for creating Othello moves.
     */
    OthelloMyStrategy(final OthelloMoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    @Override
    public Optional<OthelloMove> computeNextMove(final int gameId, final OthelloPlayer player, final OthelloState state)
            throws GameException {

        // You now have to compute a valid move. For this to succeed, you can (and should) inspect all empty fields
        // on the board.
        final OthelloBoard board = state.getBoard();
        final Map<OthelloPosition, ? extends OthelloField> emptyFields = board.getFieldsBeing(OthelloFieldState.EMPTY);

        // But not all empty fields are eligible to have a token placed on it. So you first have to determine whether
        // your player uses black or white tokens...
        final boolean usingBlackTokens = player.isUsingBlackTokens();

        // ...and then you are able to filter all active fields for this token colour. Only on these fields is it
        // possible to place a suitable token according to the rules of the game.
        // (Of course you can use a traditional for-each loop instead of streams for filtering.)
        final List<OthelloField> activeFields = new ArrayList<>();
        emptyFields.values().stream().filter((final OthelloField field) -> field.isActive(usingBlackTokens))
                .forEachOrdered(activeFields::add);

        // The Othello game forces a player to skip a move if no valid move is possible. So you should check for this
        // situation first.
        if (activeFields.isEmpty()) {
            return Optional.of(this.moveFactory.createSkipMove(usingBlackTokens));
        }

        // Now you should try to compute which of these active fields gives your player the most chances to win.
        // You could e.g. select the first possible field to place a token on, or you could choose a field where
        // placing a token changes the largest number of foreign tokens to your colour. Not recommended (at least not
        // if you aim to win), but nevertheless possible is returning Optional.empty(), i.e. no move, which forces
        // your player to resign the game.
        //
        // The most interesting operations you can find in the OthelloField interface. Look especially at
        // {@link OthelloField#hasNeighbour(OthelloDirection)}, {@link OthelloField#getNeighbour(OthelloDirection)},
        // and {@link OthelloField#getLineOfTokens(OthelloDirection, OthelloFieldState)}.
        //
        // In this (a bit contrived) example we try to find an active field that has at least two fields with the
        // opponent's tokens below it.
        final OthelloFieldState opponentState = usingBlackTokens ? OthelloFieldState.WHITE : OthelloFieldState.BLACK;
        for (final OthelloField field : activeFields) {
            final int opponentsBelow = (isOpponent(field, OthelloDirection.SOUTHEAST, opponentState) ? 1 : 0)
                    + (isOpponent(field, OthelloDirection.SOUTH, opponentState) ? 1 : 0)
                    + (isOpponent(field, OthelloDirection.SOUTHWEST, opponentState) ? 1 : 0);

            if (opponentsBelow >= 2) {
                // We found a suitable field, now we place a token on that field.
                return Optional.of(this.moveFactory.createPlaceTokenMove(usingBlackTokens, field.getPosition()));
            }
        }

        // We did not find a suitable field, so we simply place a token on the first active field.
        return Optional.of(this.moveFactory.createPlaceTokenMove(usingBlackTokens, activeFields.get(0).getPosition()));
    }

    @Override
    public String toString() {
        return OthelloMyStrategy.class.getSimpleName();
    }

    /**
     * Determines if there is a neighbour field with the opponent's token in a given direction.
     *
     * @param field        The start field.
     * @param direction    The direction to turn to.
     * @param desiredState The expected state of the neighbour field.
     * @return {@code true} if the neighbour field exists and has the expected state, else {@code false}.
     */
    private static boolean isOpponent(final OthelloField field, final OthelloDirection direction,
            final OthelloFieldState desiredState) {
        return field.hasNeighbour(direction) && field.getNeighbour(direction).getState().equals(desiredState);
    }
}
