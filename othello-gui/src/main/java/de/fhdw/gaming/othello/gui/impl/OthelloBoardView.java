/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-gui.
 *
 * Othello-gui is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Othello-gui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * othello-gui. If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.othello.gui.impl;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import de.fhdw.gaming.core.domain.Game;
import de.fhdw.gaming.core.domain.Move;
import de.fhdw.gaming.core.domain.Player;
import de.fhdw.gaming.core.domain.PlayerState;
import de.fhdw.gaming.core.domain.State;
import de.fhdw.gaming.othello.core.domain.OthelloBoard;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloGame;
import de.fhdw.gaming.othello.core.domain.OthelloObserver;
import de.fhdw.gaming.othello.core.domain.OthelloPlayer;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;
import de.fhdw.gaming.othello.core.domain.OthelloState;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 * Displays an Othello board.
 */
@SuppressWarnings("PMD.TooManyFields")
final class OthelloBoardView implements OthelloObserver {

    /**
     * The initial delay in seconds.
     */
    private static final double INITIAL_DELAY = 0.5;
    /**
     * The margin used by elements of the state pane.
     */
    private static final double STATE_PANE_MARGIN = 40.0;
    /**
     * Pattern for extracting the relevant parts of a strategy name.
     */
    private static final Pattern STRATEGY_NAME_PATTERN = Pattern.compile("^(Othello)?(.*?)(Strategy)?$");

    /**
     * The factor to multiply pixels with to receive points.
     */
    private final double pixelsToPointsFactor;
    /**
     * The field controls.
     */
    private final Map<OthelloPosition, OthelloFieldView> controls;
    /**
     * The pane containing everything.
     */
    private final HBox rootPane;
    /**
     * The pane containing the board and its border.
     */
    private final VBox boardPane;
    /**
     * The grid pane containing the fields.
     */
    private final GridPane fieldPane;
    /**
     * The label containing the number of black tokens on the board.
     */
    private Label blackTokens;
    /**
     * The label containing the number of white tokens on the board.
     */
    private Label whiteTokens;
    /**
     * The label describing the current game state.
     */
    private Label gameStateDescription;
    /**
     * The animation of the current game state description (if any).
     */
    private Optional<Timeline> gameStateDescriptionAnimation;
    /**
     * The delay in milliseconds.
     */
    private final AtomicInteger delay;
    /**
     * The size of a field control.
     */
    private final SimpleDoubleProperty fieldControlSize;
    /**
     * The size of various margins.
     */
    private final SimpleDoubleProperty margin;
    /**
     * The padding used for GridPanes.
     */
    private final SimpleObjectProperty<Insets> gridPadding;
    /**
     * The font size in pixels.
     */
    private final SimpleDoubleProperty fontSizeInPixels;
    /**
     * The font used for border labels.
     */
    private final SimpleObjectProperty<Font> borderLabelFont;
    /**
     * The font used for labels texts in the player's pane.
     */
    private final SimpleObjectProperty<Font> labelTextFont;
    /**
     * The font used for label values in the player's pane.
     */
    private final SimpleObjectProperty<Font> labelValueFont;
    /**
     * The font used for displaying the game result.
     */
    private final SimpleObjectProperty<Font> gameResultFont;
    /**
     * The pane for the top edge.
     */
    private HBox topEdge;
    /**
     * The pane for the left edge.
     */
    private VBox leftEdge;
    /**
     * The pane for the right edge.
     */
    private VBox rightEdge;
    /**
     * The pane for the bottom edge.
     */
    private HBox bottomEdge;
    /**
     * The last game state.
     */
    private Optional<OthelloState> lastGameState;
    /**
     * The {@link Semaphore} used by {@link OthelloBoardEventProviderImpl}.
     */
    private final Semaphore semaphore = new Semaphore(0);

    /**
     * Creates an {@link OthelloBoardView}.
     *
     * @param game The game.
     */
    OthelloBoardView(final OthelloGame game) {
        this.pixelsToPointsFactor = 72.0 / Screen.getPrimary().getDpi();
        this.borderLabelFont = new SimpleObjectProperty<>(Font.getDefault());
        this.labelTextFont = new SimpleObjectProperty<>(Font.getDefault());
        this.labelValueFont = new SimpleObjectProperty<>(Font.getDefault());
        this.gameResultFont = new SimpleObjectProperty<>(Font.getDefault());

        this.fieldControlSize = new SimpleDoubleProperty(0.0);
        this.margin = new SimpleDoubleProperty(0.0);
        this.gridPadding = new SimpleObjectProperty<>(Insets.EMPTY);
        this.fontSizeInPixels = new SimpleDoubleProperty(0.0);

        this.controls = new LinkedHashMap<>();
        this.fieldPane = new GridPane();
        this.boardPane = this.createFieldWithBorderPane();

        final GridPane statePane = this.createStatePane(game);

        this.rootPane = new HBox();
        this.rootPane.getChildren().addAll(this.boardPane, statePane);
        HBox.setHgrow(this.boardPane, Priority.ALWAYS);
        HBox.setHgrow(statePane, Priority.ALWAYS);
        HBox.setMargin(statePane, new Insets(OthelloBoardView.STATE_PANE_MARGIN));

        this.delay = new AtomicInteger((int) (OthelloBoardView.INITIAL_DELAY * 1000.0));
        this.lastGameState = Optional.empty();
        game.addObserver(this);

        this.gameStateDescriptionAnimation = Optional.empty();
    }

    /**
     * Creates the pane displaying the board and its border.
     * <p>
     * Requires {@link #fieldControlSize} to be valid.
     */
    private VBox createFieldWithBorderPane() {
        final Background background = new Background(
                new BackgroundFill(Color.SANDYBROWN, CornerRadii.EMPTY, Insets.EMPTY));

        final HBox topLeftCorner = new HBox();
        topLeftCorner.setBackground(background);
        topLeftCorner.minHeightProperty().bind(this.fieldControlSize.multiply(0.5));
        topLeftCorner.minWidthProperty().bind(this.fieldControlSize.multiply(0.5));
        this.topEdge = new HBox();
        this.topEdge.setBackground(background);
        this.topEdge.minHeightProperty().bind(this.fieldControlSize.multiply(0.5));
        this.topEdge.setAlignment(Pos.CENTER);
        final HBox topRightCorner = new HBox();
        topRightCorner.setBackground(background);
        topRightCorner.minHeightProperty().bind(this.fieldControlSize.multiply(0.5));
        topRightCorner.minWidthProperty().bind(this.fieldControlSize.multiply(0.5));

        HBox.setHgrow(topLeftCorner, Priority.NEVER);
        HBox.setHgrow(this.topEdge, Priority.NEVER);
        HBox.setHgrow(topRightCorner, Priority.NEVER);

        this.leftEdge = new VBox();
        this.leftEdge.setBackground(background);
        this.leftEdge.minWidthProperty().bind(this.fieldControlSize.multiply(0.5));
        this.leftEdge.setAlignment(Pos.CENTER);
        this.rightEdge = new VBox();
        this.rightEdge.setBackground(background);
        this.rightEdge.minWidthProperty().bind(this.fieldControlSize.multiply(0.5));
        this.rightEdge.setAlignment(Pos.CENTER);

        HBox.setHgrow(this.leftEdge, Priority.NEVER);
        HBox.setHgrow(this.fieldPane, Priority.NEVER);
        HBox.setHgrow(this.rightEdge, Priority.NEVER);

        final HBox bottomLeftCorner = new HBox();
        bottomLeftCorner.setBackground(background);
        bottomLeftCorner.minHeightProperty().bind(this.fieldControlSize.multiply(0.5));
        bottomLeftCorner.minWidthProperty().bind(this.fieldControlSize.multiply(0.5));
        this.bottomEdge = new HBox();
        this.bottomEdge.setBackground(background);
        this.bottomEdge.minHeightProperty().bind(this.fieldControlSize.multiply(0.5));
        this.bottomEdge.setAlignment(Pos.CENTER);
        final HBox bottomRightCorner = new HBox();
        bottomRightCorner.setBackground(background);
        bottomRightCorner.minHeightProperty().bind(this.fieldControlSize.multiply(0.5));
        bottomRightCorner.minWidthProperty().bind(this.fieldControlSize.multiply(0.5));

        HBox.setHgrow(bottomLeftCorner, Priority.NEVER);
        HBox.setHgrow(this.bottomEdge, Priority.NEVER);
        HBox.setHgrow(bottomRightCorner, Priority.NEVER);

        final HBox top = new HBox();
        top.getChildren().addAll(topLeftCorner, this.topEdge, topRightCorner);
        final HBox centre = new HBox();
        centre.getChildren().addAll(this.leftEdge, this.fieldPane, this.rightEdge);
        final HBox bottom = new HBox();
        bottom.getChildren().addAll(bottomLeftCorner, this.bottomEdge, bottomRightCorner);

        VBox.setVgrow(top, Priority.NEVER);
        VBox.setVgrow(centre, Priority.NEVER);
        VBox.setVgrow(bottom, Priority.NEVER);

        final VBox vbox = new VBox();
        vbox.getChildren().addAll(top, centre, bottom);

        return vbox;
    }

    /**
     * Creates the pane displaying the game state.
     *
     * @param game The game.
     */
    private GridPane createStatePane(final OthelloGame game) {
        final GridPane blackPlayerPane = this.createPlayerPane(game, game.getState().getBlackPlayer());
        final GridPane whitePlayerPane = this.createPlayerPane(game, game.getState().getWhitePlayer());

        // final Font font = Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, 40.0);
        this.gameStateDescription = new Label();
        this.gameStateDescription.fontProperty().bind(this.gameResultFont);
        this.gameStateDescription.setTextFill(Color.RED);

        final Label delayLabel = new Label("Delay in seconds: ");

        final Slider delaySlider = new Slider(0.0, 5.0, OthelloBoardView.INITIAL_DELAY);
        delaySlider.setMajorTickUnit(OthelloBoardView.INITIAL_DELAY);
        delaySlider.setMinorTickCount(4);
        delaySlider.setBlockIncrement(OthelloBoardView.INITIAL_DELAY);
        delaySlider.setShowTickMarks(true);
        delaySlider.setShowTickLabels(true);
        delaySlider.snapToTicksProperty().set(true);
        delaySlider.valueProperty().addListener(
                (final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue) -> {
                    this.delay.set((int) (newValue.doubleValue() * 1000.0));
                });

        final HBox delayBox = new HBox(delayLabel, delaySlider);
        HBox.setHgrow(delayLabel, Priority.NEVER);
        HBox.setHgrow(delaySlider, Priority.ALWAYS);

        final GridPane gridPane = new GridPane();
        gridPane.vgapProperty().bind(this.margin.multiply(2.0));

        gridPane.addRow(0, blackPlayerPane);
        GridPane.setHalignment(blackPlayerPane, HPos.CENTER);
        GridPane.setHgrow(blackPlayerPane, Priority.ALWAYS);
        GridPane.setVgrow(blackPlayerPane, Priority.NEVER);

        gridPane.addRow(1, whitePlayerPane);
        GridPane.setHalignment(whitePlayerPane, HPos.CENTER);
        GridPane.setHgrow(whitePlayerPane, Priority.ALWAYS);
        GridPane.setVgrow(whitePlayerPane, Priority.NEVER);

        gridPane.addRow(2, this.gameStateDescription);
        GridPane.setHalignment(this.gameStateDescription, HPos.CENTER);
        GridPane.setHgrow(this.gameStateDescription, Priority.ALWAYS);
        GridPane.setVgrow(this.gameStateDescription, Priority.NEVER);

        gridPane.addRow(3, delayBox);
        GridPane.setHalignment(delayBox, HPos.CENTER);
        GridPane.setHgrow(delayBox, Priority.ALWAYS);
        GridPane.setVgrow(delayBox, Priority.NEVER);

        return gridPane;
    }

    /**
     * Returns the root pane.
     */
    HBox getNode() {
        return this.rootPane;
    }

    /**
     * Returns the {@link Semaphore} used for user interaction.
     */
    Semaphore getUserInputSemaphore() {
        return this.semaphore;
    }

    /**
     * Maps a position to the corresponding {@link OthelloFieldView}.
     *
     * @param position The position.
     * @return The field view (if it exists).
     */
    Optional<OthelloFieldView> getFieldView(final OthelloPosition position) {
        return Optional.ofNullable(this.controls.get(position));
    }

    /**
     * Sets the game state.
     *
     * @param text The game state.
     */
    private void setGameState(final String text) {
        this.gameStateDescriptionAnimation.ifPresent((final Timeline timeline) -> timeline.stop());
        this.gameStateDescription.setText(text);
        if (!text.isEmpty()) {
            this.gameStateDescriptionAnimation = Optional.of(
                    new Timeline(
                            new KeyFrame(Duration.seconds(0.5), evt -> this.gameStateDescription.setVisible(false)),
                            new KeyFrame(Duration.seconds(1.0), evt -> this.gameStateDescription.setVisible(true))));
            this.gameStateDescriptionAnimation.get().setCycleCount(Animation.INDEFINITE);
            this.gameStateDescriptionAnimation.get().play();
        }
    }

    /**
     * Called if the game has been paused.
     *
     * @param game The game.
     */
    void gamePaused(final OthelloGame game) {
        Platform.runLater(() -> this.setGameState("P A U S E D"));
    }

    /**
     * Called if the game has been resumed.
     *
     * @param game The game.
     */
    void gameResumed(final OthelloGame game) {
        Platform.runLater(() -> this.setGameState(""));
    }

    @Override
    public void started(final Game<?, ?, ?, ?> game, final State<?, ?> state) throws InterruptedException {
        Platform.runLater(() -> {
            this.lastGameState = Optional.of((OthelloState) state);

            final OthelloBoard board = this.lastGameState.get().getBoard();
            final int numRowsAndColumns = board.getSize();

            this.addLabelsToEdge(numRowsAndColumns);

            this.boardPane.widthProperty().addListener(
                    (final ObservableValue<? extends Number> observable, final Number oldValue,
                            final Number newValue) -> {
                        final double size = Math.min(newValue.doubleValue(), this.boardPane.getHeight());
                        this.setBorderAndFieldSize(numRowsAndColumns, size);
                    });
            this.boardPane.heightProperty().addListener(
                    (final ObservableValue<? extends Number> observable, final Number oldValue,
                            final Number newValue) -> {
                        final double size = Math.min(newValue.doubleValue(), this.boardPane.getWidth());
                        this.setBorderAndFieldSize(numRowsAndColumns, size);
                    });

            this.fieldPane.widthProperty().addListener(
                    (final ObservableValue<? extends Number> observable, final Number oldValue,
                            final Number newValue) -> {
                        final double size = Math.min(newValue.doubleValue(), this.fieldPane.getHeight());
                        this.fieldControlSize.set(size / numRowsAndColumns);
                    });
            this.fieldPane.heightProperty().addListener(
                    (final ObservableValue<? extends Number> observable, final Number oldValue,
                            final Number newValue) -> {
                        final double size = Math.min(newValue.doubleValue(), this.fieldPane.getWidth());
                        this.fieldControlSize.set(size / numRowsAndColumns);
                    });

            IntStream.range(0, numRowsAndColumns).forEachOrdered((final int row) -> {
                IntStream.range(0, numRowsAndColumns).forEachOrdered((final int column) -> {
                    final OthelloPosition position = OthelloPosition.of(row, column);
                    final OthelloFieldState fieldState = board.getFieldAt(position).getState();
                    final OthelloFieldView fieldControl = new OthelloFieldView(fieldState);
                    this.fieldPane.add(fieldControl, column, row);
                    this.controls.put(position, fieldControl);

                    fieldControl.prefWidthProperty().bind(this.fieldControlSize);
                    fieldControl.prefHeightProperty().bind(this.fieldControlSize);
                });
            });
        });

        this.delayNextMove();
    }

    /**
     * Adds the labels to the top and left edges.
     *
     * @param numRowsAndColumns The number of rows (and columns) of the board.
     */
    private void addLabelsToEdge(final int numRowsAndColumns) {
        for (char i = 0; i < numRowsAndColumns; ++i) {
            final Label leftLabel = new Label(String.valueOf((char) (i + '1')));
            leftLabel.fontProperty().bind(this.borderLabelFont);
            leftLabel.setMaxHeight(Double.MAX_VALUE);
            leftLabel.setAlignment(Pos.CENTER);
            this.leftEdge.getChildren().add(leftLabel);
            VBox.setVgrow(leftLabel, Priority.ALWAYS);

            final Label rightLabel = new Label(String.valueOf((char) (i + '1')));
            rightLabel.fontProperty().bind(this.borderLabelFont);
            rightLabel.setMaxHeight(Double.MAX_VALUE);
            rightLabel.setAlignment(Pos.CENTER);
            this.rightEdge.getChildren().add(rightLabel);
            VBox.setVgrow(rightLabel, Priority.ALWAYS);

            final Label topLabel = new Label(String.valueOf((char) (i + 'A')));
            topLabel.fontProperty().bind(this.borderLabelFont);
            topLabel.setMaxWidth(Double.MAX_VALUE);
            topLabel.setAlignment(Pos.CENTER);
            this.topEdge.getChildren().add(topLabel);
            HBox.setHgrow(topLabel, Priority.ALWAYS);

            final Label bottomLabel = new Label(String.valueOf((char) (i + 'A')));
            bottomLabel.fontProperty().bind(this.borderLabelFont);
            bottomLabel.setMaxWidth(Double.MAX_VALUE);
            bottomLabel.setAlignment(Pos.CENTER);
            this.bottomEdge.getChildren().add(bottomLabel);
            HBox.setHgrow(bottomLabel, Priority.ALWAYS);
        }
    }

    /**
     * Sets the size of the board's fields and border.
     *
     * @param numRowsAndColumns The number of rows (and columns) of the board.
     * @param boardSizeInPixels The width (and height) of the board in pixels.
     */
    private void setBorderAndFieldSize(final int numRowsAndColumns, final double boardSizeInPixels) {
        final double boardSizeWithoutBorders = boardSizeInPixels * numRowsAndColumns / (numRowsAndColumns + 1);
        this.fieldPane.setPrefSize(boardSizeWithoutBorders, boardSizeWithoutBorders);
        this.topEdge.setPrefWidth(boardSizeWithoutBorders);
        this.leftEdge.setPrefHeight(boardSizeWithoutBorders);
        this.rightEdge.setPrefHeight(boardSizeWithoutBorders);
        this.bottomEdge.setPrefWidth(boardSizeWithoutBorders);

        this.fontSizeInPixels.set(this.fieldControlSize.get() * 0.5);
        final double fontSize = this.fontSizeInPixels.get() * this.pixelsToPointsFactor;

        final Font fontRegular = Font.font(null, FontWeight.NORMAL, FontPosture.REGULAR, fontSize);
        final Font fontBold = Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, fontSize);

        this.borderLabelFont.set(fontRegular);
        this.labelTextFont.set(fontBold);
        this.labelValueFont.set(fontRegular);
        this.gameResultFont.set(fontBold);

        this.margin.set(this.fontSizeInPixels.get() * 0.25);
        this.gridPadding.set(new Insets(this.margin.get()));
    }

    @Override
    public void nextPlayersComputed(final Game<?, ?, ?, ?> game, final State<?, ?> state,
            final Set<? extends Player> players) {
        // nothing to do
    }

    @Override
    public void illegalPlayerRejected(final Game<?, ?, ?, ?> game, final State<?, ?> state, final Player player) {
        // nothing to do
    }

    @Override
    public void legalMoveApplied(final Game<?, ?, ?, ?> game, final State<?, ?> state, final Player player,
            final Move<?, ?> move) throws InterruptedException {

        Platform.runLater(() -> {
            final OthelloState othelloState = (OthelloState) state;

            this.updateFields(othelloState, OthelloFieldState.BLACK, this.blackTokens);
            this.updateFields(othelloState, OthelloFieldState.WHITE, this.whiteTokens);

            this.lastGameState = Optional.of(othelloState);
        });

        this.delayNextMove();
    }

    /**
     * Updates the fields and the label for a given field state after a move.
     *
     * @param othelloState The game state.
     * @param fieldState   The state of the fields to update ({@link OthelloFieldState#BLACK} or
     *                     {@link OthelloFieldState#WHITE}).
     * @param label        The label to update ({@link #blackTokens} or {@link #whiteTokens}).
     */
    private void updateFields(final OthelloState othelloState, final OthelloFieldState fieldState, final Label label) {
        final Set<OthelloPosition> currentFieldsInDesiredState = new LinkedHashSet<>(
                othelloState.getBoard().getFieldsBeing(fieldState).keySet());
        label.setText(Integer.toString(currentFieldsInDesiredState.size()));
        currentFieldsInDesiredState.removeAll(this.lastGameState.get().getBoard().getFieldsBeing(fieldState).keySet());
        currentFieldsInDesiredState
                .forEach((final OthelloPosition position) -> this.controls.get(position).setFieldState(fieldState));
    }

    @Override
    public void illegalMoveRejected(final Game<?, ?, ?, ?> game, final State<?, ?> state, final Player player,
            final Optional<Move<?, ?>> move, final String reason) {
        // nothing to do
    }

    @Override
    public void overdueMoveRejected(final Game<?, ?, ?, ?> game, final State<?, ?> state, final Player player,
            final Optional<Move<?, ?>> chosenMove) {
        // nothing to do
    }

    @Override
    public void playerResigned(final Game<?, ?, ?, ?> game, final State<?, ?> state, final Player player) {
        // nothing to do
    }

    @Override
    public void playerOvertaken(final Game<?, ?, ?, ?> game, final State<?, ?> state, final Player overtakenPlayer,
            final Player overtakingPlayer) {
        // nothing to do
    }

    @Override
    public void finished(final Game<?, ?, ?, ?> game, final State<?, ?> state) {
        Platform.runLater(() -> {
            final OthelloState othelloState = (OthelloState) state;
            if (othelloState.getBlackPlayer().getState().equals(PlayerState.WON)) {
                this.setGameState("BLACK WINS!");
            } else if (othelloState.getWhitePlayer().getState().equals(PlayerState.WON)) {
                this.setGameState("WHITE WINS!");
            } else if (othelloState.getBlackPlayer().getState().equals(PlayerState.DRAW)) {
                this.setGameState("DRAW GAME!");
            }
        });
    }

    /**
     * Destroys this object.
     *
     * @param game The associated game.
     */
    public void destroy(final OthelloGame game) {
        this.gameStateDescriptionAnimation.ifPresent((final Timeline timeline) -> timeline.stop());
        this.semaphore.release();
        game.removeObserver(this);
    }

    /**
     * Delays the execution of the next move.
     */
    private void delayNextMove() throws InterruptedException {
        Thread.sleep(this.delay.get());
    }

    /**
     * Returns a pane displaying the state of one player.
     *
     * @param game   The game.
     * @param player The player.
     * @return The requested pane.
     */
    private GridPane createPlayerPane(final OthelloGame game, final OthelloPlayer player) {
        final Label nameLabel = new Label("Name:");
        nameLabel.fontProperty().bind(this.labelTextFont);

        final Label nameText = new Label(player.getName());
        nameText.fontProperty().bind(this.labelValueFont);

        final Label colourLabel = new Label("Colour:");
        colourLabel.fontProperty().bind(this.labelTextFont);

        final Label colourText = new Label();
        colourText.fontProperty().bind(this.labelValueFont);
        if (player.isUsingBlackTokens()) {
            final Circle circle = new Circle();
            circle.radiusProperty().bind(this.fontSizeInPixels.multiply(0.25));
            circle.setFill(Color.BLACK);
            circle.setStroke(Color.BLACK);
            colourText.setGraphic(circle);
        } else {
            final Circle circle = new Circle();
            circle.radiusProperty().bind(this.fontSizeInPixels.multiply(0.25));
            circle.setFill(Color.WHITE);
            circle.setStroke(Color.BLACK);
            colourText.setGraphic(circle);
        }

        final Label strategyLabel = new Label("Strategy:");
        strategyLabel.fontProperty().bind(this.labelTextFont);

        final Label strategyText;
        final String rawName = game.getStrategies().get(player.getName()).toString();
        final Matcher nameMatcher = OthelloBoardView.STRATEGY_NAME_PATTERN.matcher(rawName);
        if (nameMatcher.matches()) {
            strategyText = new Label(nameMatcher.group(2));
        } else {
            strategyText = new Label(rawName);
        }
        strategyText.fontProperty().bind(this.labelValueFont);

        final Label tokensLabel = new Label("Tokens:");
        tokensLabel.fontProperty().bind(this.labelTextFont);

        final Label tokensText = new Label("0");
        tokensText.fontProperty().bind(this.labelValueFont);
        if (player.isUsingBlackTokens()) {
            this.blackTokens = tokensText;
        } else {
            this.whiteTokens = tokensText;
        }

        final GridPane gridPane = new GridPane();

        final ColumnConstraints labelColumnConstraints = new ColumnConstraints();
        labelColumnConstraints.setHalignment(HPos.RIGHT);
        labelColumnConstraints.setHgrow(Priority.NEVER);
        final ColumnConstraints textColumnConstraints = new ColumnConstraints();
        textColumnConstraints.setHalignment(HPos.LEFT);
        textColumnConstraints.setHgrow(Priority.ALWAYS);
        textColumnConstraints.setMaxWidth(Double.MAX_VALUE);
        gridPane.getColumnConstraints().addAll(labelColumnConstraints, textColumnConstraints);

        final RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.TOP);
        gridPane.getRowConstraints().addAll(rowConstraints, rowConstraints, rowConstraints, rowConstraints);

        gridPane.addRow(0, nameLabel, nameText);
        gridPane.addRow(1, colourLabel, colourText);
        gridPane.addRow(2, strategyLabel, strategyText);
        gridPane.addRow(3, tokensLabel, tokensText);

        gridPane.setBorder(
                new Border(
                        new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THICK)));

        gridPane.hgapProperty().bind(this.margin.multiply(2.0));
        gridPane.paddingProperty().bind(this.gridPadding);

        return gridPane;
    }
}
