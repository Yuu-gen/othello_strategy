package de.fhdw.gaming.othello.gui.event;

/**
 * Represents an event that is created due to a user interaction with the Othello board.
 */
public interface OthelloBoardEvent {

    /**
     * Accepts an {@link OthelloBoardEventVisitor}.
     *
     * @param visitor The visitor.
     */
    void accept(OthelloBoardEventVisitor visitor);
}
