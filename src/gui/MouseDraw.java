package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseDraw implements Drawer{
	private final DrawFrame frame;
	private Point source = new Point(0,0);
	private Color col = Color.black;
	
	public MouseDraw(){
		frame = new DrawFrame(this, "Mouse Follower!");
		frame.addPanelMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
				final Point click = e.getPoint();
				if(e.getButton()==MouseEvent.BUTTON1){
					source=click;
				}else if(e.getButton()==MouseEvent.BUTTON3){
					final Dimension dim = frame.getSize();
					final float rat = (float)(click.x+click.y)/(float)(dim.width+dim.height);
					final int colNum = (int)(255*rat);
					col = new Color(colNum,0,255-colNum);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		while(true){
			frame.repaint();
		}
	}

	private Point getMouse(){
		final Point onFrame = frame.getMousePosition();
		return (onFrame==null?source:onFrame);
	}
	
	@Override
	public void draw(Graphics g, Image buffer) {
		final int width = buffer.getWidth(null)-1;
        final int height = buffer.getHeight(null)-1;
		final Graphics bufferG = buffer.getGraphics();
		final Point mouseLoc = getMouse();
		bufferG.setColor(col);
        bufferG.drawLine(source.x, source.y, mouseLoc.x, mouseLoc.y);
        
        g.drawImage(buffer, 0, 0, width, height, null);
	}
}
