package de.fhdw.gaming.othello.gui.event;

/**
 * Visits an {@link OthelloBoardEvent}.
 */
public interface OthelloBoardEventVisitor {

    /**
     * Handles an {@link OthelloMakeMoveBoardEvent}.
     *
     * @param event The event to handle.
     */
    void handleMakeMove(OthelloMakeMoveBoardEvent event);

    /**
     * Handles an {@link OthelloSkipMoveBoardEvent}.
     *
     * @param event The event to handle.
     */
    void handleSkipMove(OthelloSkipMoveBoardEvent event);

    /**
     * Handles an {@link OthelloResignGameBoardEvent}.
     *
     * @param event The event to handle.
     */
    void handleResignGame(OthelloResignGameBoardEvent event);
}
