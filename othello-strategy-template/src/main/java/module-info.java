/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-strategy-template.
 *
 * Othello-strategy-template is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Othello-strategy-template is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with othello-strategy-template. If not, see
 * <http://www.gnu.org/licenses/>.
 */
module de.fhdw.gaming.othello.strategy.template {
    requires de.fhdw.gaming.core;
    requires de.fhdw.gaming.othello.core;

    provides de.fhdw.gaming.othello.core.domain.factory.OthelloStrategyFactory
            with de.fhdw.gaming.othello.strategy.template.OthelloMyStrategyFactory;
}
