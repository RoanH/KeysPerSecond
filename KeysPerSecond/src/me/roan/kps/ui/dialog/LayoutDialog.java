package me.roan.kps.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import me.roan.kps.GraphMode;
import me.roan.kps.Main;
import me.roan.kps.Main.KeyInformation;
import me.roan.kps.RenderingMode;
import me.roan.kps.layout.LayoutValidator;
import me.roan.kps.layout.Positionable;
import me.roan.kps.panels.BasePanel;
import me.roan.kps.ui.model.DynamicInteger;
import me.roan.kps.ui.model.EndNumberModel;
import me.roan.kps.ui.model.MaxNumberModel;
import dev.roanh.util.Dialog;

/**
 * Logic for the layout configuration dialog.
 * @author Roan
 */
public class LayoutDialog{
	/**
	 * Positionable for the average panel.
	 */
	private static final Positionable avgItem = new Positionable(){

		@Override
		public void setX(int x){
			Main.config.avg_x = x;
		}

		@Override
		public void setY(int y){
			Main.config.avg_y = y;
		}

		@Override
		public void setWidth(int w){
			Main.config.avg_w = w;
		}

		@Override
		public void setHeight(int h){
			Main.config.avg_h = h;
		}

		@Override
		public String getName(){
			return "AVG";
		}

		@Override
		public int getX(){
			return Main.config.avg_x;
		}

		@Override
		public int getY(){
			return Main.config.avg_y;
		}

		@Override
		public int getWidth(){
			return Main.config.avg_w;
		}

		@Override
		public int getHeight(){
			return Main.config.avg_h;
		}

		@Override
		public RenderingMode getRenderingMode(){
			return Main.config.avg_mode;
		}

		@Override
		public void setRenderingMode(RenderingMode mode){
			Main.config.avg_mode = mode;
		}
	};
	/**
	 * Positionable for the maximum panel.
	 */
	private static final Positionable maxItem = new Positionable(){

		@Override
		public void setX(int x){
			Main.config.max_x = x;
		}

		@Override
		public void setY(int y){
			Main.config.max_y = y;
		}

		@Override
		public void setWidth(int w){
			Main.config.max_w = w;
		}

		@Override
		public void setHeight(int h){
			Main.config.max_h = h;
		}

		@Override
		public String getName(){
			return "MAX";
		}

		@Override
		public int getX(){
			return Main.config.max_x;
		}

		@Override
		public int getY(){
			return Main.config.max_y;
		}

		@Override
		public int getWidth(){
			return Main.config.max_w;
		}

		@Override
		public int getHeight(){
			return Main.config.max_h;
		}

		@Override
		public RenderingMode getRenderingMode(){
			return Main.config.max_mode;
		}

		@Override
		public void setRenderingMode(RenderingMode mode){
			Main.config.max_mode = mode;
		}
	};
	/**
	 * Positionable for the current panel.
	 */
	private static final Positionable curItem = new Positionable(){

		@Override
		public void setX(int x){
			Main.config.cur_x = x;
		}

		@Override
		public void setY(int y){
			Main.config.cur_y = y;
		}

		@Override
		public void setWidth(int w){
			Main.config.cur_w = w;
		}

		@Override
		public void setHeight(int h){
			Main.config.cur_h = h;
		}

		@Override
		public String getName(){
			return "CUR";
		}

		@Override
		public int getX(){
			return Main.config.cur_x;
		}

		@Override
		public int getY(){
			return Main.config.cur_y;
		}

		@Override
		public int getWidth(){
			return Main.config.cur_w;
		}

		@Override
		public int getHeight(){
			return Main.config.cur_h;
		}

		@Override
		public RenderingMode getRenderingMode(){
			return Main.config.cur_mode;
		}

		@Override
		public void setRenderingMode(RenderingMode mode){
			Main.config.cur_mode = mode;
		}
	};
	/**
	 * Positionable for the total panel.
	 */
	private static final Positionable totItem = new Positionable(){

		@Override
		public void setX(int x){
			Main.config.tot_x = x;
		}

		@Override
		public void setY(int y){
			Main.config.tot_y = y;
		}

		@Override
		public void setWidth(int w){
			Main.config.tot_w = w;
		}

		@Override
		public void setHeight(int h){
			Main.config.tot_h = h;
		}

		@Override
		public String getName(){
			return "TOT";
		}

		@Override
		public int getX(){
			return Main.config.tot_x;
		}

		@Override
		public int getY(){
			return Main.config.tot_y;
		}

		@Override
		public int getWidth(){
			return Main.config.tot_w;
		}

		@Override
		public int getHeight(){
			return Main.config.tot_h;
		}

		@Override
		public RenderingMode getRenderingMode(){
			return Main.config.tot_mode;
		}

		@Override
		public void setRenderingMode(RenderingMode mode){
			Main.config.tot_mode = mode;
		}
	};

	/**
	 * Shows the layout configuration dialog
	 * @param live Whether or not changes should be
	 *        applied in real time
	 */
	public static final void configureLayout(boolean live){
		Main.content.showGrid();
		JPanel form = new JPanel(new BorderLayout());

		JPanel fields = new JPanel(new GridLayout(0, 5, 2, 2));
		JPanel modes = new JPanel(new GridLayout(0, 1, 0, 2));

		fields.add(new JLabel("Key", SwingConstants.CENTER));
		fields.add(new JLabel("X", SwingConstants.CENTER));
		fields.add(new JLabel("Y", SwingConstants.CENTER));
		fields.add(new JLabel("Width", SwingConstants.CENTER));
		fields.add(new JLabel("Height", SwingConstants.CENTER));
		modes.add(new JLabel("Mode", SwingConstants.CENTER));

		for(KeyInformation i : Main.config.keyinfo){
			createListItem(i, fields, modes, live);
		}
		if(Main.config.showAvg){
			createListItem(avgItem, fields, modes, live);
		}
		if(Main.config.showMax){
			createListItem(maxItem, fields, modes, live);
		}
		if(Main.config.showCur){
			createListItem(curItem, fields, modes, live);
		}
		if(Main.config.showTotal){
			createListItem(totItem, fields, modes, live);
		}

		JPanel keys = new JPanel(new BorderLayout());
		keys.add(fields, BorderLayout.CENTER);
		keys.add(modes, BorderLayout.LINE_END);

		JPanel view = new JPanel(new BorderLayout());
		view.add(keys, BorderLayout.PAGE_START);
		view.add(new JPanel(), BorderLayout.CENTER);
		
		JPanel gridSize = new JPanel(new GridLayout(2, 2, 0, 5));
		gridSize.setBorder(BorderFactory.createTitledBorder("Size"));
		gridSize.add(new JLabel("Cell size: "));
		JSpinner gridSpinner = new JSpinner(new SpinnerNumberModel(Main.config.cellSize, BasePanel.imageSize, Integer.MAX_VALUE, 1));
		gridSize.add(gridSpinner);
		gridSize.add(new JLabel("Panel border offset: "));
		JSpinner gapSpinner = new JSpinner(new SpinnerNumberModel(Main.config.borderOffset, 0, new DynamicInteger(()->(Main.config.cellSize - BasePanel.imageSize)), 1));
		gapSpinner.addChangeListener((e)->{
			Main.config.borderOffset = (int)gapSpinner.getValue();
			if(live){
				Main.reconfigure();
			}
		});
		gridSize.add(gapSpinner);
		gridSpinner.addChangeListener((e)->{
			Main.config.cellSize = (int)gridSpinner.getValue();
			if(Main.config.borderOffset > Main.config.cellSize - BasePanel.imageSize){
				Main.config.borderOffset = Main.config.cellSize - BasePanel.imageSize;
				gapSpinner.setValue(Main.config.borderOffset);
			}
			if(live){
				Main.reconfigure();
			}
		});
		
		form.add(gridSize, BorderLayout.PAGE_START);
		
		JScrollPane pane = new JScrollPane(view);
		pane.setBorder(BorderFactory.createTitledBorder("Panels"));
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setPreferredSize(new Dimension(450, 200));

		form.add(pane, BorderLayout.CENTER);

		JPanel graphLayout = new JPanel(new GridLayout(5, 2, 0, 5));
		graphLayout.setBorder(BorderFactory.createTitledBorder("Graph"));
		graphLayout.add(new JLabel("Graph mode: "));
		JComboBox<Object> graphMode = new JComboBox<Object>(GraphMode.values());
		graphMode.setSelectedItem(Main.config.graphMode);
		graphLayout.add(graphMode);

		LayoutValidator validator = new LayoutValidator();

		graphLayout.add(new JLabel("Graph x position: "));
		JSpinner x = new JSpinner(new EndNumberModel(Main.config.graph_x, validator.getXField(), (val)->{
			Main.config.graph_x = val;
			if(live){
				Main.reconfigure();
			}
		}));
		x.setEnabled(Main.config.graphMode == GraphMode.INLINE);
		graphLayout.add(x);

		graphLayout.add(new JLabel("Graph y position: "));
		JSpinner y = new JSpinner(new EndNumberModel(Main.config.graph_y, validator.getYField(), (val)->{
			Main.config.graph_y = val;
			if(live){
				Main.reconfigure();
			}
		}));
		y.setEnabled(Main.config.graphMode == GraphMode.INLINE);
		graphLayout.add(y);

		graphLayout.add(new JLabel("Graph width: "));
		JSpinner w = new JSpinner(new MaxNumberModel(Main.config.graph_w, validator.getWidthField(), (val)->{
			Main.config.graph_w = val;
			if(live){
				Main.reconfigure();
			}
		}));
		graphLayout.add(w);

		graphLayout.add(new JLabel("Graph height: "));
		JSpinner h = new JSpinner(new MaxNumberModel(Main.config.graph_h, validator.getHeightField(), (val)->{
			Main.config.graph_h = val;
			if(live){
				Main.reconfigure();
			}
		}));
		graphLayout.add(h);

		graphMode.addActionListener((e)->{
			Main.config.graphMode = (GraphMode)graphMode.getSelectedItem();
			if(graphMode.getSelectedItem() == GraphMode.INLINE){
				x.setEnabled(true);
				y.setEnabled(true);
			}else{
				x.setEnabled(false);
				y.setEnabled(false);
			}
			if(live){
				Main.reconfigure();
			}
		});

		form.add(graphLayout, BorderLayout.PAGE_END);

		Dialog.showMessageDialog(form, true);
		Main.content.hideGrid();
	}

	/**
	 * Creates a editable list item for the
	 * layout configuration dialog
	 * @param info The positionable that links the 
	 *        editor to the underlying data
	 * @param fields The GUI panel that holds all the fields
	 * @param modes The GUI panel that holds all the modes
	 * @param live Whether or not edits should be displayed in real time
	 */
	private static final void createListItem(Positionable info, JPanel fields, JPanel modes, boolean live){
		fields.add(new JLabel(info.getName(), SwingConstants.CENTER));

		LayoutValidator validator = new LayoutValidator();

		JSpinner x = new JSpinner(new EndNumberModel(info.getX(), validator.getXField(), (val)->{
			info.setX(val);
			if(live){
				Main.reconfigure();
			}
		}));
		fields.add(x);

		JSpinner y = new JSpinner(new EndNumberModel(info.getY(), validator.getYField(), (val)->{
			info.setY(val);
			if(live){
				Main.reconfigure();
			}
		}));
		fields.add(y);

		JSpinner w = new JSpinner(new MaxNumberModel(info.getWidth(), validator.getWidthField(), (val)->{
			info.setWidth(val);
			if(live){
				Main.reconfigure();
			}
		}));
		fields.add(w);

		JSpinner h = new JSpinner(new MaxNumberModel(info.getHeight(), validator.getHeightField(), (val)->{
			info.setHeight(val);
			if(live){
				Main.reconfigure();
			}
		}));
		fields.add(h);

		JComboBox<RenderingMode> mode = new JComboBox<RenderingMode>(RenderingMode.values());
		mode.setSelectedItem(info.getRenderingMode());
		mode.addActionListener((e)->{
			info.setRenderingMode((RenderingMode)mode.getSelectedItem());
			if(live){
				Main.reconfigure();
			}
		});
		modes.add(mode);
	}
}
