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
package de.fhdw.gaming.othello.strategy.examples.minOppMob;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloBoard;
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
public final class OthelloMinOppMob implements OthelloStrategy {

    /**
     * The factory for creating Othello moves.
     */
    private final OthelloMoveFactory moveFactory;

    /**
     * Creates an {@link OthelloMyStrategy}.
     *
     * @param moveFactory The factory for creating Othello moves.
     */
    OthelloMinOppMob(final OthelloMoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    /**
     * place to put the state before modifying it for computations.
     */
    private OthelloBoard workboard = null;

    @Override
    public Optional<OthelloMove> computeNextMove(final int gameId, final OthelloPlayer player, final OthelloState state)
            throws GameException {

        this.workboard = state.getBoard().deepCopy();
        final boolean usingBlackTokens = player.isUsingBlackTokens();

        final List<OthelloField> activeFields = this.setup(state.getBoard(), usingBlackTokens);

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
        // In the following Stategy we place Tokens on all Fields,as if we were the only player.
        // then we discover how stupid it is to return the first Element of an Empty List and remember that we have to
        // actually reset the
        // Boardstate after we used place token and cry
//        OthelloField bestField = activeFields.get(0);
//        Integer bestFieldValue = 100;
//        Integer value = 0;
//        for (int i = 0; i < activeFields.size(); i++) {
//            this.workstate = state.deepCopy();
//            value = this.evaluate(this.setup(this.workstate.getBoard(), usingBlackTokens).get(i), usingBlackTokens);
//            // System.out.println(this.oldstate.getBoard());
//            if (value < bestFieldValue) {
//                bestField = activeFields.get(i);
//                bestFieldValue = value;
//            }
////            System.out.println(
////                    "the value for" + activeFields.get(i).getPosition() + " is: "
////                            + this.evaluate(activeFields.get(i), usingBlackTokens));
////            System.out.println(activeFields + "   nachmPlaceen");
//        }

        // We did not find a suitable field, so we simply place a token on the first active field.
        // System.out.println(activeFields.get(0) + "will be placed");

//        try {
//            System.out.println(
//                    this.crushTree(this.buildTree(state.getBoard(), usingBlackTokens, activeFields, 5), usingBlackTokens,5)
//                            .toString()
//            );
//        } catch (GameException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

//        final FieldIntTuple besttuple = minmax(state, usingBlackTokens, activeFields,4);

//                this
//                .crushTree(this.buildTree(state.getBoard(), usingBlackTokens, activeFields, 4), usingBlackTokens, 4);

//        System.out.println(besttuple.getValue());
        final OthelloPosition bestposition = this.calculate(activeFields, usingBlackTokens, state);
//        final OthelloPosition bestposition = this.calculate(activeFields, usingBlackTokens, state);
        return Optional.of(this.moveFactory.createPlaceTokenMove(usingBlackTokens, bestposition));
    }

    @Override
    public String toString() {
        return OthelloMinOppMob.class.getSimpleName();
    }

    /**
     * Generates a list of active fields, wich is neccassary for almost all Strategies.
     *
     * @param board            the Board to find the active fields of
     * @param usingBlackTokens If black is the player to move
     * @return a list of active fields
     */
    private List<OthelloField> setup(final OthelloBoard board, final boolean usingBlackTokens) {
        final Map<OthelloPosition, ? extends OthelloField> emptyFields = board.getFieldsBeing(OthelloFieldState.EMPTY);

        // But not all empty fields are eligible to have a token placed on it. So you first have to determine whether
        // your player uses black or white tokens...
        // siehe Formalmarameter

        // ...and then you are able to filter all active fields for this token colour. Only on these fields is it
        // possible to place a suitable token according to the rules of the game.
        // (Of course you can use a traditional for-each loop instead of streams for filtering.)
        final List<OthelloField> activeFields = new ArrayList<>();
        emptyFields.values().parallelStream().filter((final OthelloField field) -> field.isActive(usingBlackTokens))
                .forEachOrdered(activeFields::add);
        return activeFields;
    }

    /**
     * Assigns a value to a field representing how good placing a token on it would be.
     *
     * @param field            field to be evaluated
     * @param usingBlackTokens If black is the player to move
     * @return value of the field (you want to maximize this for you and minimize it for your opponent
     * @throws GameException
     */
    private Integer evaluate(final OthelloField field, final boolean usingBlackTokens) throws GameException {
        field.placeToken(usingBlackTokens);
        final Integer value = this.setup(field.getBoard(), !usingBlackTokens).size();
        return value;
    }

    /**
     * Determines which field a token should be placed on.
     *
     * @inefficient generates the same List of active Fields for each examined move.But i don't know how to rectify this
     *              because java makes cloning hard.
     *
     * @param activeFields     a List of fields a Token could be placed on
     * @param usingBlackTokens If black is the player to move
     * @param workstate        state to be manipulated for calculation (copy of state)
     * @param state            state for which a move is to be determined
     * @return field on which a token should be placed
     * @throws GameException
     */
    private OthelloPosition calculate(final List<OthelloField> activeFields, final boolean usingBlackTokens,
            final OthelloState state) throws GameException {
        OthelloField bestField = activeFields.get(0);
        Integer bestFieldValue = 100;
        Integer value = 0;

        for (int i = 0; i < activeFields.size(); i++) {
            this.workboard = state.getBoard().deepCopy();
            value = this.evaluate(this.setup(this.workboard, usingBlackTokens).get(i), usingBlackTokens);
            // System.out.println(this.oldstate.getBoard());
            if (value < bestFieldValue) {
                bestField = activeFields.get(i);
                bestFieldValue = value;
            }
        }
        return bestField.getPosition();
    }

}
