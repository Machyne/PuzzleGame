package control;

import java.awt.Image;
import java.awt.Point;

public interface ScrollableView {
	int[] getDim();
	Image drawImage(Image image);
	void handleClick(Point coords, int type);
}
