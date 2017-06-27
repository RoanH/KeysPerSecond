package me.roan.kps;

import java.awt.BorderLayout;

public enum GraphMode {

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
	
	protected final String layoutPosition;
	
	private GraphMode(String pos){
		layoutPosition = pos;
	}
	
	protected abstract int getAddedWidth();
	
	protected abstract int getAddedHeight();
}
