package control;

import gui.MKButton;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import back.Game;

public class PuzzleScrollableView implements ScrollableView{
	private static final int HEADER_OFFSET = 18;
	private static final int BUTTON_OFFSET = HEADER_OFFSET + 20;
	private static final int BUTTON_HEIGHT = 65;
	private static final int BUTTON_WIDTH = 15;
	private static final Font BUTTON_FONT = new Font("Lucida Grande",Font.PLAIN,42);
	private static final Font HEADER_FONT = new Font("Lucida Grande",Font.BOLD,18);
	private final PuzzleScrollableView THIS = this;
	private final ActionListener BUT_LISTEN = new ActionListener() {
		@Override
		public void actionPerformed(final ActionEvent event) {
			final int index = Integer.parseInt(event.getActionCommand());
			THIS.showSwap(index);
		}
	};

	private final Game theGame;
	private final ArrayList<MKButton> buts;

	public PuzzleScrollableView(){
		theGame = new Game();
		buts = new ArrayList<MKButton>();
		buts.add(getBut("1"));
	}

	private MKButton getBut(final String actionCommand){
		final MKButton but = new MKButton("", BUTTON_FONT, actionCommand);
		but.addActionListener(BUT_LISTEN);
		but.setWidth(BUTTON_WIDTH);
		return but;
	}

	@Override
	public int[] getDim() {
		final int[] dim = {400,(theGame.isLoss()?BUTTON_OFFSET:BUTTON_OFFSET+BUTTON_HEIGHT*theGame.getLevel())};
		return dim;
	}

	private String getHeader(){
		return "Level: "+theGame.getLevel()+". You have "+theGame.getTurns()+" swaps remaining.";
	}

	private void manageButtons(){
		final int[] nums = theGame.getNums();
		if(nums.length>buts.size()){buts.add(getBut(""+theGame.getLevel()));}
		for(int i=0;i<nums.length;i++){
			buts.get(i).setText(""+nums[i]);
			buts.get(i).setLocation(180, BUTTON_OFFSET+i*BUTTON_HEIGHT);
		}
	}

	private boolean isSwapping = false;
	private int toSwap = 0;
	private int swapRenderCount = 0;
	
	private void showSwap(int index){
		isSwapping = true;
		toSwap = index;
		swapRenderCount = 0;
	}
	
	private void moveButtons(final Graphics g){
		if(swapRenderCount>=BUTTON_HEIGHT){
			isSwapping=false;
			theGame.swapFirst(toSwap);
		}
		swapRenderCount+=2;
		for(int i=0;i<buts.size();i++){
			if( i<toSwap ){
				MKButton b = new MKButton(buts.get(i));
				final Point old = b.getLocation();
				float distFromCenter = ( ((float)toSwap-1)/2.0f ) - ((float)i);
				//outAndBack is (halfway minus (distance from half)).
				//This ensures that the button starts and ends with the same X value.
				float outAndBack = BUTTON_HEIGHT/2-Math.abs(BUTTON_HEIGHT/2-((float)(swapRenderCount)));
				final Point change = new Point( (int)(1.3f* outAndBack *distFromCenter), 
						(int)(2.0f* ((float)swapRenderCount) *distFromCenter));
				b.setLocation(old.x+change.x,old.y+change.y);
				b.show(g);
			}else{
				buts.get(i).show(g);
			}
		}
	}
	
	@Override
	public Image drawImage(final Image image) {
		final Graphics g = image.getGraphics();
		g.setFont(HEADER_FONT);
		if(!theGame.isLoss()){
			g.drawString(getHeader(), 40, HEADER_OFFSET);
			manageButtons();
			if(isSwapping){
				moveButtons(g);
			}else{
				for(MKButton b:buts){
					b.show(g);
				}
			}
			return image;
		}else{
			g.drawString("Game over! You made it to level "+theGame.getLevel()+".", 20, HEADER_OFFSET);
		}
		return image;
	}

	@Override
	public void handleClick(final Point coords, final int type) {
		if(!isSwapping){
			for(MKButton but:buts){
				if(but.inBounds(coords)){but.doClick();return;}
			}
		}
	}

}
