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
public final class OthelloMinMaxStrategy implements OthelloStrategy {

    /**
     * The factory for creating Othello moves.
     */
    private final OthelloMoveFactory moveFactory;

    /**
     * Creates an {@link OthelloMyStrategy}.
     *
     * @param moveFactory The factory for creating Othello moves.
     */
    OthelloMinMaxStrategy(final OthelloMoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    /**
     *
     */
    private final Integer DEPTHOFTREE = 3;

    /**
     * place to put the state before modifying it for computations.
     */
    private OthelloBoard workboard = null;

    private Integer Temperature = 32;
    private Integer StableWorth = 7;
    private boolean ActiveFieldEvaluation = true;
    private final Integer badFieldPenalty = 0;

    // define a method to actually find winning positions
    // penalize putting tokens on the fields around the corners in the fist moves

    @Override
    public Optional<OthelloMove> computeNextMove(final int gameId, final OthelloPlayer player, final OthelloState state)
            throws GameException {

        if (this.Temperature < 10) {
            this.StableWorth = 3;
        }
        if (this.Temperature < 23) {
            this.ActiveFieldEvaluation = false;
        }
        this.Temperature -= 1;
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

//        final long startTime = System.nanoTime();
        final FieldIntTuple besttuple = this.minmax(state, usingBlackTokens, activeFields, this.DEPTHOFTREE);
//        final long endTime = System.nanoTime();
//        final long duration = (endTime - startTime);
//        System.out.println(duration / 1000000000);// division to get Seconds

//                this
//                .crushTree(this.buildTree(state.getBoard(), usingBlackTokens, activeFields, 4), usingBlackTokens, 4);

//        System.out.println(besttuple.getValue());
//        System.out.println(this.Temperature);

        final OthelloPosition bestposition = besttuple.getField().getPosition();
//        final OthelloPosition bestposition = this.calculate(activeFields, usingBlackTokens, state);
        return Optional.of(this.moveFactory.createPlaceTokenMove(usingBlackTokens, bestposition));
    }

    @Override
    public String toString() {
        return OthelloMinMaxStrategy.class.getSimpleName();
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
     * Builds a one layer deep tree with the board you give it as root and all possible boards one move into the future
     * as children.
     *
     * Dosen't understand skipmoves correctly , the current if else is just a temporary fix
     *
     * @param board
     * @param usingBlackTokens
     * @param activeFields
     * @return
     * @throws GameException
     */

    private Node<FieldIntTuple> growTree(final OthelloField field, final boolean usingBlackTokens,
            final List<OthelloField> activeFields) throws GameException {
        final Node<FieldIntTuple> rootpos = new Node<>(new FieldIntTuple(0, field));
        OthelloField workfield = null;
        if (activeFields.isEmpty()) {
            rootpos.addChild(new Node<>(new FieldIntTuple(0, field)));
        }

        for (int i = 0; i < activeFields.size(); i++) {
            this.workboard = field.getBoard().deepCopy();
            workfield = this.setup(this.workboard, usingBlackTokens).get(i);

            workfield.placeToken(usingBlackTokens);
            rootpos.addChild(new Node<>(new FieldIntTuple(0, workfield)));
        }

        return rootpos;
    }

    private Node<FieldIntTuple> evaluateLowestLayer(final Node<FieldIntTuple> rootnode) {
        rootnode.getLowestLayer().parallelStream()
                .forEach(node -> node.getData().setValue(this.evaluateBoard(node.getData().getField().getBoard())));
        return rootnode;
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
        final Node<
                FieldIntTuple> outTree = this.growTree(board.getFields().get(0).get(0), usingBlackTokens, activeFields);
        boolean currentUsingBlackTokens = usingBlackTokens;
        for (int i = 0; i < depth; i++) {
            currentUsingBlackTokens = !currentUsingBlackTokens;
            for (final Node<FieldIntTuple> node : outTree.getLowestLayer()) { // sadly i don't think i can replace this
                                                                              // for loop
                                                                              // with a parallel stream because it uses
                                                                              // an outside
                                                                              // variable that is not final. but perhaps
                                                                              // this could
                                                                              // be circumvented by having current using
                                                                              // black tokens
                                                                              // be a final list of alternating true and
                                                                              // false booleans (as many as depth is)
                node.addChildren(
                        this.growTree(
                                node.getData().getField(),
                                currentUsingBlackTokens,
                                this.setup(node.getData().getField().getBoard(), currentUsingBlackTokens))
                                .getChildren());
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

        Integer BlackActiveFieldsNum = null;
        Integer BlackFieldsNum = null;

        Integer WhiteActiveFieldsNum = null;
        Integer WhiteFieldsNum = null;

        Integer EvalNum = null;

        final Set<OthelloPosition> BlackFields = board.getFieldsBeing(OthelloFieldState.BLACK).keySet();
        final Set<OthelloPosition> WhiteFields = board.getFieldsBeing(OthelloFieldState.WHITE).keySet();

        BlackFieldsNum = BlackFields.size();
        WhiteFieldsNum = WhiteFields.size();

        BlackActiveFieldsNum = this.setup(board, true).size();
        WhiteActiveFieldsNum = this.setup(board, false).size();

        if (this.ActiveFieldEvaluation) {

//            if (BlackFields.contains(OthelloPosition.of(0, 1))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(1, 0))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(1, 1))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(0, 7))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(1, 7))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(1, 8))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(7, 7))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(7, 8))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(8, 7))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(7, 0))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(7, 1))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (BlackFields.contains(OthelloPosition.of(8, 1))) {
//                BlackActiveFieldsNum -= this.badFieldPenalty;
//            }
//
//            if (WhiteFields.contains(OthelloPosition.of(0, 1))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(1, 0))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(1, 1))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(0, 7))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(1, 7))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(1, 8))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(7, 7))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(7, 8))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(8, 7))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(7, 0))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(7, 1))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }
//            if (WhiteFields.contains(OthelloPosition.of(8, 1))) {
//                WhiteActiveFieldsNum -= this.badFieldPenalty;
//            }

            EvalNum = BlackActiveFieldsNum - WhiteActiveFieldsNum;

        } else {
            EvalNum = BlackFieldsNum - WhiteFieldsNum;
        }

        for (final OthelloPosition BlackPosition : BlackFields) {
            if (this.isFieldStable(board.getFieldAt(BlackPosition))) {
                EvalNum += this.StableWorth;
            }
        }
        for (final OthelloPosition WhitePosition : WhiteFields) {
            if (this.isFieldStable(board.getFieldAt(WhitePosition))) {
                EvalNum -= this.StableWorth;
            }
        }
//        if (BlackActiveFieldsNum.equals(0)) {
//            return -1000;
//        }
//        if (WhiteActiveFieldsNum.equals(0)) {
//            return 1000;
        if (this.isWinning(true, BlackActiveFieldsNum, WhiteActiveFieldsNum, BlackFieldsNum, WhiteFieldsNum)) {
            return 1000000;
        }
        if (this.isWinning(false, BlackActiveFieldsNum, WhiteActiveFieldsNum, BlackFieldsNum, WhiteFieldsNum)) {
            return -1000000;
        } else {
            return EvalNum;
        }

    }

    /**
     * Should remove the lowest layer and propagate its values up the tree acording to minmax. probably sometimes throws
     * "index 0 out of range for length 0" because compare sometimes gets passed an empty List as group
     *
     * @param rootnode
     * @param usingBlackTokens
     * @return
     * @throws Exception
     */
    private Node<FieldIntTuple> shrinkTree(final Node<FieldIntTuple> rootnode, final boolean usingBlackTokens) {
        final boolean currentUsingBlackTokens = usingBlackTokens;
        final List<List<Node<FieldIntTuple>>> lNodes = rootnode.getOrganizedLowestLayer();

        for (int i = 0; i < lNodes.size(); i++) {
            if (lNodes.get(i).isEmpty()) {
                continue;
            }

            lNodes.get(i).get(0).getParent().setData(
                    new FieldIntTuple(
                            this.compare(lNodes.get(i), currentUsingBlackTokens).getValue(),
                            lNodes.get(i).get(0).getParent().getData().getField()));
            for (int j = 0; j < lNodes.get(i).size(); j++) {
                lNodes.get(i).get(j).deleteNode();
            }
        }
        return rootnode;
    }

    /**
     * Returns the minimum or the maximum value FieldIntTuple out of the List it is provided. minimum if
     * usingBlackTokens is false maximum if usingBlackTokens is true
     *
     * @param group
     * @param usingBlackTokens
     * @return
     * @throws Exception
     */
    private FieldIntTuple compare(final List<Node<FieldIntTuple>> group, final boolean usingBlackTokens) {
        if (usingBlackTokens) {
            return group.parallelStream().max((i, j) -> i.getData().getValue().compareTo(j.getData().getValue())).get()
                    .getData();
        } else {
            return group.parallelStream().min((i, j) -> i.getData().getValue().compareTo(j.getData().getValue())).get()
                    .getData();
        }
    }

    /**
     * Evaluates which move should be made according to minmax.
     *
     * @param rootnode
     * @param usingBlackTokens
     * @param depth
     * @return
     * @throws Exception
     */
    private FieldIntTuple crushTree(final Node<FieldIntTuple> rootnode, final boolean usingBlackTokens,
            final Integer depth) {
        boolean currentUsingBlackTokens = usingBlackTokens;
        Node<FieldIntTuple> currentrootnode = rootnode;
        for (int i = 0; i < depth; i++) {
            currentrootnode = this.shrinkTree(currentrootnode, currentUsingBlackTokens);
            currentUsingBlackTokens = !currentUsingBlackTokens;

        }
//        return currentrootnode.getLowestLayer();
        return this.compare(currentrootnode.getChildren(), currentUsingBlackTokens);
    }

    private FieldIntTuple minmax(final OthelloState state, final boolean usingBlackTokens,
            final List<OthelloField> activeFields, final Integer depth) throws GameException {

        return this.reccruschTree(
                this.evaluateLowestLayer(this.buildTree(state.getBoard(), usingBlackTokens, activeFields, depth)),
                usingBlackTokens,
                depth + 1,
                -100000,
                100000);
//        if (depth % 2 == 0) {
//            return this.crushTree(
//                    this.evaluateLowestLayer(this.buildTree(state.getBoard(), usingBlackTokens, activeFields, depth)),
//                    usingBlackTokens,
//                    depth);
//        } else {
//            return this.crushTree(
//                    this.evaluateLowestLayer(this.buildTree(state.getBoard(), usingBlackTokens, activeFields, depth)),
//                    !usingBlackTokens,
//                    depth);
//        }
    }

    private boolean isFieldStable(final OthelloField field) {
        final List<OthelloDirection> opposingSides = new ArrayList<>();
        OthelloField workfield = field;
        for (final OthelloDirection direction : OthelloDirection.values()) {
            if (workfield.hasNeighbour(direction)) {
                if (!workfield.getNeighbour(direction).getState().equals(field.getState())) {
                    switch (direction) {
                    case NORTH:
                        opposingSides.add(OthelloDirection.SOUTH);
                        break;
                    case NORTHEAST:
                        opposingSides.add(OthelloDirection.SOUTHWEST);
                        break;
                    case EAST:
                        opposingSides.add(OthelloDirection.WEST);
                        break;
                    case SOUTHEAST:
                        opposingSides.add(OthelloDirection.NORTHWEST);
                        break;
                    case SOUTH:
                        opposingSides.add(OthelloDirection.NORTH);
                        break;
                    case SOUTHWEST:
                        opposingSides.add(OthelloDirection.NORTHEAST);
                        break;
                    case WEST:
                        opposingSides.add(OthelloDirection.EAST);
                        break;
                    case NORTHWEST:
                        opposingSides.add(OthelloDirection.SOUTHEAST);
                        break;
                    }
                }
            }

        }
        for (final OthelloDirection othelloDirection : opposingSides) {
            while (workfield.hasNeighbour(othelloDirection)) {
                if (workfield.getNeighbour(othelloDirection).getState().equals(workfield.getState())) {
                    workfield = workfield.getNeighbour(othelloDirection);
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    private FieldIntTuple reccruschTree(final Node<FieldIntTuple> rootnode, final boolean usingBlackTokens,
            final Integer depth, final Integer alpha, final Integer beta) {
        Integer localAlpha = alpha;
        Integer localBeta = beta;
        if (depth <= 0) {
            return rootnode.getData();
        }

        if (usingBlackTokens) {
            FieldIntTuple bestNode = new FieldIntTuple(-10000, rootnode.getData().getField());
            for (final Node<FieldIntTuple> child : rootnode.getChildren()) {
                final FieldIntTuple currEval = new FieldIntTuple(0, child.getData().getField());
                currEval.setValue(this.reccruschTree(child, false, depth - 1, localAlpha, localBeta).getValue());
                if (bestNode.getValue() >= currEval.getValue()) {
                    bestNode = bestNode;
                } else {
                    bestNode = currEval;
                }
                localAlpha = Math.max(localAlpha, currEval.getValue());
                if (localBeta <= localAlpha) {
                    break;
                }

            }
            return bestNode;
        } else {
            FieldIntTuple bestNode = new FieldIntTuple(10000, rootnode.getData().getField());
            for (final Node<FieldIntTuple> child : rootnode.getChildren()) {
                final FieldIntTuple currEval = new FieldIntTuple(0, child.getData().getField());
                currEval.setValue(this.reccruschTree(child, true, depth - 1, localAlpha, localBeta).getValue());

                if (bestNode.getValue() <= currEval.getValue()) {
                    bestNode = bestNode;
                } else {
                    bestNode = currEval;
                }
                localBeta = Math.min(localBeta, currEval.getValue());
                if (localBeta <= localAlpha) {
                    break;
                }

            }
            return bestNode;
        }
    }

    private boolean isWinning(final boolean usingBlackTokens, final Integer BlackActiveFieldsNum,
            final Integer WhiteActiveFieldsNum, final Integer BlackFieldsNum, final Integer WhiteFieldsNum) {
        if (BlackActiveFieldsNum.equals(0) && WhiteActiveFieldsNum.equals(0)) {
            if (usingBlackTokens) {
                return BlackFieldsNum > WhiteFieldsNum;
            } else {
                return WhiteFieldsNum > BlackFieldsNum;
            }
        } else {
            return false;
        }
    }

}
