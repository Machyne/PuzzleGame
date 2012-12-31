package control;

import gui.MouseDraw;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

public class Main {
	public static void main(String args[]){
		//new MouseDraw();
		//new ScrollDisplay(SCROLL_VIEW_TEST);
		new ScrollDisplay(new PuzzleScrollableView(),"Puzzle Game!");
	}
	
	
	public static ScrollableView SCROLL_VIEW_TEST = new ScrollableView() {
		@Override
		public int[] getDim() {
			return new int[]{640,650};
		}
		
		@Override
		public Image drawImage(final Image image) {
			image.getGraphics().setColor(Color.red);
			image.getGraphics().fillRect(20, 20, 40, 50);
			image.getGraphics().drawLine(10, 10, 610, 620);
			return image;
		}

		@Override
		public void handleClick(Point coords, int type) {
			System.out.println(coords.toString()+" type: "+type);
		}
	};
}
