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
 * Represents a direction on the board.
 */
public enum OthelloDirection {

    /**
     * Upwards.
     */
    NORTH {
        @Override
        public OthelloPosition step(final OthelloPosition origin) {
            return origin.offset(-1, 0);
        }
    },

    /**
     * Upwards and to the right.
     */
    NORTHEAST {
        @Override
        public OthelloPosition step(final OthelloPosition origin) {
            return origin.offset(-1, 1);
        }
    },

    /**
     * To the right.
     */
    EAST {
        @Override
        public OthelloPosition step(final OthelloPosition origin) {
            return origin.offset(0, 1);
        }
    },

    /**
     * Downwards and to the right.
     */
    SOUTHEAST {
        @Override
        public OthelloPosition step(final OthelloPosition origin) {
            return origin.offset(1, 1);
        }
    },

    /**
     * Downwards.
     */
    SOUTH {
        @Override
        public OthelloPosition step(final OthelloPosition origin) {
            return origin.offset(1, 0);
        }
    },

    /**
     * Downwards and to the left.
     */
    SOUTHWEST {
        @Override
        public OthelloPosition step(final OthelloPosition origin) {
            return origin.offset(1, -1);
        }
    },

    /**
     * To the left.
     */
    WEST {
        @Override
        public OthelloPosition step(final OthelloPosition origin) {
            return origin.offset(0, -1);
        }
    },

    /**
     * Upwards and to the left.
     */
    NORTHWEST {
        @Override
        public OthelloPosition step(final OthelloPosition origin) {
            return origin.offset(-1, -1);
        }
    };

    /**
     * Performs a step into this direction given an origin. Note that the resulting position is not checked against any
     * bounds, so this has to be done by the caller if necessary.
     *
     * @param origin The origin.
     * @return The resulting position.
     */
    public abstract OthelloPosition step(OthelloPosition origin);
}
