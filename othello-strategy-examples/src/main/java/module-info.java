/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-strategy-examples.
 *
 * Othello-strategy-examples is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Othello-strategy-examples is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with othello-strategy-examples. If not, see
 * <http://www.gnu.org/licenses/>.
 */
module de.fhdw.gaming.othello.strategy.examples {
    requires de.fhdw.gaming.core;
    requires de.fhdw.gaming.othello.core;

    provides de.fhdw.gaming.othello.core.domain.factory.OthelloStrategyFactory
            with de.fhdw.gaming.othello.strategy.examples.first.OthelloFirstMoveFoundStrategyFactory,
            de.fhdw.gaming.othello.strategy.examples.random.OthelloRandomMoveStrategyFactory,
            de.fhdw.gaming.othello.strategy.examples.maxFlips.OthelloMaxFlipsMoveStrategyFactory,
            de.fhdw.gaming.othello.strategy.examples.minOppMob.OthelloMinOppMobStrategyFactory,
            de.fhdw.gaming.othello.strategy.examples.minMaxD3.OthelloMinMaxD3StrategyFactory,
            de.fhdw.gaming.othello.strategy.examples.MinMaxANG.OthelloMinMaxANGFactory,
            de.fhdw.gaming.othello.strategy.examples.MinMax.OthelloMinMaxFactory,
            de.fhdw.gaming.othello.strategy.examples.MinMaxCombi2.OthelloMinMaxCombi2Factory,
            de.fhdw.gaming.othello.strategy.examples.MinMaxCombi3.OthelloMinMaxCombi3Factory;
}
