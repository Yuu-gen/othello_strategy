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
package de.fhdw.gaming.othello.gui.impl;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.core.domain.OthelloStrategy;
import de.fhdw.gaming.othello.core.moves.OthelloMove;
import de.fhdw.gaming.othello.core.moves.factory.OthelloMoveFactory;
import de.fhdw.gaming.othello.gui.OthelloBoardEventProvider;
import de.fhdw.gaming.othello.gui.event.OthelloBoardEventVisitor;
import de.fhdw.gaming.othello.gui.event.OthelloMakeMoveBoardEvent;
import de.fhdw.gaming.othello.gui.event.OthelloResignGameBoardEvent;
import de.fhdw.gaming.othello.gui.event.OthelloSkipMoveBoardEvent;

/**
 * Implements {@link OthelloStrategy} by asking the user for a move.
 */
public final class OthelloInteractiveStrategy implements OthelloStrategy {

    /**
     * The factory for creating Othello moves.
     */
    private final OthelloMoveFactory moveFactory;

    /**
     * Creates an {@link OthelloInteractiveStrategy}.
     *
     * @param moveFactory The factory for creating Othello moves.
     */
    OthelloInteractiveStrategy(final OthelloMoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    /**
     * Asks the user to choose a move.
     */
    @Override
    public Optional<OthelloMove> computeNextMove(final int gameId, final OthelloPlayer player, final OthelloState state)
            throws GameException {

        final Optional<OthelloBoardEventProvider> provider = OthelloGuiObserverImpl.getEventProvider(gameId);
        if (provider.isEmpty()) {
            return Optional.empty();
        }

        final AtomicReference<OthelloMove> move = new AtomicReference<>();
        provider.get().waitForEvent(player, state).accept(new OthelloBoardEventVisitor() {

            @Override
            public void handleMakeMove(final OthelloMakeMoveBoardEvent event) {
                move.setPlain(
                        OthelloInteractiveStrategy.this.moveFactory
                                .createPlaceTokenMove(player.isUsingBlackTokens(), event.getFieldPosition()));
            }

            @Override
            public void handleSkipMove(final OthelloSkipMoveBoardEvent event) {
                move.setPlain(OthelloInteractiveStrategy.this.moveFactory.createSkipMove(player.isUsingBlackTokens()));
            }

            @Override
            public void handleResignGame(final OthelloResignGameBoardEvent event) {
                // nothing to do, "move" remains null
            }
        });

        return Optional.ofNullable(move.getPlain());
    }

    @Override
    public String toString() {
        return OthelloInteractiveStrategy.class.getSimpleName();
    }

    @Override
    public boolean isInteractive() {
        return true;
    }

    @Override
    public void abortRequested(final int gameId) {
        final Optional<OthelloBoardEventProvider> provider = OthelloGuiObserverImpl.getEventProvider(gameId);
        if (!provider.isEmpty()) {
            provider.get().cancelWaiting();
        }
    }
}
