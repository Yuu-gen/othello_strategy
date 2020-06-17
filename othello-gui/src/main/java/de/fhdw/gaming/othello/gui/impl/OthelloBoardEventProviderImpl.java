package de.fhdw.gaming.othello.gui.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloField;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import de.fhdw.gaming.othello.gui.OthelloBoardEventProvider;
import de.fhdw.gaming.othello.gui.event.OthelloBoardEvent;
import de.fhdw.gaming.othello.gui.event.OthelloMakeMoveBoardEvent;
import de.fhdw.gaming.othello.gui.event.OthelloSkipMoveBoardEvent;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Implements user interaction with an Othello board.
 */
final class OthelloBoardEventProviderImpl implements OthelloBoardEventProvider {

    /**
     * The associated {@link OthelloBoardView}.
     */
    private final OthelloBoardView boardView;

    /**
     * Constructor.
     *
     * @param boardView The associated {@link OthelloBoardView}.
     */
    OthelloBoardEventProviderImpl(final OthelloBoardView boardView) {
        this.boardView = boardView;
    }

    @Override
    public OthelloBoardEvent waitForEvent(final OthelloPlayer player, final OthelloState state) {
        final Map<OthelloPosition, OthelloFieldView> activeFieldViews = new LinkedHashMap<>();
        for (final Map.Entry<OthelloPosition, ? extends OthelloField> entry : state.getBoard()
                .getFieldsBeing(OthelloFieldState.EMPTY).entrySet()) {
            final OthelloField field = entry.getValue();
            if (field.isActive(player.isUsingBlackTokens())) {
                activeFieldViews.put(entry.getKey(), this.boardView.getFieldView(entry.getKey()).orElseThrow());
            }
        }

        final AtomicReference<OthelloBoardEvent> event = new AtomicReference<>();
        final Runnable cleanUp;
        if (activeFieldViews.isEmpty()) {
            final List<OthelloFieldView> fieldViews = this.setupInactiveFields(state.getBoard(), event);
            cleanUp = () -> this.cleanUpFields(fieldViews);
        } else {
            this.setupActiveFields(activeFieldViews, event);
            cleanUp = () -> this.cleanUpFields(activeFieldViews.values());
        }

        try {
            this.boardView.getUserInputSemaphore().acquire();
            return event.get();
        } catch (final InterruptedException e) {
            return null;
        } finally {
            Platform.runLater(cleanUp);
        }
    }

    /**
     * Sets up the fields when a move is possible (and hence the set of active fields is not empty).
     *
     * @param activeFieldViews The non-empty set of views for active fields.
     * @param event            The event to be set when the user selects an active field.
     */
    private void setupActiveFields(final Map<OthelloPosition, OthelloFieldView> activeFieldViews,
            final AtomicReference<OthelloBoardEvent> event) {
        for (final Map.Entry<OthelloPosition, OthelloFieldView> entry : activeFieldViews.entrySet()) {
            final OthelloPosition position = entry.getKey();
            final OthelloFieldView fieldView = entry.getValue();
            Platform.runLater(() -> {
                fieldView.setCursor(Cursor.CROSSHAIR);
                fieldView.setHighlighted(true);
                fieldView.setOnMouseClicked((final MouseEvent mouseEvent) -> {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        event.set(new OthelloMakeMoveBoardEvent(position));
                        this.boardView.getUserInputSemaphore().release();
                    }
                });
            });
        }
    }

    /**
     * Sets up the fields when no move is possible (and hence the set of active fields is empty).
     *
     * @param board The Othello board.
     * @param event The event to be set when the user skips a move by right-clicking on the board.
     * @return A list of all field views on the board.
     */
    private List<OthelloFieldView> setupInactiveFields(final OthelloBoard board,
            final AtomicReference<OthelloBoardEvent> event) {
        final List<OthelloField> fields = new ArrayList<>();
        board.getFields().forEach((final List<? extends OthelloField> list) -> fields.addAll(list));

        final List<OthelloFieldView> fieldViews = new ArrayList<>();
        for (final OthelloField field : fields) {
            final OthelloFieldView fieldView = this.boardView.getFieldView(field.getPosition()).orElseThrow();
            fieldViews.add(fieldView);
            Platform.runLater(() -> {
                fieldView.setCursor(Cursor.CLOSED_HAND);
                fieldView.setOnMouseClicked((final MouseEvent mouseEvent) -> {
                    event.set(new OthelloSkipMoveBoardEvent());
                    this.boardView.getUserInputSemaphore().release();
                });
            });
        }

        return fieldViews;
    }

    /**
     * Cleans up after user interaction by resetting mouse cursors and removing event handlers.
     *
     * @param fieldViews The modified field views.
     */
    private void cleanUpFields(final Collection<? extends OthelloFieldView> fieldViews) {
        for (final OthelloFieldView fieldView : fieldViews) {
            fieldView.setCursor(Cursor.DEFAULT);
            fieldView.setHighlighted(false);
            fieldView.setOnMouseClicked(null);
        }
    }

    @Override
    public void cancelWaiting() {
        this.boardView.getUserInputSemaphore().release();
    }
}
