package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;

public class MKButton extends AbstractButton {
	private static final long serialVersionUID = 1L;
	private int hBorder_ = 6;
    private static final int vBorder_ = 2;
    
    private final Point UL; //upper-left coordinates
    private String displayText;
    private Font font;
    private FontMetrics metrics;
    private String actionCommand;
    private final ArrayList<ActionListener> actionListeners;
    private int width;
    private int height;

    public MKButton(final String displayText, final Font font, final String actionCommand) {
        super();
        this.UL = new Point(0,0);
        this.displayText = displayText;
        this.font = font;
        this.metrics = null;
        this.actionCommand = actionCommand;
        this.actionListeners = new ArrayList<ActionListener>();
        this.width = 0;
        this.height = 0;
    }

    public MKButton(final MKButton button){
    	super();
    	this.UL = new Point(button.UL);
    	this.displayText = new String(button.displayText);
    	this.font = button.font;
    	this.metrics = null;
    	this.actionCommand = new String(button.actionCommand);
    	this.actionListeners = new ArrayList<ActionListener>();
    	this.width = button.width;
    	this.height = button.height;
    }
    
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setText(final String s) {
        displayText = s;
    }

    public void setFont(final Font f) {
        font = f;
    }

    private void setFontMetricsWithGraphics(final Graphics g) {
        g.setFont(font);
        metrics = g.getFontMetrics();
        calculateWidthAndHeight();
    }

    public void setActionCommand(final String action) {
        actionCommand = action;
    }

    public void addActionListener(final ActionListener a) {
        actionListeners.add(a);
    }

    private void calculateWidthAndHeight() {
        width = metrics.stringWidth(displayText) + hBorder_ * 2;
        height = metrics.getHeight() + vBorder_ * 2;
    }

    public void setLocation(final int x, final int y) {
        UL.setLocation(x, y);
    }
    
    public Point getLocation(){
    	return new Point(UL);
    }

    public boolean inBounds(final Point coordinates) {
        return inBounds(coordinates.x,coordinates.y);
    }
    
    public boolean inBounds(final int x, final int y) {
        return (x >= UL.x) && (x < UL.x + width) && (y >= UL.y) && (y < UL.y + height);
    }

    public void doClick() {
        for(ActionListener a:actionListeners){
        	a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand));
        }
    }

    public void show(final Graphics g) {
        setFontMetricsWithGraphics(g);
    	g.setColor(new Color(216, 216, 216));
        g.fillRect(UL.x, UL.y, width, height);
        g.setColor(Color.black);
        g.setFont(font);
        int textWidth = metrics.stringWidth(displayText);
        int textHeight = metrics.getHeight();
        g.drawString(displayText, UL.x + (width - textWidth) / 2, UL.y + height - textHeight/4);
    }

	public void setWidth(int borderWidth) {
		hBorder_=borderWidth;
	}
}
