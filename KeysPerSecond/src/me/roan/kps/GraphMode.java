package me.roan.kps;

import java.awt.BorderLayout;

/**
 * Enum specifying all the different
 * layout positions for the graph
 * @author Roan
 */
public enum GraphMode {
	/**
	 * Indicates that the graph is placed
	 * under the other components
	 */
	Bottom(BorderLayout.PAGE_END) {
		@Override
		protected int getAddedWidth() {
			return 0;
		}

		@Override
		protected int getAddedHeight() {
			return Main.config.graphHeight;
		}
	},
	/**
	 * Indicates that the graph is placed
	 * above the other components
	 */
	Top(BorderLayout.PAGE_START) {
		@Override
		protected int getAddedWidth() {
			return 0;
		}

		@Override
		protected int getAddedHeight() {
			return Main.config.graphHeight;
		}
	},
	/**
	 * Indicates that the graph is placed
	 * on the left side of the other components
	 */
	Left(BorderLayout.LINE_START) {
		@Override
		protected int getAddedWidth() {
			return Main.config.graphWidth;
		}

		@Override
		protected int getAddedHeight() {
			return 0;
		}
	},
	/**
	 * Indicates that the graph is placed
	 * on the right side of the other components
	 */
	Right(BorderLayout.LINE_END) {
		@Override
		protected int getAddedWidth() {
			return Main.config.graphWidth;
		}

		@Override
		protected int getAddedHeight() {
			return 0;
		}
	},
	/**
	 * Indicates that the graph is placed
	 * in it's own window
	 */
	Detached(null) {
		@Override
		protected int getAddedWidth() {
			return 0;
		}

		@Override
		protected int getAddedHeight() {
			return 0;
		}
	};
	
	/**
	 * BorderLayout constraint for
	 * the layout position
	 * @see BorderLayout
	 */
	protected final String layoutPosition;
	
	/**
	 * Constructs a new graph mode
	 * with the given BorderLayout
	 * constraint
	 * @param pos The BorderLayout
	 *        constraint
	 * @see BorderLayout
	 */
	private GraphMode(String pos){
		layoutPosition = pos;
	}
	
	/**
	 * Returns the width needed by the graph
	 * in the main window
	 * @return The width of the graph
	 */
	protected abstract int getAddedWidth();
	
	/**
	 * Returns the height needed by the graph
	 * in the main window
	 * @return The height of the graph
	 */
	protected abstract int getAddedHeight();
}
