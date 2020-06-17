package de.fhdw.gaming.othello.gui.event;

/**
 * Represents an {@link OthelloBoardEvent} where the user selected a field to make a move.
 */
public final class OthelloSkipMoveBoardEvent implements OthelloBoardEvent {

    @Override
    public void accept(final OthelloBoardEventVisitor visitor) {
        visitor.handleSkipMove(this);
    }
}
