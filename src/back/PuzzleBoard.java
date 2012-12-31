package back;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleBoard {
	private final List<Integer> nums;
	
	public PuzzleBoard(final int level){
		nums = new ArrayList<Integer>();
		for(int i=0;i<level;i++){
			nums.add(i+1);
		}
		randomize(nums);
	}

	public int[] getNums(){
		final int s = nums.size();
		final int[] ret = new int[s];
		for(int i=0;i<s;i++){
			ret[i]=nums.get(i);
		}
		return ret;
	}

	public void swapFirst(final int index){
		int x = index-1;
		for(int i=0;i<x-i;i++){
			swapPlaces(nums,i,x-i);
		}
	}

	public boolean isDescending(){
		final List<Integer> win = new ArrayList<Integer>();
		for(int i=nums.size();i>0;i--){
			win.add(i);
		}
		return nums.equals(win);
	}

	private void randomize(final List<Integer> list){
		final Random rng = new Random();
		for(int i=0; i<list.size(); i++){
			swapPlaces(list,i,rng.nextInt(list.size()));
		}
	}

	@Override
	public String toString(){
		final StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(int x:nums){
			sb.append(x+" ");
		}
		sb.append("]");
		return sb.toString();
	}

	private static void swapPlaces(final List<Integer> list, final int indexA, final int indexB){
		final int temp = list.get(indexA);
		list.set(indexA, list.get(indexB));
		list.set(indexB, temp);
	}
}
