package de.fhdw.gaming.othello.gui.event;

import de.fhdw.gaming.othello.core.domain.OthelloPosition;

/**
 * Represents an {@link OthelloBoardEvent} where the user selected a field to make a move.
 */
public final class OthelloMakeMoveBoardEvent implements OthelloBoardEvent {

    /**
     * The position of the field selected.
     */
    private final OthelloPosition fieldPosition;

    /**
     * Constructor.
     *
     * @param fieldPosition The position of the field selected.
     */
    public OthelloMakeMoveBoardEvent(final OthelloPosition fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    /**
     * Returns the position of the field selected.
     */
    public OthelloPosition getFieldPosition() {
        return this.fieldPosition;
    }

    @Override
    public void accept(final OthelloBoardEventVisitor visitor) {
        visitor.handleMakeMove(this);
    }
}
