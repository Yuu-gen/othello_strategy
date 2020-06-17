/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-core.
 *
 * Othello-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Othello-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with othello-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.othello.core.domain;

/**
 * Represents the state of a field.
 */
public enum OthelloFieldState {

    /**
     * The field is empty.
     */
    EMPTY {
        @Override
        public OthelloFieldState inverse() {
            return this;
        }

        @Override
        public String toString() {
            return "empty";
        }
    },

    /**
     * There is a black token on the field.
     */
    BLACK {
        @Override
        public OthelloFieldState inverse() {
            return WHITE;
        }

        @Override
        public String toString() {
            return "black";
        }
    },

    /**
     * There is a white token on the field.
     */
    WHITE {
        @Override
        public OthelloFieldState inverse() {
            return BLACK;
        }

        @Override
        public String toString() {
            return "white";
        }
    };

    /**
     * Returns the inverse of the field state. {@link #EMPTY} transforms into itself.
     */
    public abstract OthelloFieldState inverse();

    @Override
    public abstract String toString();
}
