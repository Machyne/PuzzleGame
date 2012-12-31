package control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;

import gui.DrawFrame;
import gui.Drawer;

public class ScrollDisplay implements Drawer {
	private static final Dimension DEF_SIZE = new Dimension(480,480);
	private static final int DEF_SCROLL_SCALE = 18; //Pixels:notches
	private static final int DEF_GREY_NUM = 50;
	private static final int FULL_ALPHA = 90;
	private static final int ALPHA_DECAY_SLEEP_TIME = 90;
	private static final int REPAINT_SLEEP_TIME = 33;
	private static final int BAR_UNCLICKED = -1;
	private static final Color SCROLL_COL(int grey, int alpha){
		return new Color(grey,grey,grey,alpha);
	}

	private final DrawFrame frame;
	private final ScrollableView view;
	private final Thread scrollThread;
	private final Thread repaintThread;
	private int currentTop;
	private int gameTall;
	private int alpha;
	private int clickHeight;


	public ScrollDisplay(final ScrollableView view){
		this(view, "", DEF_SCROLL_SCALE);
	}
	
	public ScrollDisplay(final ScrollableView view, final String title){
		this(view, title, DEF_SCROLL_SCALE);
	}

	public ScrollDisplay(final ScrollableView view, final String title, final int scrollScale){
		this.view=view;
		currentTop = 0;
		gameTall = 0;
		alpha = FULL_ALPHA;
		clickHeight = BAR_UNCLICKED;
		frame = new DrawFrame(this, DEF_SIZE, title);
		final MouseWheelListener wheely = new MouseWheelListener(){
			@Override
			public void mouseWheelMoved(final MouseWheelEvent e) {
				alpha = FULL_ALPHA;
				final int notches = e.getWheelRotation();
				final int old = currentTop;
				currentTop=checkScroll(notches*scrollScale+currentTop);
				if(old!=currentTop){
					frame.repaint();
				}
			}
		};
		frame.addMouseWheelListener(wheely);
		final MouseListener clicky = new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				clickHeight = BAR_UNCLICKED;
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if(mouseIsOnScrollBar()){
					clickHeight = frame.getMousePosition().y;
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				final Point p = frame.getMousePosition();
				p.y = p.y+getCurrentTop();
				view.handleClick(p,e.getButton());
			}
		};
		frame.addMouseListener(clicky);
		
		final MouseMotionListener movey = new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {}
			@Override
			public void mouseDragged(MouseEvent e) {
				if(clickHeight != BAR_UNCLICKED){
					final int scrollTarget = e.getY()-clickHeight-30;
					final float scrollTop = (float)currentTop*(float)windowHeight()/(float)gameTall+scrollTarget;
					currentTop = checkScroll( (int)(scrollTop*(float)gameTall/(float)windowHeight()) );
					clickHeight=Math.min(Math.max(0,scrollTarget+clickHeight), windowHeight());
					frame.repaint();
				}
			}
		};
		frame.addMouseMotionListener(movey);
		
		final Runnable repaint = new Runnable() {
			@Override
			public void run() {
				while(true){
					try { Thread.sleep(REPAINT_SLEEP_TIME); } catch (InterruptedException e) {}
					frame.repaint();
				}
			}
		};
		
		final Runnable fader = new Runnable() {
			@Override
			public void run() {
				try { Thread.sleep(7*ALPHA_DECAY_SLEEP_TIME); } catch (InterruptedException e) {}
				while(true){
					try{
					if(mouseIsRight()|| (clickHeight != BAR_UNCLICKED) ){
						alpha = FULL_ALPHA;
					}else if(alpha!=0){
						if(alpha==FULL_ALPHA){
							try{Thread.sleep(7*ALPHA_DECAY_SLEEP_TIME);} catch (InterruptedException err){}
						}
						alpha = Math.max(0,alpha-10);
					}
					//frame.repaint();
					try { Thread.sleep(ALPHA_DECAY_SLEEP_TIME); } catch (InterruptedException e) {}
					}catch(Exception e){/*just in case*/}
				}
			}
		};
		scrollThread = new Thread(fader,"scroll fader");
		scrollThread.start();
		
		repaintThread = new Thread(repaint,"frame repainter");
		repaintThread.start();
	}

	private int getCurrentTop(){
		return currentTop;
	}
	
	private boolean mouseIsRight(){
		return (frame.getMousePosition()==null?false:frame.getMousePosition().x>frame.getDrawSize().width-12);
	}
	
	private boolean mouseIsOnScrollBar(){
		final int height = windowHeight();
		final int mouseHeight = (frame.getMousePosition()==null?-1:frame.getMousePosition().y);
		final int scrollTop = (int)((float)currentTop*(float)height/(float)gameTall);
		final int scrollLength = (int)((float)height*(float)height/(float)gameTall);
		return (mouseIsRight() && (mouseHeight>=scrollTop && mouseHeight<=scrollTop+scrollLength));
	}

	@Override
	public void draw(Graphics g, Image buffer) {
		final int width = frame.getDrawSize().width;
		final int height = windowHeight();
		final Graphics bufferG = buffer.getGraphics();
		currentTop=checkScroll(currentTop);

		int[] viewDim = view.getDim();
		bufferG.drawImage(view.drawImage(frame.getImage(viewDim[0],viewDim[1])),0,0,width,height,0,currentTop,width,currentTop+height,null);

		//Draw Scroll Bar
		if(height<gameTall){
			final int scrollTop = (int)((float)currentTop*(float)height/(float)gameTall);
			final int scrollLength = (int)((float)height*(float)height/(float)gameTall);
			bufferG.setColor(SCROLL_COL(DEF_GREY_NUM,alpha));
			bufferG.fillRoundRect(width-10, scrollTop, 8, scrollLength,5,8);
		}

		g.drawImage(buffer, 0, 0, width, height, null);
	}


	public JFrame getFrame(){
		return frame;
	}

	private int windowHeight() {
		return frame.getDrawSize().height;
	}

	private void fixGameHeight() {
		gameTall = Math.max(view.getDim()[1], windowHeight());
	}

	private int checkScroll(final int target){
		fixGameHeight();
		int ret=Math.min(target,gameTall-windowHeight());
		if(target<=0){
			ret=0;
		}
		return ret;
	}

}
