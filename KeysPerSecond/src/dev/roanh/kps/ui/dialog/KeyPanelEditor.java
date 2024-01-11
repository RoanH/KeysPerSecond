package dev.roanh.kps.ui.dialog;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.group.KeyPanelSettings;

public class KeyPanelEditor extends PanelEditor{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 8336531868831372183L;

	public KeyPanelEditor(KeyPanelSettings config, boolean live){
		super(config, live);
		
		labels.add(new JLabel("Visible: "));
		JCheckBox visible = new JCheckBox("", config.isVisible());
		fields.add(visible);
		visible.addActionListener(e->{
			config.setVisible(visible.isSelected());
			Main.reconfigure();
		});
	}
}
