package dev.roanh.kps.ui.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.ThemeColor;
import dev.roanh.kps.config.group.ThemeSettings;
import dev.roanh.util.Dialog;

public class ColorDialog extends JPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -5867337735121395139L;

	public ColorDialog(ThemeSettings config, boolean live){
		super(new GridLayout(2, 3, 4, 2));
		
		//foreground
		JLabel lfg = new JLabel("Foreground colour: ");
		ColorPicker cfg = new ColorPicker(config::getForeground, config::setForeground, live);

		JPanel spanelfg = new JPanel(new BorderLayout());
		JSpinner sfg = new JSpinner(new SpinnerNumberModel(config.getForeground().getAlpha() * 100.0D, 0.0D, 100.0D, 5.0D));
		spanelfg.add(new JLabel("Opacity (%): "), BorderLayout.LINE_START);
		spanelfg.add(sfg, BorderLayout.CENTER);
		sfg.addChangeListener(e->{
			config.setForeground(new ThemeColor(config.getForeground().getRGB(), (float)((double)sfg.getValue() / 100.0D)));
			if(live){
				Main.reconfigure();
			}
		});

		//background
		JLabel lbg = new JLabel("Background colour: ");
		ColorPicker cbg = new ColorPicker(config::getBackground, config::setBackground, live);

		JPanel spanelbg = new JPanel(new BorderLayout());
		JSpinner sbg = new JSpinner(new SpinnerNumberModel(config.getBackground().getAlpha() * 100.0D, 0.0D, 100.0D, 5.0D));
		spanelbg.add(new JLabel("Opacity (%): "), BorderLayout.LINE_START);
		spanelbg.add(sbg, BorderLayout.CENTER);
		sbg.addChangeListener(e->{
			config.setBackground(new ThemeColor(config.getBackground().getRGB(), (float)((double)sbg.getValue() / 100.0D)));
			if(live){
				Main.reconfigure();
			}
		});

		add(lfg);
		add(cfg);
		add(spanelfg);
		add(lbg);
		add(cbg);
		add(spanelbg);
	}
	
	/**
	 * Shows the color configuration dialog
	 */
	public static final void configureColors(ThemeSettings config, boolean live){
		if(!config.hasCustomColors()){
			Dialog.showMessageDialog("Please enable custom colours first.");
			return;
		}
		
		ThemeColor foreground = config.getForeground();
		ThemeColor background = config.getBackground();
		if(!Dialog.showSaveDialog(new ColorDialog(config, live))){
			config.setForeground(foreground);
			config.setBackground(background);
		}

		Main.reconfigure();
	}
}
