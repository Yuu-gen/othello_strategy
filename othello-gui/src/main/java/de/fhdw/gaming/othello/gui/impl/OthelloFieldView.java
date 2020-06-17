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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Displays an Othello field.
 */
final class OthelloFieldView extends Region {

    /**
     * Number of frames per second in an animation.
     */
    private static final int FRAMES_PER_SECOND = 25;
    /**
     * Fraction of the duration of the token change animation in relation to the duration of a move.
     */
    private static final double TOKEN_ANIMATION_RATIO = 0.5;

    /**
     * The old field state.
     */
    private OthelloFieldState oldFieldState;
    /**
     * The field state.
     */
    private OthelloFieldState fieldState;
    /**
     * The canvas.
     */
    private final Canvas canvas;
    /**
     * The duration of a move.
     */
    private final ObjectProperty<Duration> moveDuration;
    /**
     * The current animation (if any).
     */
    private Optional<Timeline> currentAnimation;
    /**
     * {@code true} if the field is highlighted, else {@code false}.
     */
    private boolean highlighted;

    /**
     * Creates an {@link OthelloFieldView}.
     *
     * @param fieldState The field state.
     */
    OthelloFieldView(final OthelloFieldState fieldState) {
        this.oldFieldState = fieldState;
        this.fieldState = fieldState;
        this.moveDuration = new SimpleObjectProperty<>(Duration.millis(500.0));
        this.currentAnimation = Optional.empty();
        this.highlighted = false;

        this.canvas = new Canvas() {

            @Override
            public boolean isResizable() {
                return true;
            }

            @Override
            public double prefWidth(final double height) {
                return 0.0;
            }

            @Override
            public double prefHeight(final double width) {
                return 0.0;
            }

            @Override
            public double maxWidth(final double height) {
                return Double.POSITIVE_INFINITY;
            }

            @Override
            public double maxHeight(final double width) {
                return Double.POSITIVE_INFINITY;
            }
        };

        this.canvas.widthProperty().bind(this.widthProperty());
        this.canvas.heightProperty().bind(this.heightProperty());
        this.getChildren().add(this.canvas);

        this.widthProperty().addListener(
                (final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue) -> {
                    this.draw();
                });
        this.heightProperty().addListener(
                (final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue) -> {
                    this.draw();
                });

        this.setMinSize(50.0, 50.0);
        this.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Returns the move duration property.
     */
    ObjectProperty<Duration> moveDurationProperty() {
        return this.moveDuration;
    }

    /**
     * Updates the state of the field control.
     *
     * @param fieldState The field state.
     */
    void setFieldState(final OthelloFieldState fieldState) {
        this.oldFieldState = this.fieldState;
        this.fieldState = fieldState;
        this.draw();
    }

    /**
     * Updates the highlight state of the field control.
     *
     * @param highlighted {@code true} if the field is highlighted, else {@code false}.
     */
    void setHighlighted(final boolean highlighted) {
        this.highlighted = highlighted;
        this.draw();
    }

    /**
     * Draws the field.
     */
    private void draw() {
        this.currentAnimation.ifPresent((final Timeline timeline) -> timeline.stop());

        final double size = this.getWidth();

        final GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(this.highlighted ? Color.GREEN : Color.DARKOLIVEGREEN);
        gc.fillRect(0.0, 0.0, size, size);

        gc.setFill(Color.BLACK);
        gc.beginPath();
        gc.moveTo(0.0, 0.0);
        gc.lineTo(size, 0.0);
        gc.lineTo(size, size);
        gc.lineTo(0.0, size);
        gc.lineTo(0.0, 0.0);
        gc.closePath();
        gc.stroke();

        final double margin = size * 0.1;
        final boolean animate = !this.oldFieldState.equals(OthelloFieldState.EMPTY)
                && !this.oldFieldState.equals(this.fieldState);

        switch (this.fieldState) {
        case EMPTY:
            // nothing to do
            break;
        case BLACK:
            if (animate) {
                gc.setFill(Color.WHITE);
                gc.fillOval(margin, margin, size - 2 * margin, size - 2 * margin);
                gc.setFill(Color.BLACK);
                this.animateTokenChange(gc, new Rectangle2D(margin, margin, size - 2 * margin, size - 2 * margin));
            } else {
                gc.setFill(Color.BLACK);
                gc.fillOval(margin, margin, size - 2 * margin, size - 2 * margin);
            }
            break;
        case WHITE:
            if (animate) {
                gc.setFill(Color.BLACK);
                gc.fillOval(margin, margin, size - 2 * margin, size - 2 * margin);
                gc.setFill(Color.WHITE);
                this.animateTokenChange(gc, new Rectangle2D(margin, margin, size - 2 * margin, size - 2 * margin));
            } else {
                gc.setFill(Color.WHITE);
                gc.fillOval(margin, margin, size - 2 * margin, size - 2 * margin);
            }
            break;
        default:
            throw new UnsupportedOperationException(
                    String.format("Unknown Othello field state '%s'.", this.fieldState));
        }

        this.oldFieldState = this.fieldState;
    }

    /**
     * Animates a token change.
     *
     * @param gc   The {@link GraphicsContext} to use.
     * @param area The area where the token should be drawn.
     */
    private void animateTokenChange(final GraphicsContext gc, final Rectangle2D area) {
        final Point2D centre = new Point2D(
                area.getMinX() + area.getWidth() / 2.0,
                area.getMinY() + area.getHeight() / 2.0);

        final List<KeyFrame> keyFrames = new ArrayList<>();
        for (int keyFrameIndex = 1; keyFrameIndex < OthelloFieldView.FRAMES_PER_SECOND; ++keyFrameIndex) {
            final double diffX = area.getWidth() * keyFrameIndex / OthelloFieldView.FRAMES_PER_SECOND / 2;
            final double diffY = area.getHeight() * keyFrameIndex / OthelloFieldView.FRAMES_PER_SECOND / 2;

            final KeyFrame keyFrame = new KeyFrame(
                    this.moveDuration.get().multiply(OthelloFieldView.TOKEN_ANIMATION_RATIO).multiply(keyFrameIndex)
                            .divide(OthelloFieldView.FRAMES_PER_SECOND),
                    (final ActionEvent event) -> {
                        gc.fillOval(centre.getX() - diffX, centre.getY() - diffY, diffX * 2, diffY * 2);
                    });
            keyFrames.add(keyFrame);
        }

        final KeyFrame finalKeyFrame = new KeyFrame(
                this.moveDuration.get().multiply(OthelloFieldView.TOKEN_ANIMATION_RATIO),
                (final ActionEvent event) -> {
                    gc.fillOval(area.getMinX(), area.getMinY(), area.getWidth(), area.getHeight());
                });
        keyFrames.add(finalKeyFrame);

        this.currentAnimation = Optional.of(new Timeline(keyFrames.toArray(new KeyFrame[0])));
        this.currentAnimation.get().play();
    }
}
