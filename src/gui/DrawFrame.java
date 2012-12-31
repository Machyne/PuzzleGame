package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class DrawFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private final DrawPanel panel;
	private final JPanel fake;
	private final static Dimension DEF_SIZE = new Dimension(640,480);
	
	public DrawFrame(final Drawer d){
		this(d,DEF_SIZE,"");
	}

	public DrawFrame(final Drawer d, final String title){
		this(d,DEF_SIZE,title);
	}

	public DrawFrame(final Drawer d, final Dimension dim){
		this(d,dim,"");
	}
	
	public DrawFrame(final Drawer d, final Dimension dim, final String title){
		super(title);
		panel=new DrawPanel(d);
		fake=new JPanel(){
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(final Graphics g){
				//
			}
		};
		setup(dim);
	}
	
	private void setup(final Dimension dim){
		setSize(dim);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(panel);
		panel.add(fake);
		setVisible(true);
	}

    public Dimension getDrawSize(){
    	return panel.getSize();
    }
    
    public Image getImage(final int width, final int height){
    	return fake.createImage(width,height);
    }
    
    @Override
    public Point getMousePosition(){
    	return checkBounds(panel.getMousePosition());
    }
    
    @Override
    public Point getMousePosition(boolean allowChildren){
    	return checkBounds(panel.getMousePosition(allowChildren));
    }

	private Point checkBounds(final Point raw) {
		final Point ret;
		if(raw!=null){
			final Dimension dim = getDrawSize();
			final boolean isGood = isInside(raw.x,0,dim.width,true)&&isInside(raw.y,0,dim.height,true);
			ret = (isGood? raw: null);
		}else{
			ret=null;
		}
		return ret;
	}
	
	private boolean isInside(final int val, final int low, final int high, final boolean isInclusive){
		return (isInclusive? (val>=low && val<=high)
				:(val>low && val<high));
	}
	
	@Override
	public void repaint(){
		super.repaint();
		panel.repaint();
	}
    
	
	private class DrawPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		private final Drawer d;
    	
    	public DrawPanel(final Drawer d){
    		super();
    		setLayout(null);
    		this.d=d;
    	}
    	
    	public Image getImage(){
    		final Dimension size = getSize();
			final int x = size.width;
        	final int y = size.height;
        	return createImage(x,y);
    	}
    	
        @Override
        public void paintComponent(final Graphics g){
        	d.draw(g,getImage());
        }
    }


	public void addPanelMouseListener(final MouseListener l) {
		panel.addMouseListener(l);
	}
}
