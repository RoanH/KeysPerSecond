package dev.roanh.kps.ui.dialog;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.group.AveragePanelSettings;

public class AvgPanelEditor extends PanelEditor{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -6191919120127582754L;

	public AvgPanelEditor(AveragePanelSettings config, boolean live){
		super(config, live);
		
		labels.add(new JLabel("Precision: "));
		JComboBox<String> values = new JComboBox<String>(new String[]{"No digits beyond the decimal point", "1 digit beyond the decimal point", "2 digits beyond the decimal point", "3 digits beyond the decimal point"});
		fields.add(values);
		values.setSelectedIndex(config.getPrecision());
		values.addActionListener(e->{
			config.setPrecision(values.getSelectedIndex());
			if(live){
				Main.frame.repaint();
			}
		});
	}
}
