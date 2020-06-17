/*
 * Copyright © 2020 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of othello-gui.
 *
 * Othello-gui is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Othello-gui is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with othello-gui. If not, see
 * <http://www.gnu.org/licenses/>.
 */
module de.fhdw.gaming.othello.gui {
    exports de.fhdw.gaming.othello.gui;
    exports de.fhdw.gaming.othello.gui.event;

    requires transitive de.fhdw.gaming.othello.core;
    requires transitive de.fhdw.gaming.gui;

    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive javafx.fxml;

    provides de.fhdw.gaming.gui.GuiObserverFactory with de.fhdw.gaming.othello.gui.impl.OthelloGuiObserverFactoryImpl;
    provides de.fhdw.gaming.othello.core.domain.factory.OthelloStrategyFactory
            with de.fhdw.gaming.othello.gui.impl.OthelloInteractiveStrategyFactory;

    opens de.fhdw.gaming.othello.gui.impl;
}
