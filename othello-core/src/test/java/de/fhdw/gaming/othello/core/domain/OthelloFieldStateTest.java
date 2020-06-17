/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-core.
 *
 * Othello-core is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Othello-core is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * othello-core. If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.othello.core.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link OthelloFieldState}.
 */
class OthelloFieldStateTest {

    /**
     * Tests {@link OthelloFieldState#inverse()}.
     */
    @Test
    void testInverse() {
        assertThat(OthelloFieldState.EMPTY.inverse(), is(equalTo(OthelloFieldState.EMPTY)));
        assertThat(OthelloFieldState.BLACK.inverse(), is(equalTo(OthelloFieldState.WHITE)));
        assertThat(OthelloFieldState.WHITE.inverse(), is(equalTo(OthelloFieldState.BLACK)));
    }

    /**
     * Tests {@link OthelloFieldState#toString()}.
     */
    @Test
    void testToString() {
        assertThat(OthelloFieldState.EMPTY.toString(), is(equalTo("empty")));
        assertThat(OthelloFieldState.BLACK.toString(), is(equalTo("black")));
        assertThat(OthelloFieldState.WHITE.toString(), is(equalTo("white")));
    }
}
