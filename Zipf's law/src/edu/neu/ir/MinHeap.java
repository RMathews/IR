package edu.neu.ir;

import java.util.List;
import java.util.ArrayList;

public class MinHeap<E extends Comparable<E>> {
	private int maxSize;
	private List<E> arr;
	private int currentSize;
	
	public MinHeap(int maxSize){
		arr = new ArrayList<E>() ;
		currentSize = 0;
		this.maxSize = maxSize; 
	}
	
	public int size(){
		return currentSize;
	}
	
	public void add(E data) throws Exception{
		if(currentSize >= maxSize){
			throw new Exception("Exceeded max size. Can't insert more");
		}
		else{
			++currentSize;
		}
		arr.add(data);		
		bubbleUp(currentSize - 1);		
	}
	
	public E extractMin() throws Exception{
		E minElem = peekMin();
		if(currentSize == 0){
			throw new Exception("No elements to extract");
		}
		else{
			// Swap the first and last elements. Then delete last element			
			swap(0, currentSize - 1);
			arr.remove(currentSize - 1);
			--currentSize;
			trickleDown(0);
		}
		return minElem;
	}
	
	public E peekMin() {
		return arr.get(0);
	}
	
	private int getParentIdx(int idx){
		return (idx - 1)/2;
	}
	
	private int getLeftChildIdx(int idx){
		return 2*idx + 1;
	}
	
	private int getRightChildIdx(int idx){
		return 2 * idx + 2;
	}
	
	private void swap(int idx1, int idx2){
		E temp = arr.get(idx1);
		arr.set(idx1, arr.get(idx2));
		arr.set(idx2, temp);
	}
	
	private void bubbleUp(int idx){
		// Till we reach root or parent element is greater than current 
		while(idx != 0 && arr.get(getParentIdx(idx)).compareTo(arr.get(idx)) > 0){
			swap(getParentIdx(idx), idx);
			idx = getParentIdx(idx);
		}
	}
	
	private void trickleDown(int idx){
		
		// While current element is greater than the lesser child keep moving it down
		while(true){
			int leftIdx = getLeftChildIdx(idx);
			int rightIdx = getRightChildIdx(idx);
			int lesserIdx;
			if(leftIdx > currentSize - 1){
				break;
			}
			else if(rightIdx > currentSize - 1){
				lesserIdx = leftIdx;
			}
			else{
				lesserIdx = arr.get(leftIdx).compareTo(arr.get(rightIdx)) < 0 ? leftIdx : rightIdx;
			}
			if(arr.get(idx).compareTo(arr.get(lesserIdx)) > 0){
				swap(idx, lesserIdx);
				idx = lesserIdx;
			}
			else{
				break;
			}		
		}		
	}
	
	@Override
	public String toString(){
		return arr.toString();
	}
}
