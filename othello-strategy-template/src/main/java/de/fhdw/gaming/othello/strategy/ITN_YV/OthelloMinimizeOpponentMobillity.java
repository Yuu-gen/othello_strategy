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
import java.util.Set;

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
public final class OthelloMinimizeOpponentMobillity implements OthelloStrategy {

    /**
     * The factory for creating Othello moves.
     */
    private final OthelloMoveFactory moveFactory;

    /**
     * Creates an {@link OthelloMyStrategy}.
     *
     * @param moveFactory The factory for creating Othello moves.
     */
    OthelloMinimizeOpponentMobillity(final OthelloMoveFactory moveFactory) {
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
        System.out.println(
                this.crushTree(this.buildTree(state.getBoard(), usingBlackTokens, activeFields, 2), usingBlackTokens,2)
                        .toString()
        );

        final OthelloField bestField = this.calculate(activeFields, usingBlackTokens, state);
        return Optional.of(this.moveFactory.createPlaceTokenMove(usingBlackTokens, bestField.getPosition()));
    }

    @Override
    public String toString() {
        return OthelloMinimizeOpponentMobillity.class.getSimpleName();
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
        emptyFields.values().stream().filter((final OthelloField field) -> field.isActive(usingBlackTokens))
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
    private OthelloField calculate(final List<OthelloField> activeFields, final boolean usingBlackTokens,
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
        return bestField;
    }

    /**
     * Builds a one layer deep tree with the board you give it as root and all possible boards one move into the future
     * as children.
     *
     * @param board
     * @param usingBlackTokens
     * @param activeFields
     * @return
     * @throws GameException
     */
    private Node<FieldIntTuple> growTree(final OthelloBoard board, final boolean usingBlackTokens,
            final List<OthelloField> activeFields) throws GameException {
        final Node<FieldIntTuple> rootpos = new Node<>(new FieldIntTuple(0, new OthelloField() {
            
            @Override
            public void placeToken(boolean blackToken) throws GameException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public boolean isActive(boolean placingBlackToken) {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public boolean hasNeighbour(OthelloDirection direction) {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public OthelloFieldState getState() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public OthelloPosition getPosition() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public OthelloField getNeighbour(OthelloDirection direction) throws IllegalArgumentException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Set<? extends OthelloField> getLineOfTokens(OthelloDirection direction, OthelloFieldState delimiterState) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public OthelloBoard getBoard() {
                // TODO Auto-generated method stub
                return null;
            }
        }));
        OthelloField workfield = null;

        for (int i = 0; i < activeFields.size(); i++) {
            this.workboard = board.deepCopy();
            workfield = this.setup(this.workboard, usingBlackTokens).get(i);
            workfield.placeToken(usingBlackTokens);
            rootpos.addChild(new Node<>(new FieldIntTuple(this.evaluateBoard(workfield.getBoard()), workfield)));
        }

        return rootpos;
    }

    /**
     * Builds tree of a specified depth with the board you give it as root and all possible boards that could arise
     * through legal moves within the specified depth as children.
     *
     * @param board            board to use as the root of the Tree
     * @param usingBlackTokens if it is black to move in the root board
     * @param activeFields     list of active fields in the root board
     * @param depth            how far to look into the future
     * @return
     * @throws GameException
     */
    private Node<FieldIntTuple> buildTree(final OthelloBoard board, final boolean usingBlackTokens,
            final List<OthelloField> activeFields, final Integer depth) throws GameException {
        final Node<FieldIntTuple> outTree = this.growTree(board, usingBlackTokens, activeFields);
        boolean currentUsingBlackTokens = usingBlackTokens;
        for (int i = 0; i < depth; i++) {
            currentUsingBlackTokens = !currentUsingBlackTokens;
            for (final Node<FieldIntTuple> node : outTree.getChildren()) {
                node.addChild(
                        this.growTree(
                                node.getData().getField().getBoard(),
                                currentUsingBlackTokens,
                                this.setup(node.getData().getField().getBoard(), currentUsingBlackTokens)));
            }

        }
        return outTree;
    }

    /**
     * Assigns an Integer Evaluation to a board. Values higher than 0 are good for black,lower values are good for
     * white.
     *
     * @param board board to be evaluated
     * @return
     */
    private Integer evaluateBoard(final OthelloBoard board) {
        return board.getFieldsBeing(OthelloFieldState.BLACK).size()
                - board.getFieldsBeing(OthelloFieldState.WHITE).size();
    }

    private Node<FieldIntTuple> shrinkTree(final Node<FieldIntTuple> rootnode, final boolean usingBlackTokens) {
        boolean currentUsingBlackTokens = usingBlackTokens;
        final List<List<Node<FieldIntTuple>>> lNodes = rootnode.getOrganizedLowestLayer();
        for (int i = 0; i < lNodes.size(); i++) {
            lNodes.get(i).get(0).getParent().
            setData(new FieldIntTuple(this.compare(lNodes.get(i), currentUsingBlackTokens).getValue(),
                    lNodes.get(i).get(0).getParent().getData().getField()));
            for (int j = 0; j < lNodes.get(i).size(); j++) {
                lNodes.get(i).get(j).deleteNode();
            }
        }
//        for (final List<Node<FieldIntTuple>> group : rootnode.getOrganizedLowestLayer()) {
//            currentUsingBlackTokens = !currentUsingBlackTokens;
//            group.get(0).getParent().setData(this.compare(group, currentUsingBlackTokens));
//            for (final Node<FieldIntTuple> node : group) {
//                node.deleteNode();
//
//            }
////            group.parallelStream().forEach(s -> {
////                s.deleteNode();
////            });
//        }
        return rootnode;
    }

    private FieldIntTuple compare(final List<Node<FieldIntTuple>> group, final boolean usingBlackTokens) {
        if (usingBlackTokens) {
            return group.parallelStream().max((i, j) -> i.getData().getValue().compareTo(j.getData().getValue())).get()
                    .getData();
        } else {
            return group.parallelStream().min((i, j) -> i.getData().getValue().compareTo(j.getData().getValue())).get()
                    .getData();
        }
    }
    
    private OthelloPosition crushTree(Node<FieldIntTuple> rootnode,boolean usingBlackTokens,Integer depth) {
        boolean currentUsingBlackTokens = usingBlackTokens;
        Node<FieldIntTuple> currentrootnode = rootnode;
        for (int i = 0; i < depth - 1; i++) {
            currentUsingBlackTokens=!currentUsingBlackTokens;
            currentrootnode=shrinkTree(currentrootnode, currentUsingBlackTokens);
            
        }        
        return compare(currentrootnode.getOrganizedLowestLayer().get(0), currentUsingBlackTokens).getField().getPosition();
    }

}
