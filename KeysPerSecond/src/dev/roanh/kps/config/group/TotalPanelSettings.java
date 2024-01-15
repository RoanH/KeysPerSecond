/*
 * KeysPerSecond: An open source input statistics displayer.
 * Copyright (C) 2017  Roan Hofland (roan@roanh.dev).  All rights reserved.
 * GitHub Repository: https://github.com/RoanH/KeysPerSecond
 *
 * KeysPerSecond is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeysPerSecond is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.roanh.kps.config.group;

import java.util.List;

import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.legacy.LegacyProxyStore;
import dev.roanh.kps.config.legacy.ProxySetting;
import dev.roanh.kps.panels.TotPanel;
import dev.roanh.kps.ui.editor.DataPanelEditor;
import dev.roanh.kps.ui.editor.Editor;

/**
 * Configuration for a total panel.
 * @author Roan
 * @see TotPanel
 */
public class TotalPanelSettings extends SpecialPanelSettings implements LegacyProxyStore{

	/**
	 * Constructs new total panel settings.
	 */
	public TotalPanelSettings(){
		super(PanelType.TOTAL, "TOT");
	}

	@Override
	public TotPanel createPanel(){
		return new TotPanel(this);
	}
	
	@Override
	public void showEditor(boolean live){
		Editor.showEditor(new DataPanelEditor(this, live));
	}
	
	@Override
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(ProxySetting.of("totX", x));
		proxyList.add(ProxySetting.of("totY", y));
		proxyList.add(ProxySetting.of("totWidth", width));
		proxyList.add(ProxySetting.of("totHeight", height));
		proxyList.add(ProxySetting.of("totMode", mode));
	}
}
