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
package de.fhdw.gaming.othello.strategy.examples.MinMaxCombi3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
public final class OthelloMinMaxCombi3Strategy implements OthelloStrategy {

    /**
     * How many layers the search tree will have.
     */
    private Integer depthOfTree = 3;

    /**
     * provides the strategy with information on how many moves have been played yet. (decreases by 1 with every call of
     * computeNextMove)
     */
    private Integer temperature = 32;
    /**
     * Integer denoting how valuable a token on a stable Field (as defined by isFieldStable) is.
     *
     */
    private Integer stableWorth = 3;
    /**
     * A coefficient used to emphasize certain aspects of the evaluation. is changed according to temperature
     */
    private int fieldCoefficient = 1;
    /**
     * maps a value to every field of a 8x8 OthelloBoard. is used for board evaluation
     */
    private static HashMap<OthelloPosition, Integer> boardWeights = initializeBoardWeigths();

    /**
     * The factory for creating Othello moves.
     */
    private final OthelloMoveFactory moveFactory;

    /**
     * Creates an {@link OthelloMyStrategy}.
     *
     * @param moveFactory The factory for creating Othello moves.
     */
    OthelloMinMaxCombi3Strategy(final OthelloMoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    @Override
    /**
     * resets the fields of the Strategy. Called before every game in a contest.
     */
    public void reset() {
        this.depthOfTree = 3;
        this.temperature = 32;
        this.stableWorth = 3;
        this.fieldCoefficient = 1;
        OthelloMinMaxCombi3Strategy.boardWeights = initializeBoardWeigths();
    }

    /**
     * Initializes the HashMap used for evaluating the OthelloBoard.
     *
     * @return HashMap im Initialzustand
     */
    private static HashMap<OthelloPosition, Integer> initializeBoardWeigths() {
        return new HashMap<>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                this.put(OthelloPosition.of(0, 0), 100);
                this.put(OthelloPosition.of(0, 1), -30);
                this.put(OthelloPosition.of(0, 2), 6);
                this.put(OthelloPosition.of(0, 3), 2);
                this.put(OthelloPosition.of(0, 4), 2);
                this.put(OthelloPosition.of(0, 5), 6);
                this.put(OthelloPosition.of(0, 6), -30);
                this.put(OthelloPosition.of(0, 7), 100);
                this.put(OthelloPosition.of(1, 0), -30);
                this.put(OthelloPosition.of(1, 1), -50);
                this.put(OthelloPosition.of(1, 2), 0);
                this.put(OthelloPosition.of(1, 3), 0);
                this.put(OthelloPosition.of(1, 4), 0);
                this.put(OthelloPosition.of(1, 5), 0);
                this.put(OthelloPosition.of(1, 6), -50);
                this.put(OthelloPosition.of(1, 7), -30);
                this.put(OthelloPosition.of(2, 0), 6);
                this.put(OthelloPosition.of(2, 1), 0);
                this.put(OthelloPosition.of(2, 2), 0);
                this.put(OthelloPosition.of(2, 3), 0);
                this.put(OthelloPosition.of(2, 4), 0);
                this.put(OthelloPosition.of(2, 5), 0);
                this.put(OthelloPosition.of(2, 6), 0);
                this.put(OthelloPosition.of(2, 7), 6);
                this.put(OthelloPosition.of(3, 0), 2);
                this.put(OthelloPosition.of(3, 1), 0);
                this.put(OthelloPosition.of(3, 2), 0);
                this.put(OthelloPosition.of(3, 3), 3);
                this.put(OthelloPosition.of(3, 4), 3);
                this.put(OthelloPosition.of(3, 5), 0);
                this.put(OthelloPosition.of(3, 6), 0);
                this.put(OthelloPosition.of(3, 7), 2);
                this.put(OthelloPosition.of(4, 0), 2);
                this.put(OthelloPosition.of(4, 1), 0);
                this.put(OthelloPosition.of(4, 2), 0);
                this.put(OthelloPosition.of(4, 3), 3);
                this.put(OthelloPosition.of(4, 4), 3);
                this.put(OthelloPosition.of(4, 5), 0);
                this.put(OthelloPosition.of(4, 6), 0);
                this.put(OthelloPosition.of(4, 7), 2);
                this.put(OthelloPosition.of(5, 0), 6);
                this.put(OthelloPosition.of(5, 1), 0);
                this.put(OthelloPosition.of(5, 2), 0);
                this.put(OthelloPosition.of(5, 3), 0);
                this.put(OthelloPosition.of(5, 4), 0);
                this.put(OthelloPosition.of(5, 5), 0);
                this.put(OthelloPosition.of(5, 6), 0);
                this.put(OthelloPosition.of(5, 7), 6);
                this.put(OthelloPosition.of(6, 0), -30);
                this.put(OthelloPosition.of(6, 1), -50);
                this.put(OthelloPosition.of(6, 2), 0);
                this.put(OthelloPosition.of(6, 3), 0);
                this.put(OthelloPosition.of(6, 4), 0);
                this.put(OthelloPosition.of(6, 5), 0);
                this.put(OthelloPosition.of(6, 6), -50);
                this.put(OthelloPosition.of(6, 7), -30);
                this.put(OthelloPosition.of(7, 0), 100);
                this.put(OthelloPosition.of(7, 1), -30);
                this.put(OthelloPosition.of(7, 2), 6);
                this.put(OthelloPosition.of(7, 3), 2);
                this.put(OthelloPosition.of(7, 4), 2);
                this.put(OthelloPosition.of(7, 5), 6);
                this.put(OthelloPosition.of(7, 6), -30);
                this.put(OthelloPosition.of(7, 7), 100);
            }
        };
    }

    @Override
    public Optional<OthelloMove> computeNextMove(final int gameId, final OthelloPlayer player, final OthelloState state)
            throws GameException {
        int ownCorners = 0;
        this.stableWorth = 3;
        if (this.temperature < 10) {
            this.depthOfTree = 5;
            this.stableWorth = 0;

        }
        if (this.temperature < 5) {
            this.fieldCoefficient = 2;

        }

        this.temperature -= 1;
        final boolean usingBlackTokens = player.isUsingBlackTokens();

        Set<OthelloPosition> ownFields = new HashSet<>();
        final List<OthelloField> activeFields = this.setup(state.getBoard(), usingBlackTokens);
        if (usingBlackTokens) {
            ownFields = state.getBoard().getFieldsBeing(OthelloFieldState.BLACK).keySet();
        } else {
            ownFields = state.getBoard().getFieldsBeing(OthelloFieldState.WHITE).keySet();

        }

        ownCorners = this.checkOwnCorners(ownFields);
        this.stableWorth += ownCorners * 6;

        // The Othello game forces a player to skip a move if no valid move is possible. So you should check for this
        // situation first.
        if (activeFields.isEmpty()) {
            return Optional.of(this.moveFactory.createSkipMove(usingBlackTokens));
        }

        final FieldIntTuple besttuple = this.minmax(state, usingBlackTokens, activeFields, this.depthOfTree);

//        System.out.println(besttuple.getValue()); //prints how good the Strategy thinks the current board is.

        final OthelloPosition bestposition = besttuple.getField().getPosition();
//        final OthelloPosition bestposition = this.calculate(activeFields, usingBlackTokens, state);
        final Optional<OthelloMove> output = Optional
                .of(this.moveFactory.createPlaceTokenMove(usingBlackTokens, bestposition));
        return output;
    }

    @Override
    public String toString() {
        return OthelloMinMaxCombi3Strategy.class.getSimpleName();
    }

    /**
     * Generates a list of active fields, which is neccassary for almost all Strategies.
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

        // ...and then you are able to filter all active fields for this token color. Only on these fields is it
        // possible to place a suitable token according to the rules of the game.
        // (Of course you can use a traditional for-each loop instead of streams for filtering.)
        final List<OthelloField> activeFields = new ArrayList<>();
        emptyFields.values().stream().filter((final OthelloField field) -> field.isActive(usingBlackTokens))
                .forEachOrdered(activeFields::add);
        return activeFields;
    }

    /**
     * Builds a one layer deep tree with the board you give it as root and all possible boards one move into the future
     * as children.
     *
     *
     * @param field
     * @param usingBlackTokens
     * @param activeFields
     * @return
     * @throws GameException
     */

    private Node<FieldIntTuple> growTree(final OthelloField field, final boolean usingBlackTokens,
            final List<OthelloField> activeFields) throws GameException {
        final Node<FieldIntTuple> rootpos = new Node<>(new FieldIntTuple(0, field));
        OthelloField workfield = field;
        if (activeFields.isEmpty()) {
            rootpos.addChild(new Node<>(new FieldIntTuple(0, field)));
        }

        for (int i = 0; i < activeFields.size(); i++) {
            final OthelloBoard workboard = field.getBoard().deepCopy();
            workfield = this.setup(workboard, usingBlackTokens).get(i);

            workfield.placeToken(usingBlackTokens);
            rootpos.addChild(new Node<>(new FieldIntTuple(0, workfield)));
        }

        return rootpos;
    }

    /**
     * Evaluates the lowest layer of a given tree using evaluateBoard, and assigns the values to the nodes in the lowest
     * layer.
     *
     * @param rootnode
     * @return
     */
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
            for (final Node<FieldIntTuple> node : outTree.getLowestLayer()) {
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

        final Set<OthelloPosition> blackFields = board.getFieldsBeing(OthelloFieldState.BLACK).keySet();
        final Set<OthelloPosition> whiteFields = board.getFieldsBeing(OthelloFieldState.WHITE).keySet();

        final int blackActiveFieldsNum = this.setup(board, true).size();
        final int whiteActiveFieldsNum = this.setup(board, false).size();
        int blackFieldsNum = blackFields.size();
        int whiteFieldsNum = whiteFields.size();

        if (this.isWinning(true, blackActiveFieldsNum, whiteActiveFieldsNum, blackFieldsNum, whiteFieldsNum)) {
            return 1000000;
        }
        if (this.isWinning(false, blackActiveFieldsNum, whiteActiveFieldsNum, blackFieldsNum, whiteFieldsNum)) {
            return -1000000;
        } else {
            for (final OthelloPosition blackPosition : blackFields) {
                if (this.isFieldStable(board.getFieldAt(blackPosition))) {
                    blackFieldsNum += this.stableWorth;
                }
                blackFieldsNum += OthelloMinMaxCombi3Strategy.boardWeights.get(blackPosition);
                blackFieldsNum -= this.getFieldPenalty(board.getFieldAt(blackPosition));

            }
            for (final OthelloPosition whitePosition : whiteFields) {
                if (this.isFieldStable(board.getFieldAt(whitePosition))) {
                    whiteFieldsNum += this.stableWorth;
                }
                whiteFieldsNum += OthelloMinMaxCombi3Strategy.boardWeights.get(whitePosition);
                whiteFieldsNum -= this.getFieldPenalty(board.getFieldAt(whitePosition));

            }
            return ((blackFieldsNum * this.fieldCoefficient + blackActiveFieldsNum)
                    - (whiteFieldsNum * this.fieldCoefficient + whiteActiveFieldsNum));
        }

    }

    /**
     * Removes the lowest layer and propagates its values up the tree according to minmax. Used by crushTree.
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
     * Evaluates which move should be made according to minmax for a given GameTree. Iterative approach with worse
     * performance, only exists for debugging purposes.
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

    /**
     * Builds a Tree representing the current board and all boards reachable by playing legal moves (up to the specified
     * depth) and then extracts the best move according to minmax search.
     *
     * @param state
     * @param usingBlackTokens
     * @param activeFields
     * @param depth
     * @return
     * @throws GameException
     */
    private FieldIntTuple minmax(final OthelloState state, final boolean usingBlackTokens,
            final List<OthelloField> activeFields, final Integer depth) throws GameException {

        // enterypoint for using the recursive approach with alpha beta pruning
        return this.reccruschTree(
                this.evaluateLowestLayer(this.buildTree(state.getBoard(), usingBlackTokens, activeFields, depth)),
                usingBlackTokens,
                depth + 1,
                -100000,
                100000);

        // enterypoint for using the iterative approach without alpha beta pruning.
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

    /**
     * Defines if a field is stable, by checking if for every direction in which the given field has has a neighbor
     * there is a uninterrupted line of own tokens to the edge of the board on the other side.
     *
     * @param field
     * @return
     */
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
                    default:
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

    /**
     * Recursively calculates the best move of a GameTree. (implements alpha beta pruning in contrast to the iterative
     * implementation)
     *
     * @param rootnode
     * @param usingBlackTokens
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    private FieldIntTuple reccruschTree(final Node<FieldIntTuple> rootnode, final boolean usingBlackTokens,
            final Integer depth, final Integer alpha, final Integer beta) {
        Integer localAlpha = alpha;
        Integer localBeta = beta;
        if (depth <= 0 || rootnode.getChildren().isEmpty()) {
            return rootnode.getData();
        }

        if (usingBlackTokens) {
            FieldIntTuple bestNode = new FieldIntTuple(-10000000, rootnode.getData().getField());
            for (final Node<FieldIntTuple> child : rootnode.getChildren()) {
                final FieldIntTuple currEval = new FieldIntTuple(0, child.getData().getField());
                currEval.setValue(this.reccruschTree(child, false, depth - 1, localAlpha, localBeta).getValue());
                if (bestNode.getValue() >= currEval.getValue()) {
                    // do nothing
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
            FieldIntTuple bestNode = new FieldIntTuple(10000000, rootnode.getData().getField());
            for (final Node<FieldIntTuple> child : rootnode.getChildren()) {
                final FieldIntTuple currEval = new FieldIntTuple(0, child.getData().getField());
                currEval.setValue(this.reccruschTree(child, true, depth - 1, localAlpha, localBeta).getValue());

                if (bestNode.getValue() <= currEval.getValue()) {
                    // do nothing
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

    /**
     * True if the given parameters describe a wining board for the given player. (else this method is false)
     *
     * @param usingBlackTokens
     * @param blackActiveFieldsNum
     * @param whiteActiveFieldsNum
     * @param blackFieldsNum
     * @param whiteFieldsNum
     * @return
     */
    private boolean isWinning(final boolean usingBlackTokens, final int blackActiveFieldsNum,
            final Integer whiteActiveFieldsNum, final int blackFieldsNum, final int whiteFieldsNum) {
        if (blackActiveFieldsNum == 0 && whiteActiveFieldsNum == 0) {
            if (usingBlackTokens) {
                return blackFieldsNum > whiteFieldsNum;
            } else {
                return whiteFieldsNum > blackFieldsNum;
            }
        } else {
            return false;
        }
    }

    /**
     * Calculates the evaluation penalty for a field by counting adjacent empty fields. (this penalty represents the
     * danger of the token being fliped.
     *
     * @param field
     * @return
     */
    private int getFieldPenalty(final OthelloField field) {
        int penalty = 0;
        for (final OthelloDirection direction : OthelloDirection.values()) {
            if (field.hasNeighbour(direction)) {
                if (field.getNeighbour(direction).getState().equals(OthelloFieldState.EMPTY)) {
                    penalty += 1;
                }

            }
        }
        return penalty;
    }

    /**
     * Updates the map according to the corners occupied by the Strategy and returns the number of corners owned.
     *
     * @param ownFields
     * @return
     */
    private int checkOwnCorners(final Set<OthelloPosition> ownFields) {
        int ownCorners = 0;
        for (final OthelloPosition ownPosition : ownFields) {

            if (ownPosition.equals(OthelloPosition.of(0, 0))) {
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(0, 1), 0);
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(1, 0), 0);
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(1, 1), 0);
                ownCorners += 1;
            }
            if (ownPosition.equals(OthelloPosition.of(0, 7))) {
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(0, 6), 0);
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(1, 7), 0);
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(1, 6), 0);
                ownCorners += 1;

            }
            if (ownPosition.equals(OthelloPosition.of(7, 0))) {
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(6, 0), 0);
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(7, 1), 0);
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(1, 6), 0);
                ownCorners += 1;

            }
            if (ownPosition.equals(OthelloPosition.of(7, 7))) {
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(7, 6), 0);
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(6, 7), 0);
                OthelloMinMaxCombi3Strategy.boardWeights.replace(OthelloPosition.of(6, 6), 0);
                ownCorners += 1;
            }
        }
        return ownCorners;
    }

}
