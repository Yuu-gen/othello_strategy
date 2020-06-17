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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fhdw.gaming.othello.core.domain.OthelloField;
import de.fhdw.gaming.othello.core.domain.OthelloFieldState;
import de.fhdw.gaming.othello.core.domain.OthelloPosition;

/**
 * Tests {@link OthelloBoardImpl}.
 */
class OthelloBoardImplTest {

    /**
     * The {@link OthelloBoardImpl} under test.
     */
    private OthelloBoardImpl board;

    /**
     * Creates a {@link OthelloBoardImpl} to test.
     */
    @BeforeEach
    public void setUp() {
        this.board = new OthelloBoardImpl(4);
    }

    /**
     * Tests {@link OthelloBoardImpl#getSize()}.
     */
    @Test
    void testGetSize() {
        assertThat(this.board.getSize(), is(equalTo(4)));
    }

    /**
     * Tests creating an {@link OthelloBoardImpl} with an unsupported size.
     */
    @Test
    void testWrongSize() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OthelloBoardImpl(5),
                "Should fail with uneven number of rows/columns");
        assertThrows(
                IllegalArgumentException.class,
                () -> new OthelloBoardImpl(2),
                "Should fail with number of rows/columns <= 4");
        assertThrows(
                IllegalArgumentException.class,
                () -> new OthelloBoardImpl(-2),
                "Should fail with number of rows/columns <= 4");
    }

    /**
     * Tests {@link OthelloBoardImpl#hasFieldAt(de.fhdw.gaming.othello.core.domain.OthelloPosition)}.
     */
    @Test
    void testHasFieldAt() {
        assertThat(this.board.hasFieldAt(pos(-1, -1)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(-1, 0)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(-1, 1)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(-1, 2)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(-1, 3)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(-1, 4)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(0, -1)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(0, 0)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(0, 1)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(0, 2)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(0, 3)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(0, 4)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(1, -1)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(1, 0)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(1, 1)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(1, 2)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(1, 3)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(1, 4)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(2, -1)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(2, 0)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(2, 1)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(2, 2)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(2, 3)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(2, 4)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(3, -1)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(3, 0)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(3, 1)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(3, 2)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(3, 3)), is(equalTo(true)));
        assertThat(this.board.hasFieldAt(pos(3, 4)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(4, -1)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(4, 0)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(4, 1)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(4, 2)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(4, 3)), is(equalTo(false)));
        assertThat(this.board.hasFieldAt(pos(4, 4)), is(equalTo(false)));
    }

    /**
     * Tests {@link OthelloBoardImpl#getFieldAt(de.fhdw.gaming.othello.core.domain.OthelloPosition)}.
     */
    @Test
    void testGetFieldAt() {
        final OthelloFieldImpl field00 = this.board.getFieldAt(pos(0, 0));
        assertThat(field00.getBoard(), is(sameInstance(this.board)));
        assertThat(field00.getPosition(), is(equalTo(pos(0, 0))));
        assertThat(field00.getState(), is(equalTo(OthelloFieldState.EMPTY)));

        final OthelloFieldImpl field11 = this.board.getFieldAt(pos(1, 1));
        assertThat(field11.getBoard(), is(sameInstance(this.board)));
        assertThat(field11.getPosition(), is(equalTo(pos(1, 1))));
        assertThat(field11.getState(), is(equalTo(OthelloFieldState.WHITE)));

        final OthelloFieldImpl field21 = this.board.getFieldAt(pos(2, 1));
        assertThat(field21.getBoard(), is(sameInstance(this.board)));
        assertThat(field21.getPosition(), is(equalTo(pos(2, 1))));
        assertThat(field21.getState(), is(equalTo(OthelloFieldState.BLACK)));

        assertThrows(IllegalArgumentException.class, () -> this.board.getFieldAt(pos(4, 4)));
    }

    /**
     * Tests {@link OthelloBoardImpl#getFields()}.
     */
    @Test
    void testGetFields() {
        final List<List<? extends OthelloField>> fields = this.board.getFields();
        assertThat(fields, hasSize(4));
        assertThat(
                fields,
                contains(
                        Arrays.asList(this.fieldAt(0, 0), this.fieldAt(0, 1), this.fieldAt(0, 2), this.fieldAt(0, 3)),
                        Arrays.asList(this.fieldAt(1, 0), this.fieldAt(1, 1), this.fieldAt(1, 2), this.fieldAt(1, 3)),
                        Arrays.asList(this.fieldAt(2, 0), this.fieldAt(2, 1), this.fieldAt(2, 2), this.fieldAt(2, 3)),
                        Arrays.asList(this.fieldAt(3, 0), this.fieldAt(3, 1), this.fieldAt(3, 2), this.fieldAt(3, 3))));
    }

    /**
     * Tests {@link OthelloBoardImpl#getFieldsBeing(de.fhdw.gaming.othello.core.domain.OthelloFieldState)}.
     */
    @Test
    void testGetFieldsBeing() {
        Map<OthelloPosition, OthelloFieldImpl> blackFields = this.board.getFieldsBeing(OthelloFieldState.BLACK);
        assertThat(blackFields.keySet(), hasSize(2));
        assertThat(blackFields.keySet(), containsInAnyOrder(pos(1, 2), pos(2, 1)));
        assertThat(blackFields.get(pos(1, 2)), is(sameInstance(this.board.getFieldAt(pos(1, 2)))));
        assertThat(blackFields.get(pos(2, 1)), is(sameInstance(this.board.getFieldAt(pos(2, 1)))));

        Map<OthelloPosition, OthelloFieldImpl> whiteFields = this.board.getFieldsBeing(OthelloFieldState.WHITE);
        assertThat(whiteFields.keySet(), hasSize(2));
        assertThat(whiteFields.keySet(), containsInAnyOrder(pos(1, 1), pos(2, 2)));
        assertThat(whiteFields.get(pos(1, 1)), is(sameInstance(this.board.getFieldAt(pos(1, 1)))));
        assertThat(whiteFields.get(pos(2, 2)), is(sameInstance(this.board.getFieldAt(pos(2, 2)))));

        this.board.getFieldAt(pos(1, 1)).setState(OthelloFieldState.BLACK);

        blackFields = this.board.getFieldsBeing(OthelloFieldState.BLACK);
        assertThat(blackFields.keySet(), hasSize(3));
        assertThat(blackFields.keySet(), containsInAnyOrder(pos(1, 1), pos(1, 2), pos(2, 1)));
        assertThat(blackFields.get(pos(1, 1)), is(sameInstance(this.board.getFieldAt(pos(1, 1)))));
        assertThat(blackFields.get(pos(1, 2)), is(sameInstance(this.board.getFieldAt(pos(1, 2)))));
        assertThat(blackFields.get(pos(2, 1)), is(sameInstance(this.board.getFieldAt(pos(2, 1)))));

        whiteFields = this.board.getFieldsBeing(OthelloFieldState.WHITE);
        assertThat(whiteFields.keySet(), hasSize(1));
        assertThat(whiteFields.keySet(), containsInAnyOrder(pos(2, 2)));
        assertThat(whiteFields.get(pos(2, 2)), is(sameInstance(this.board.getFieldAt(pos(2, 2)))));
    }

    /**
     * Tests {@link OthelloBoardImpl#deepCopy()}.
     */
    @Test
    void testDeepCopy() {
        final OthelloBoardImpl copy = this.board.deepCopy();
        assertThat(copy, is(equalTo(this.board)));

        // change original, copy must not change (and be different from the copy)
        this.board.getFieldAt(pos(0, 1)).setState(OthelloFieldState.BLACK);
        assertThat(copy.getFieldAt(pos(0, 1)).getState(), is(equalTo(OthelloFieldState.EMPTY)));
        assertThat(copy, is(not(equalTo(this.board))));

        // change copy, original must not change (and be different from the copy)
        copy.getFieldAt(pos(0, 1)).setState(OthelloFieldState.WHITE);
        assertThat(this.board.getFieldAt(pos(0, 1)).getState(), is(equalTo(OthelloFieldState.BLACK)));
        assertThat(copy, is(not(equalTo(this.board))));

        // make copy equal to original, equals() must return true
        copy.getFieldAt(pos(0, 1)).setState(OthelloFieldState.BLACK);
        assertThat(copy, is(equalTo(this.board)));
    }

    /**
     * Tests {@link OthelloBoardImpl#toString()}.
     */
    @Test
    void testToString() {
        assertThat(
                this.board.toString(),
                is(
                        equalTo(
                                "OthelloBoard[size=4, fields=[[OthelloField[position=A1, state=empty], "
                                        + "OthelloField[position=B1, state=empty], "
                                        + "OthelloField[position=C1, state=empty], "
                                        + "OthelloField[position=D1, state=empty]], ["
                                        + "OthelloField[position=A2, state=empty], "
                                        + "OthelloField[position=B2, state=white], "
                                        + "OthelloField[position=C2, state=black], "
                                        + "OthelloField[position=D2, state=empty]], ["
                                        + "OthelloField[position=A3, state=empty], "
                                        + "OthelloField[position=B3, state=black], "
                                        + "OthelloField[position=C3, state=white], "
                                        + "OthelloField[position=D3, state=empty]], ["
                                        + "OthelloField[position=A4, state=empty], "
                                        + "OthelloField[position=B4, state=empty], "
                                        + "OthelloField[position=C4, state=empty], "
                                        + "OthelloField[position=D4, state=empty]]]]")));
    }

    /**
     * Tests {@link OthelloBoardImpl#equals()}.
     */
    @Test
    void testEquals() {
        final OthelloBoardImpl other = new OthelloBoardImpl(4);
        assertThat(this.board, is(equalTo(other)));
        other.getFieldAt(pos(0, 0)).setState(OthelloFieldState.BLACK);
        assertThat(this.board, is(not(equalTo(other))));

        assertThat(this.board, is(not(equalTo(new OthelloBoardImpl(6)))));
        assertThat(this.board, is(not(equalTo(4))));
    }

    /**
     * Tests {@link OthelloBoardImpl#hashCode()}.
     */
    @Test
    void testHashCode() {
        assertThat(this.board.hashCode(), is(equalTo(new OthelloBoardImpl(4).hashCode())));
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
