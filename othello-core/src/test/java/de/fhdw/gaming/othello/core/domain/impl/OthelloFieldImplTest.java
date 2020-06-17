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
package de.fhdw.gaming.othello.core.domain.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.othello.core.domain.OthelloDirection;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;

/**
 * Tests {@link OthelloFieldImpl}.
 */
class OthelloFieldImplTest {

    /**
     * The {@link OthelloBoardImpl} containing the fields.
     */
    private OthelloBoardImpl board;

    /**
     * Creates an {@link OthelloBoardImpl}.
     */
    @BeforeEach
    void setUp() {
        this.board = new OthelloBoardImpl(4);
    }

    /**
     * Tests {@link OthelloFieldImpl#getBoard()}.
     */
    @Test
    void testGetBoard() {
        final OthelloFieldImpl field = this.fieldAt(0, 0);
        assertThat(field.getBoard(), is(sameInstance(this.board)));
    }

    /**
     * Tests {@link OthelloFieldImpl#getPosition()}.
     */
    @Test
    void testGetPosition() {
        final OthelloFieldImpl field00 = this.fieldAt(0, 0);
        assertThat(field00.getPosition(), is(equalTo(pos(0, 0))));
        final OthelloFieldImpl field33 = this.fieldAt(3, 3);
        assertThat(field33.getPosition(), is(equalTo(pos(3, 3))));
    }

    /**
     * Tests {@link OthelloFieldImpl#getState()}.
     */
    @Test
    void testGetState() {
        for (int row = 0; row < 4; ++row) {
            for (int column = 0; column < 4; ++column) {
                if (row >= 1 && row <= 2 && column >= 1 && column <= 2) {
                    continue;
                }
                final OthelloFieldImpl field = this.fieldAt(row, column);
                assertThat(field.getState(), is(equalTo(OthelloFieldState.EMPTY)));
            }
        }

        assertThat(this.fieldAt(1, 1).getState(), is(equalTo(OthelloFieldState.WHITE)));
        assertThat(this.fieldAt(1, 2).getState(), is(equalTo(OthelloFieldState.BLACK)));
        assertThat(this.fieldAt(2, 1).getState(), is(equalTo(OthelloFieldState.BLACK)));
        assertThat(this.fieldAt(2, 2).getState(), is(equalTo(OthelloFieldState.WHITE)));
    }

    /**
     * Tests {@link OthelloFieldImpl#setState(de.fhdw.gaming.othello.core.domain.OthelloFieldState)}.
     */
    @Test
    void testSetState() {
        final OthelloFieldImpl field = this.fieldAt(1, 1);
        assertThat(field.getState(), is(equalTo(OthelloFieldState.WHITE)));
        field.setState(OthelloFieldState.BLACK);
        assertThat(field.getState(), is(equalTo(OthelloFieldState.BLACK)));
        field.setState(OthelloFieldState.WHITE);
        assertThat(field.getState(), is(equalTo(OthelloFieldState.WHITE)));
    }

    /**
     * Tests {@link OthelloFieldImpl#setState(de.fhdw.gaming.othello.core.domain.OthelloFieldState)} when setting a
     * field's state to {@link OthelloFieldState#EMPTY}.
     */
    @Test
    void testSetStateToEmpty() {
        final OthelloFieldImpl field00 = this.fieldAt(0, 0);
        assertThat(field00.getState(), is(equalTo(OthelloFieldState.EMPTY)));
        field00.setState(OthelloFieldState.EMPTY);
        assertThat(field00.getState(), is(equalTo(OthelloFieldState.EMPTY)));

        final OthelloFieldImpl field11 = this.fieldAt(1, 1);
        assertThat(field11.getState(), is(equalTo(OthelloFieldState.WHITE)));
        assertThrows(IllegalArgumentException.class, () -> field11.setState(OthelloFieldState.EMPTY));
    }

    /**
     * Tests {@link OthelloFieldImpl#hasNeighbour(de.fhdw.gaming.othello.core.domain.OthelloDirection)} on fields lying
     * in the corners of the board.
     */
    @Test
    void testHasNeighbourOfCornerFields() {
        final OthelloFieldImpl field00 = this.fieldAt(0, 0);
        assertThat(field00.hasNeighbour(OthelloDirection.NORTH), is(equalTo(false)));
        assertThat(field00.hasNeighbour(OthelloDirection.NORTHEAST), is(equalTo(false)));
        assertThat(field00.hasNeighbour(OthelloDirection.EAST), is(equalTo(true)));
        assertThat(field00.hasNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(true)));
        assertThat(field00.hasNeighbour(OthelloDirection.SOUTH), is(equalTo(true)));
        assertThat(field00.hasNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(false)));
        assertThat(field00.hasNeighbour(OthelloDirection.WEST), is(equalTo(false)));
        assertThat(field00.hasNeighbour(OthelloDirection.NORTHWEST), is(equalTo(false)));

        final OthelloFieldImpl field03 = this.fieldAt(0, 3);
        assertThat(field03.hasNeighbour(OthelloDirection.NORTH), is(equalTo(false)));
        assertThat(field03.hasNeighbour(OthelloDirection.NORTHEAST), is(equalTo(false)));
        assertThat(field03.hasNeighbour(OthelloDirection.EAST), is(equalTo(false)));
        assertThat(field03.hasNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(false)));
        assertThat(field03.hasNeighbour(OthelloDirection.SOUTH), is(equalTo(true)));
        assertThat(field03.hasNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(true)));
        assertThat(field03.hasNeighbour(OthelloDirection.WEST), is(equalTo(true)));
        assertThat(field03.hasNeighbour(OthelloDirection.NORTHWEST), is(equalTo(false)));

        final OthelloFieldImpl field30 = this.fieldAt(3, 0);
        assertThat(field30.hasNeighbour(OthelloDirection.NORTH), is(equalTo(true)));
        assertThat(field30.hasNeighbour(OthelloDirection.NORTHEAST), is(equalTo(true)));
        assertThat(field30.hasNeighbour(OthelloDirection.EAST), is(equalTo(true)));
        assertThat(field30.hasNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(false)));
        assertThat(field30.hasNeighbour(OthelloDirection.SOUTH), is(equalTo(false)));
        assertThat(field30.hasNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(false)));
        assertThat(field30.hasNeighbour(OthelloDirection.WEST), is(equalTo(false)));
        assertThat(field30.hasNeighbour(OthelloDirection.NORTHWEST), is(equalTo(false)));

        final OthelloFieldImpl field33 = this.fieldAt(3, 3);
        assertThat(field33.hasNeighbour(OthelloDirection.NORTH), is(equalTo(true)));
        assertThat(field33.hasNeighbour(OthelloDirection.NORTHEAST), is(equalTo(false)));
        assertThat(field33.hasNeighbour(OthelloDirection.EAST), is(equalTo(false)));
        assertThat(field33.hasNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(false)));
        assertThat(field33.hasNeighbour(OthelloDirection.SOUTH), is(equalTo(false)));
        assertThat(field33.hasNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(false)));
        assertThat(field33.hasNeighbour(OthelloDirection.WEST), is(equalTo(true)));
        assertThat(field33.hasNeighbour(OthelloDirection.NORTHWEST), is(equalTo(true)));
    }

    /**
     * Tests {@link OthelloFieldImpl#hasNeighbour(de.fhdw.gaming.othello.core.domain.OthelloDirection)} on fields lying
     * on the edges of the board.
     */
    @Test
    void testHasNeighbourOfEdgeFields() {
        final OthelloFieldImpl field01 = this.fieldAt(0, 1);
        assertThat(field01.hasNeighbour(OthelloDirection.NORTH), is(equalTo(false)));
        assertThat(field01.hasNeighbour(OthelloDirection.NORTHEAST), is(equalTo(false)));
        assertThat(field01.hasNeighbour(OthelloDirection.EAST), is(equalTo(true)));
        assertThat(field01.hasNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(true)));
        assertThat(field01.hasNeighbour(OthelloDirection.SOUTH), is(equalTo(true)));
        assertThat(field01.hasNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(true)));
        assertThat(field01.hasNeighbour(OthelloDirection.WEST), is(equalTo(true)));
        assertThat(field01.hasNeighbour(OthelloDirection.NORTHWEST), is(equalTo(false)));

        final OthelloFieldImpl field10 = this.fieldAt(1, 0);
        assertThat(field10.hasNeighbour(OthelloDirection.NORTH), is(equalTo(true)));
        assertThat(field10.hasNeighbour(OthelloDirection.NORTHEAST), is(equalTo(true)));
        assertThat(field10.hasNeighbour(OthelloDirection.EAST), is(equalTo(true)));
        assertThat(field10.hasNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(true)));
        assertThat(field10.hasNeighbour(OthelloDirection.SOUTH), is(equalTo(true)));
        assertThat(field10.hasNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(false)));
        assertThat(field10.hasNeighbour(OthelloDirection.WEST), is(equalTo(false)));
        assertThat(field10.hasNeighbour(OthelloDirection.NORTHWEST), is(equalTo(false)));

        final OthelloFieldImpl field23 = this.fieldAt(2, 3);
        assertThat(field23.hasNeighbour(OthelloDirection.NORTH), is(equalTo(true)));
        assertThat(field23.hasNeighbour(OthelloDirection.NORTHEAST), is(equalTo(false)));
        assertThat(field23.hasNeighbour(OthelloDirection.EAST), is(equalTo(false)));
        assertThat(field23.hasNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(false)));
        assertThat(field23.hasNeighbour(OthelloDirection.SOUTH), is(equalTo(true)));
        assertThat(field23.hasNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(true)));
        assertThat(field23.hasNeighbour(OthelloDirection.WEST), is(equalTo(true)));
        assertThat(field23.hasNeighbour(OthelloDirection.NORTHWEST), is(equalTo(true)));

        final OthelloFieldImpl field32 = this.fieldAt(3, 2);
        assertThat(field32.hasNeighbour(OthelloDirection.NORTH), is(equalTo(true)));
        assertThat(field32.hasNeighbour(OthelloDirection.NORTHEAST), is(equalTo(true)));
        assertThat(field32.hasNeighbour(OthelloDirection.EAST), is(equalTo(true)));
        assertThat(field32.hasNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(false)));
        assertThat(field32.hasNeighbour(OthelloDirection.SOUTH), is(equalTo(false)));
        assertThat(field32.hasNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(false)));
        assertThat(field32.hasNeighbour(OthelloDirection.WEST), is(equalTo(true)));
        assertThat(field32.hasNeighbour(OthelloDirection.NORTHWEST), is(equalTo(true)));
    }

    /**
     * Tests {@link OthelloFieldImpl#hasNeighbour(de.fhdw.gaming.othello.core.domain.OthelloDirection)} on fields in the
     * centre of the board.
     */
    @Test
    void testHasNeighbourOfCentreFields() {
        final OthelloFieldImpl field11 = this.fieldAt(1, 1);
        assertThat(field11.hasNeighbour(OthelloDirection.NORTH), is(equalTo(true)));
        assertThat(field11.hasNeighbour(OthelloDirection.NORTHEAST), is(equalTo(true)));
        assertThat(field11.hasNeighbour(OthelloDirection.EAST), is(equalTo(true)));
        assertThat(field11.hasNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(true)));
        assertThat(field11.hasNeighbour(OthelloDirection.SOUTH), is(equalTo(true)));
        assertThat(field11.hasNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(true)));
        assertThat(field11.hasNeighbour(OthelloDirection.WEST), is(equalTo(true)));
        assertThat(field11.hasNeighbour(OthelloDirection.NORTHWEST), is(equalTo(true)));

        final OthelloFieldImpl field22 = this.fieldAt(2, 2);
        assertThat(field22.hasNeighbour(OthelloDirection.NORTH), is(equalTo(true)));
        assertThat(field22.hasNeighbour(OthelloDirection.NORTHEAST), is(equalTo(true)));
        assertThat(field22.hasNeighbour(OthelloDirection.EAST), is(equalTo(true)));
        assertThat(field22.hasNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(true)));
        assertThat(field22.hasNeighbour(OthelloDirection.SOUTH), is(equalTo(true)));
        assertThat(field22.hasNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(true)));
        assertThat(field22.hasNeighbour(OthelloDirection.WEST), is(equalTo(true)));
        assertThat(field22.hasNeighbour(OthelloDirection.NORTHWEST), is(equalTo(true)));
    }

    /**
     * Tests {@link OthelloFieldImpl#getNeighbour(de.fhdw.gaming.othello.core.domain.OthelloDirection)} on fields lying
     * in the corners of the board.
     */
    @Test
    void testGetNeighbourOfCornerFields() {
        final OthelloFieldImpl field00 = this.fieldAt(0, 0);
        assertThrows(IllegalArgumentException.class, () -> field00.getNeighbour(OthelloDirection.NORTH));
        assertThrows(IllegalArgumentException.class, () -> field00.getNeighbour(OthelloDirection.NORTHEAST));
        assertThat(field00.getNeighbour(OthelloDirection.EAST), is(equalTo(this.fieldAt(0, 1))));
        assertThat(field00.getNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(this.fieldAt(1, 1))));
        assertThat(field00.getNeighbour(OthelloDirection.SOUTH), is(equalTo(this.fieldAt(1, 0))));
        assertThrows(IllegalArgumentException.class, () -> field00.getNeighbour(OthelloDirection.SOUTHWEST));
        assertThrows(IllegalArgumentException.class, () -> field00.getNeighbour(OthelloDirection.WEST));
        assertThrows(IllegalArgumentException.class, () -> field00.getNeighbour(OthelloDirection.NORTHWEST));

        final OthelloFieldImpl field03 = this.fieldAt(0, 3);
        assertThrows(IllegalArgumentException.class, () -> field03.getNeighbour(OthelloDirection.NORTH));
        assertThrows(IllegalArgumentException.class, () -> field03.getNeighbour(OthelloDirection.NORTHEAST));
        assertThrows(IllegalArgumentException.class, () -> field03.getNeighbour(OthelloDirection.EAST));
        assertThrows(IllegalArgumentException.class, () -> field03.getNeighbour(OthelloDirection.SOUTHEAST));
        assertThat(field03.getNeighbour(OthelloDirection.SOUTH), is(equalTo(this.fieldAt(1, 3))));
        assertThat(field03.getNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(this.fieldAt(1, 2))));
        assertThat(field03.getNeighbour(OthelloDirection.WEST), is(equalTo(this.fieldAt(0, 2))));
        assertThrows(IllegalArgumentException.class, () -> field03.getNeighbour(OthelloDirection.NORTHWEST));

        final OthelloFieldImpl field30 = this.fieldAt(3, 0);
        assertThat(field30.getNeighbour(OthelloDirection.NORTH), is(equalTo(this.fieldAt(2, 0))));
        assertThat(field30.getNeighbour(OthelloDirection.NORTHEAST), is(equalTo(this.fieldAt(2, 1))));
        assertThat(field30.getNeighbour(OthelloDirection.EAST), is(equalTo(this.fieldAt(3, 1))));
        assertThrows(IllegalArgumentException.class, () -> field30.getNeighbour(OthelloDirection.SOUTHEAST));
        assertThrows(IllegalArgumentException.class, () -> field30.getNeighbour(OthelloDirection.SOUTH));
        assertThrows(IllegalArgumentException.class, () -> field30.getNeighbour(OthelloDirection.SOUTHWEST));
        assertThrows(IllegalArgumentException.class, () -> field30.getNeighbour(OthelloDirection.WEST));
        assertThrows(IllegalArgumentException.class, () -> field30.getNeighbour(OthelloDirection.NORTHWEST));

        final OthelloFieldImpl field33 = this.fieldAt(3, 3);
        assertThat(field33.getNeighbour(OthelloDirection.NORTH), is(equalTo(this.fieldAt(2, 3))));
        assertThrows(IllegalArgumentException.class, () -> field33.getNeighbour(OthelloDirection.NORTHEAST));
        assertThrows(IllegalArgumentException.class, () -> field33.getNeighbour(OthelloDirection.EAST));
        assertThrows(IllegalArgumentException.class, () -> field33.getNeighbour(OthelloDirection.SOUTHEAST));
        assertThrows(IllegalArgumentException.class, () -> field33.getNeighbour(OthelloDirection.SOUTH));
        assertThrows(IllegalArgumentException.class, () -> field33.getNeighbour(OthelloDirection.SOUTHWEST));
        assertThat(field33.getNeighbour(OthelloDirection.WEST), is(equalTo(this.fieldAt(3, 2))));
        assertThat(field33.getNeighbour(OthelloDirection.NORTHWEST), is(equalTo(this.fieldAt(2, 2))));
    }

    /**
     * Tests {@link OthelloFieldImpl#getNeighbour(de.fhdw.gaming.othello.core.domain.OthelloDirection)} on fields lying
     * on the edges of the board.
     */
    @Test
    void testGetNeighbourOfEdgeFields() {
        final OthelloFieldImpl field01 = this.fieldAt(0, 1);
        assertThrows(IllegalArgumentException.class, () -> field01.getNeighbour(OthelloDirection.NORTH));
        assertThrows(IllegalArgumentException.class, () -> field01.getNeighbour(OthelloDirection.NORTHEAST));
        assertThat(field01.getNeighbour(OthelloDirection.EAST), is(equalTo(this.fieldAt(0, 2))));
        assertThat(field01.getNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(this.fieldAt(1, 2))));
        assertThat(field01.getNeighbour(OthelloDirection.SOUTH), is(equalTo(this.fieldAt(1, 1))));
        assertThat(field01.getNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(this.fieldAt(1, 0))));
        assertThat(field01.getNeighbour(OthelloDirection.WEST), is(equalTo(this.fieldAt(0, 0))));
        assertThrows(IllegalArgumentException.class, () -> field01.getNeighbour(OthelloDirection.NORTHWEST));

        final OthelloFieldImpl field10 = this.fieldAt(1, 0);
        assertThat(field10.getNeighbour(OthelloDirection.NORTH), is(equalTo(this.fieldAt(0, 0))));
        assertThat(field10.getNeighbour(OthelloDirection.NORTHEAST), is(equalTo(this.fieldAt(0, 1))));
        assertThat(field10.getNeighbour(OthelloDirection.EAST), is(equalTo(this.fieldAt(1, 1))));
        assertThat(field10.getNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(this.fieldAt(2, 1))));
        assertThat(field10.getNeighbour(OthelloDirection.SOUTH), is(equalTo(this.fieldAt(2, 0))));
        assertThrows(IllegalArgumentException.class, () -> field10.getNeighbour(OthelloDirection.SOUTHWEST));
        assertThrows(IllegalArgumentException.class, () -> field10.getNeighbour(OthelloDirection.WEST));
        assertThrows(IllegalArgumentException.class, () -> field10.getNeighbour(OthelloDirection.NORTHWEST));

        final OthelloFieldImpl field23 = this.fieldAt(2, 3);
        assertThat(field23.getNeighbour(OthelloDirection.NORTH), is(equalTo(this.fieldAt(1, 3))));
        assertThrows(IllegalArgumentException.class, () -> field23.getNeighbour(OthelloDirection.NORTHEAST));
        assertThrows(IllegalArgumentException.class, () -> field23.getNeighbour(OthelloDirection.EAST));
        assertThrows(IllegalArgumentException.class, () -> field23.getNeighbour(OthelloDirection.SOUTHEAST));
        assertThat(field23.getNeighbour(OthelloDirection.SOUTH), is(equalTo(this.fieldAt(3, 3))));
        assertThat(field23.getNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(this.fieldAt(3, 2))));
        assertThat(field23.getNeighbour(OthelloDirection.WEST), is(equalTo(this.fieldAt(2, 2))));
        assertThat(field23.getNeighbour(OthelloDirection.NORTHWEST), is(equalTo(this.fieldAt(1, 2))));

        final OthelloFieldImpl field32 = this.fieldAt(3, 2);
        assertThat(field32.getNeighbour(OthelloDirection.NORTH), is(equalTo(this.fieldAt(2, 2))));
        assertThat(field32.getNeighbour(OthelloDirection.NORTHEAST), is(equalTo(this.fieldAt(2, 3))));
        assertThat(field32.getNeighbour(OthelloDirection.EAST), is(equalTo(this.fieldAt(3, 3))));
        assertThrows(IllegalArgumentException.class, () -> field32.getNeighbour(OthelloDirection.SOUTHEAST));
        assertThrows(IllegalArgumentException.class, () -> field32.getNeighbour(OthelloDirection.SOUTH));
        assertThrows(IllegalArgumentException.class, () -> field32.getNeighbour(OthelloDirection.SOUTHWEST));
        assertThat(field32.getNeighbour(OthelloDirection.WEST), is(equalTo(this.fieldAt(3, 1))));
        assertThat(field32.getNeighbour(OthelloDirection.NORTHWEST), is(equalTo(this.fieldAt(2, 1))));
    }

    /**
     * Tests {@link OthelloFieldImpl#getNeighbour(de.fhdw.gaming.othello.core.domain.OthelloDirection)} on fields lying
     * in the centre of the board.
     */
    @Test
    void testGetNeighbourOfCentreFields() {
        final OthelloFieldImpl field11 = this.fieldAt(1, 1);
        assertThat(field11.getNeighbour(OthelloDirection.NORTH), is(equalTo(this.fieldAt(0, 1))));
        assertThat(field11.getNeighbour(OthelloDirection.NORTHEAST), is(equalTo(this.fieldAt(0, 2))));
        assertThat(field11.getNeighbour(OthelloDirection.EAST), is(equalTo(this.fieldAt(1, 2))));
        assertThat(field11.getNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(this.fieldAt(2, 2))));
        assertThat(field11.getNeighbour(OthelloDirection.SOUTH), is(equalTo(this.fieldAt(2, 1))));
        assertThat(field11.getNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(this.fieldAt(2, 0))));
        assertThat(field11.getNeighbour(OthelloDirection.WEST), is(equalTo(this.fieldAt(1, 0))));
        assertThat(field11.getNeighbour(OthelloDirection.NORTHWEST), is(equalTo(this.fieldAt(0, 0))));

        final OthelloFieldImpl field22 = this.fieldAt(2, 2);
        assertThat(field22.getNeighbour(OthelloDirection.NORTH), is(equalTo(this.fieldAt(1, 2))));
        assertThat(field22.getNeighbour(OthelloDirection.NORTHEAST), is(equalTo(this.fieldAt(1, 3))));
        assertThat(field22.getNeighbour(OthelloDirection.EAST), is(equalTo(this.fieldAt(2, 3))));
        assertThat(field22.getNeighbour(OthelloDirection.SOUTHEAST), is(equalTo(this.fieldAt(3, 3))));
        assertThat(field22.getNeighbour(OthelloDirection.SOUTH), is(equalTo(this.fieldAt(3, 2))));
        assertThat(field22.getNeighbour(OthelloDirection.SOUTHWEST), is(equalTo(this.fieldAt(3, 1))));
        assertThat(field22.getNeighbour(OthelloDirection.WEST), is(equalTo(this.fieldAt(2, 1))));
        assertThat(field22.getNeighbour(OthelloDirection.NORTHWEST), is(equalTo(this.fieldAt(1, 1))));
    }

    /**
     * Tests {@link OthelloFieldImpl#isActive(boolean)}.
     */
    @Test
    void testIsActive() {
        assertThat(this.fieldAt(0, 0).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(0, 0).isActive(false), is(equalTo(false)));
        assertThat(this.fieldAt(0, 1).isActive(true), is(equalTo(true)));
        assertThat(this.fieldAt(0, 1).isActive(false), is(equalTo(false)));
        assertThat(this.fieldAt(0, 2).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(0, 2).isActive(false), is(equalTo(true)));
        assertThat(this.fieldAt(0, 3).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(0, 3).isActive(false), is(equalTo(false)));

        assertThat(this.fieldAt(1, 0).isActive(true), is(equalTo(true)));
        assertThat(this.fieldAt(1, 0).isActive(false), is(equalTo(false)));
        assertThat(this.fieldAt(1, 1).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(1, 1).isActive(false), is(equalTo(false)));
        assertThat(this.fieldAt(1, 2).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(1, 2).isActive(false), is(equalTo(false)));
        assertThat(this.fieldAt(1, 3).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(1, 3).isActive(false), is(equalTo(true)));

        assertThat(this.fieldAt(2, 0).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(2, 0).isActive(false), is(equalTo(true)));
        assertThat(this.fieldAt(2, 1).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(2, 1).isActive(false), is(equalTo(false)));
        assertThat(this.fieldAt(2, 2).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(2, 2).isActive(false), is(equalTo(false)));
        assertThat(this.fieldAt(2, 3).isActive(true), is(equalTo(true)));
        assertThat(this.fieldAt(2, 3).isActive(false), is(equalTo(false)));

        assertThat(this.fieldAt(3, 0).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(3, 0).isActive(false), is(equalTo(false)));
        assertThat(this.fieldAt(3, 1).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(3, 1).isActive(false), is(equalTo(true)));
        assertThat(this.fieldAt(3, 2).isActive(true), is(equalTo(true)));
        assertThat(this.fieldAt(3, 2).isActive(false), is(equalTo(false)));
        assertThat(this.fieldAt(3, 3).isActive(true), is(equalTo(false)));
        assertThat(this.fieldAt(3, 3).isActive(false), is(equalTo(false)));
    }

    /**
     * Tests {@link OthelloFieldImpl#placeToken(boolean)}, black token, first move.
     */
    @Test
    void testPlaceBlackToken1() throws GameException {
        this.fieldAt(0, 1).placeToken(true);
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 2),
                        pos(0, 3),
                        pos(1, 0),
                        pos(1, 3),
                        pos(2, 0),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.BLACK).keySet(),
                containsInAnyOrder(pos(0, 1), pos(1, 1), pos(1, 2), pos(2, 1)));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.WHITE).keySet(), containsInAnyOrder(pos(2, 2)));
    }

    /**
     * Tests {@link OthelloFieldImpl#placeToken(boolean)}, black token, second move.
     */
    @Test
    void testPlaceBlackToken2() throws GameException {
        this.fieldAt(1, 0).placeToken(true);
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 1),
                        pos(0, 2),
                        pos(0, 3),
                        pos(1, 3),
                        pos(2, 0),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.BLACK).keySet(),
                containsInAnyOrder(pos(1, 0), pos(1, 1), pos(1, 2), pos(2, 1)));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.WHITE).keySet(), containsInAnyOrder(pos(2, 2)));
    }

    /**
     * Tests {@link OthelloFieldImpl#placeToken(boolean)}, black token, third move.
     */
    @Test
    void testPlaceBlackToken3() throws GameException {
        this.fieldAt(2, 3).placeToken(true);
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 1),
                        pos(0, 2),
                        pos(0, 3),
                        pos(1, 0),
                        pos(1, 3),
                        pos(2, 0),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.BLACK).keySet(),
                containsInAnyOrder(pos(1, 2), pos(2, 1), pos(2, 2), pos(2, 3)));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.WHITE).keySet(), containsInAnyOrder(pos(1, 1)));
    }

    /**
     * Tests {@link OthelloFieldImpl#placeToken(boolean)}, black token, fourth move.
     */
    @Test
    void testPlaceBlackToken4() throws GameException {
        this.fieldAt(3, 2).placeToken(true);
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 1),
                        pos(0, 2),
                        pos(0, 3),
                        pos(1, 0),
                        pos(1, 3),
                        pos(2, 0),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 3)));
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.BLACK).keySet(),
                containsInAnyOrder(pos(1, 2), pos(2, 1), pos(2, 2), pos(3, 2)));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.WHITE).keySet(), containsInAnyOrder(pos(1, 1)));
    }

    /**
     * Tests {@link OthelloFieldImpl#placeToken(boolean)}, white token, first move.
     */
    @Test
    void testPlaceWhiteToken1() throws GameException {
        this.fieldAt(0, 2).placeToken(false);
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 1),
                        pos(0, 3),
                        pos(1, 0),
                        pos(1, 3),
                        pos(2, 0),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.BLACK).keySet(), containsInAnyOrder(pos(2, 1)));
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.WHITE).keySet(),
                containsInAnyOrder(pos(0, 2), pos(1, 1), pos(1, 2), pos(2, 2)));
    }

    /**
     * Tests {@link OthelloFieldImpl#placeToken(boolean)}, white token, second move.
     */
    @Test
    void testPlaceWhiteToken2() throws GameException {
        this.fieldAt(1, 3).placeToken(false);
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 1),
                        pos(0, 2),
                        pos(0, 3),
                        pos(1, 0),
                        pos(2, 0),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.BLACK).keySet(), containsInAnyOrder(pos(2, 1)));
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.WHITE).keySet(),
                containsInAnyOrder(pos(1, 1), pos(1, 2), pos(1, 3), pos(2, 2)));
    }

    /**
     * Tests {@link OthelloFieldImpl#placeToken(boolean)}, white token, third move.
     */
    @Test
    void testPlaceWhiteToken3() throws GameException {
        this.fieldAt(2, 0).placeToken(false);
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 1),
                        pos(0, 2),
                        pos(0, 3),
                        pos(1, 0),
                        pos(1, 3),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 1),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.BLACK).keySet(), containsInAnyOrder(pos(1, 2)));
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.WHITE).keySet(),
                containsInAnyOrder(pos(1, 1), pos(2, 0), pos(2, 1), pos(2, 2)));
    }

    /**
     * Tests {@link OthelloFieldImpl#placeToken(boolean)}, white token, fourth move.
     */
    @Test
    void testPlaceWhiteToken4() throws GameException {
        this.fieldAt(3, 1).placeToken(false);
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.EMPTY).keySet(),
                containsInAnyOrder(
                        pos(0, 0),
                        pos(0, 1),
                        pos(0, 2),
                        pos(0, 3),
                        pos(1, 0),
                        pos(1, 3),
                        pos(2, 0),
                        pos(2, 3),
                        pos(3, 0),
                        pos(3, 2),
                        pos(3, 3)));
        assertThat(this.board.getFieldsBeing(OthelloFieldState.BLACK).keySet(), containsInAnyOrder(pos(1, 2)));
        assertThat(
                this.board.getFieldsBeing(OthelloFieldState.WHITE).keySet(),
                containsInAnyOrder(pos(1, 1), pos(2, 1), pos(2, 2), pos(3, 1)));
    }

    /**
     * Tests {@link OthelloFieldImpl#placeToken(boolean)}, black token, wrong positions.
     */
    @Test
    void testPlaceBlackTokenWrongMoves() {
        assertThrows(GameException.class, () -> this.fieldAt(0, 0).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(0, 2).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(0, 3).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(1, 1).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(1, 2).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(1, 3).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(2, 0).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(2, 1).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(2, 2).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(3, 0).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(3, 1).placeToken(true));
        assertThrows(GameException.class, () -> this.fieldAt(3, 3).placeToken(true));
    }

    /**
     * Tests {@link OthelloFieldImpl#placeToken(boolean)}, white token, wrong positions.
     */
    @Test
    void testPlaceWhiteTokenWrongMoves() {
        assertThrows(GameException.class, () -> this.fieldAt(0, 0).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(0, 1).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(0, 3).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(1, 0).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(1, 1).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(1, 2).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(2, 1).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(2, 2).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(2, 3).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(3, 0).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(3, 2).placeToken(false));
        assertThrows(GameException.class, () -> this.fieldAt(3, 3).placeToken(false));
    }

    /**
     * Tests {@link OthelloFieldImpl#toString()}.
     */
    @Test
    void testToString() {
        assertThat(this.fieldAt(1, 1).toString(), is(equalTo("OthelloField[position=B2, state=white]")));
        assertThat(this.fieldAt(1, 2).toString(), is(equalTo("OthelloField[position=C2, state=black]")));
    }

    /**
     * Tests {@link OthelloFieldImpl#equals()}.
     */
    @Test
    void testEquals() {
        final OthelloFieldImpl field = this.fieldAt(0, 0);
        assertThat(field, is(equalTo(new OthelloFieldImpl(this.board, pos(0, 0), OthelloFieldState.EMPTY))));
        assertThat(
                field,
                is(
                        equalTo(
                                new OthelloFieldImpl(
                                        new OthelloBoardImpl(this.board.getSize()),
                                        pos(0, 0),
                                        OthelloFieldState.EMPTY))));

        assertThat(field, is(not(equalTo(new OthelloFieldImpl(this.board, pos(0, 1), OthelloFieldState.EMPTY)))));
        assertThat(field, is(not(equalTo(new OthelloFieldImpl(this.board, pos(0, 0), OthelloFieldState.BLACK)))));
        assertThat(field, is(not(equalTo(pos(0, 0)))));
    }

    /**
     * Tests {@link OthelloFieldImpl#hashCode()}.
     */
    @Test
    void testHashCode() {
        final OthelloFieldImpl field = this.fieldAt(0, 0);
        assertThat(
                field.hashCode(),
                is(equalTo(new OthelloFieldImpl(this.board, pos(0, 0), OthelloFieldState.EMPTY).hashCode())));
    }

    /**
     * Returns a position.
     *
     * @param row    The row.
     * @param column The column.
     * @return The position.
     */
    private static OthelloPosition pos(final int row, final int column) {
        return OthelloPosition.of(row, column);
    }

    /**
     * Returns the field at the given position.
     *
     * @param row    The row.
     * @param column The column.
     * @return The field.
     */
    private OthelloFieldImpl fieldAt(final int row, final int column) {
        return this.board.getFieldAt(pos(row, column));
    }
}
