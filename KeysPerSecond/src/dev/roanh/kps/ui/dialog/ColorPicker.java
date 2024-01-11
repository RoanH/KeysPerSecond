package dev.roanh.kps.ui.dialog;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

import dev.roanh.kps.Main;
import dev.roanh.kps.config.ThemeColor;
import dev.roanh.util.Dialog;

public class ColorPicker extends JPanel implements MouseListener{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 3576402284968096657L;
	private Supplier<ThemeColor> read;
	private Consumer<ThemeColor> write;
	/**
	 * Whether or not the color chooser is open
	 */
	private boolean open = false;
	/**
	 * Color chooser instance
	 */
	private final JColorChooser chooser = new JColorChooser();
	
	public ColorPicker(Supplier<ThemeColor> read, Consumer<ThemeColor> write, boolean live){
		this.read = read;
		this.write = c->{
			write.accept(c);
			if(live){
				Main.reconfigure();
			}
		};
		
		setBackground(read.get().getColor());
		this.addMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		if(!open){
			open = true;
			chooser.setColor(getBackground());
			if(Dialog.showSaveDialog(chooser)){
				setBackground(chooser.getColor());
				write.accept(new ThemeColor(chooser.getColor().getRGB(), read.get().getAlpha()));
			}
			open = false;
		}
	}

	@Override
	public void mousePressed(MouseEvent e){
	}

	@Override
	public void mouseReleased(MouseEvent e){
	}

	@Override
	public void mouseEntered(MouseEvent e){
	}

	@Override
	public void mouseExited(MouseEvent e){
	}
}
