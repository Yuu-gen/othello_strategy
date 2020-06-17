/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of gaming-core.
 *
 * Gaming-core is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Gaming-core is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * gaming-core. If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.othello.core.domain.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Implements {@link OthelloStrategyFactoryProvider} by using the {@link ServiceLoader}.
 */
public final class OthelloDefaultStrategyFactoryProvider implements OthelloStrategyFactoryProvider {

    @Override
    public List<OthelloStrategyFactory> getStrategyFactories() {
        final ServiceLoader<OthelloStrategyFactory> services = ServiceLoader.load(OthelloStrategyFactory.class);
        final List<OthelloStrategyFactory> list = new ArrayList<>();
        services.iterator().forEachRemaining(list::add);
        return list;
    }
}
