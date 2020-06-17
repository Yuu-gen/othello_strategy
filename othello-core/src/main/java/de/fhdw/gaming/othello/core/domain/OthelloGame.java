package de.fhdw.gaming.othello.core.domain;

import de.fhdw.gaming.core.domain.Game;
import de.fhdw.gaming.othello.core.moves.OthelloMove;

/**
 * Represents an Othello game.
 */
public interface OthelloGame extends Game<OthelloPlayer, OthelloState, OthelloMove, OthelloStrategy> {

    /**
     * Returns the number of rows (or columns) of this board.
     */
    int getBoardSize();
}
