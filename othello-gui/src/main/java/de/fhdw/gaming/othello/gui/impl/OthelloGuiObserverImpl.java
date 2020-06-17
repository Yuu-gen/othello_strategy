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
package de.fhdw.gaming.othello.gui.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import de.fhdw.gaming.core.domain.Game;
import de.fhdw.gaming.gui.GuiObserver;
import de.fhdw.gaming.othello.core.domain.OthelloGame;
import de.fhdw.gaming.othello.gui.OthelloBoardEventProvider;
import javafx.scene.Node;

/**
 * Implements {@link GuiObserver}.
 */
final class OthelloGuiObserverImpl implements GuiObserver {

    /**
     * The primary (i.e. first created) {@link OthelloGuiObserverImpl} instance of this class.
     */
    private static final AtomicReference<WeakReference<OthelloGuiObserverImpl>> INSTANCE = new AtomicReference<>();

    /**
     * The {@link OthelloBoardView} objects per Othello game ID.
     */
    private final Map<Integer, OthelloBoardView> boardViews;

    /**
     * Creates an {@link OthelloGuiObserverImpl}.
     */
    OthelloGuiObserverImpl() {
        OthelloGuiObserverImpl.INSTANCE.compareAndSet(null, new WeakReference<>(this));
        this.boardViews = new LinkedHashMap<>();
    }

    /**
     * Returns an {@link OthelloBoardEventProvider} for a given game.
     *
     * @param gameId The game ID.
     * @return The {@link OthelloBoardEventProvider}.
     */
    static Optional<OthelloBoardEventProvider> getEventProvider(final int gameId) {
        final OthelloGuiObserverImpl instance = Optional.ofNullable(OthelloGuiObserverImpl.INSTANCE.get())
                .map(Reference::get).orElse(null);
        if (instance == null) {
            return Optional.empty();
        } else {
            final OthelloBoardView boardView = instance.boardViews.get(gameId);
            return boardView == null ? Optional.empty() : Optional.of(new OthelloBoardEventProviderImpl(boardView));
        }
    }

    @Override
    public Optional<Node> gameCreated(final Game<?, ?, ?, ?> game) {
        if (game instanceof OthelloGame) {
            final OthelloBoardView boardView = new OthelloBoardView((OthelloGame) game);
            this.boardViews.put(game.getId(), boardView);
            return Optional.of(boardView.getNode());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void gamePaused(final Game<?, ?, ?, ?> game) {
        if (game instanceof OthelloGame) {
            final OthelloBoardView boardView = this.boardViews.get(game.getId());
            if (boardView != null) {
                boardView.gamePaused((OthelloGame) game);
            }
        }
    }

    @Override
    public void gameResumed(final Game<?, ?, ?, ?> game) {
        if (game instanceof OthelloGame) {
            final OthelloBoardView boardView = this.boardViews.get(game.getId());
            if (boardView != null) {
                boardView.gameResumed((OthelloGame) game);
            }
        }
    }

    @Override
    public void gameDestroyed(final Game<?, ?, ?, ?> game) {
        if (game instanceof OthelloGame) {
            final OthelloBoardView boardView = this.boardViews.remove(game.getId());
            if (boardView != null) {
                boardView.destroy((OthelloGame) game);
            }
        }
    }
}
