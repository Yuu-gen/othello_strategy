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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;

import de.fhdw.gaming.othello.core.domain.OthelloPosition;

/**
 * Tests {@link OthelloPosition}.
 */
class OthelloPositionTest {

    /**
     * Tests {@link OthelloPosition#getRow()}.
     */
    @Test
    void testGetRow() {
        final OthelloPosition position = OthelloPosition.of(1, 2);
        assertThat(position.getRow(), is(equalTo(1)));
    }

    /**
     * Tests {@link OthelloPosition#getColumn()}.
     */
    @Test
    void testGetColumn() {
        final OthelloPosition position = OthelloPosition.of(1, 2);
        assertThat(position.getColumn(), is(equalTo(2)));
    }

    /**
     * Tests {@link OthelloPosition#offset(int, int)}.
     */
    @Test
    void testOffset() {
        final OthelloPosition position = OthelloPosition.of(1, 2).offset(5, -3);
        assertThat(position.getRow(), is(equalTo(6)));
        assertThat(position.getColumn(), is(equalTo(-1)));
    }

    /**
     * Tests {@link OthelloPosition#toString()}.
     */
    @Test
    void testToString() {
        assertThat(OthelloPosition.of(0, 1).toString(), is(equalTo("B1")));
        assertThat(OthelloPosition.of(1, 2).toString(), is(equalTo("C2")));
        assertThat(OthelloPosition.of(3, 5).toString(), is(equalTo("F4")));
        assertThat(OthelloPosition.of(6, 7).toString(), is(equalTo("H7")));
        assertThat(OthelloPosition.of(7, 4).toString(), is(equalTo("E8")));
        assertThat(OthelloPosition.of(2, 3).toString(), is(equalTo("D3")));
        assertThat(OthelloPosition.of(5, 0).toString(), is(equalTo("A6")));
        assertThat(OthelloPosition.of(4, 6).toString(), is(equalTo("G5")));
    }

    /**
     * Tests {@link OthelloPosition#equals(Object)}.
     */
    @Test
    void testEqualsObject() {
        final OthelloPosition position1 = OthelloPosition.of(1, 2);
        final OthelloPosition position2 = OthelloPosition.of(1, 2);
        final OthelloPosition position3 = OthelloPosition.of(1, 1);
        final OthelloPosition position4 = OthelloPosition.of(2, 2);

        assertThat(position1, is(equalTo(position1)));
        assertThat(position1, is(equalTo(position2)));
        assertThat(position2, is(equalTo(position1)));

        assertThat(position1, is(not(equalTo(position3))));
        assertThat(position3, is(not(equalTo(position1))));
        assertThat(position1, is(not(equalTo(position4))));
        assertThat(position4, is(not(equalTo(position1))));

        assertThat(position1, is(not(equalTo(1))));
    }

    /**
     * Tests {@link OthelloPosition#hashCode()}.
     */
    @Test
    void testHashCode() {
        final OthelloPosition position1 = OthelloPosition.of(1, 2);
        final OthelloPosition position2 = OthelloPosition.of(1, 2);
        assertThat(position1.hashCode(), is(equalTo(position2.hashCode())));
    }
}
