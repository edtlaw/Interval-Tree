package structures;

import java.util.ArrayList;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {
	
	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;
	
	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {
		
		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}
		
		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;
		
		// sort intervals on left and right end points
		//System.out.println();
		sortIntervals(intervalsLeft, 'l');
		//System.out.println();
		sortIntervals(intervalsRight,'r');
		
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = 
							getSortedEndPoints(intervalsLeft, intervalsRight);
		
		// build the tree nodes
		
		root = buildTreeNodes(sortedEndPoints);
		
		
		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
	}
	
	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		// COMPLETE THIS METHOD
		
		int size = intervals.size();
		for(int i = 0; i<size;i++){
			for(int j = i+1; j<size;j++){
				if(lr == 'l'){
					int currLE = intervals.get(i).leftEndPoint;
					int nextLE = intervals.get(j).leftEndPoint;		
					if(nextLE<currLE){
						Interval tmp = intervals.get(i);
						intervals.set(i, intervals.get(j));
						intervals.set(j, tmp);
						
					}
				}
				else{
					int currRE = intervals.get(i).rightEndPoint;
					int nextRE = intervals.get(j).rightEndPoint;		
					if(nextRE<currRE){
						Interval tmp = intervals.get(i);
						intervals.set(i, intervals.get(j));
						intervals.set(j, tmp);
					}
						
					
				}
			}
			
		}
		/*
		for(int x = 0; x <size; x++){
			System.out.println(intervals.get(x));
		}
		*/
		
	}
	
		
	
	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		// COMPLETE THIS METHOD
		
		int size = leftSortedIntervals.size();
		ArrayList <Integer> leftEnds = new ArrayList<Integer>(size);
		ArrayList<Integer> rightEnds = new ArrayList<Integer>(size);
		ArrayList<Integer> allSorted = new ArrayList<Integer>(2*size);
		
		
		for(int i = 0; i < size; i ++){
			int leftEnd = leftSortedIntervals.get(i).leftEndPoint;
			
			if(leftEnds.contains(leftEnd)){
				continue;
			}
			else{
				leftEnds.add(leftEnd);
			}
		}
		for(int i = 0; i<size; i++){
			int rightEnd = rightSortedIntervals.get(i).rightEndPoint;
			if(rightEnds.contains(rightEnd)){
				continue;
			}
			else{
				rightEnds.add(rightEnd);
			}
		}
		allSorted = leftEnds;
		for(int i = 0; i<rightEnds.size();i++){
			if(allSorted.contains(rightEnds.get(i)))
				continue;
			else
				allSorted.add(rightEnds.get(i));
				
		}
		for(int i = 0; i <allSorted.size(); i++){
			for(int j = i+1; j<allSorted.size(); j++){
				if(allSorted.get(i)>allSorted.get(j)){
					int tmp = allSorted.get(i);
					allSorted.set(i, allSorted.get(j));
					allSorted.set(j, tmp);
				}
			}
		}
		System.out.println(allSorted);
		return allSorted;
		
		
	}
	
	/**
	 * Builds the interval tree structure given a sorted array list of end points
	 * without duplicates.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE PROGRAM COMPILE
		
		Queue <IntervalTreeNode> Q = new Queue<IntervalTreeNode>();
		for(int i = 0; i < endPoints.size(); i++){
			float val = endPoints.get(i);
			IntervalTreeNode tmp = new IntervalTreeNode(val,val,val);
			tmp.leftIntervals = new ArrayList<Interval>(0);
			tmp.rightIntervals = new ArrayList<Interval>(0);
			Q.enqueue(tmp);
		}
		/*
		if(Q.size==1){
			IntervalTreeNode T = Q.dequeue();
			return T;
		}
		*/
		while(Q.size!=1){
			int temps = Q.size;
			while(temps>1){
				IntervalTreeNode T1 = Q.dequeue();
				IntervalTreeNode T2 = Q.dequeue();
				float v1 = T1.maxSplitValue;
				float v2 = T2.minSplitValue;
				float splitVal = (v1+v2)/2;
				IntervalTreeNode N = new IntervalTreeNode(splitVal,T1.minSplitValue,T2.maxSplitValue);
				N.leftIntervals = new ArrayList <Interval>(10);
				N.rightIntervals = new ArrayList<Interval>(10);
				N.leftChild = T1;
				N.rightChild = T2;
				System.out.println(N);
				Q.enqueue(N);
				temps = temps -2;
			}
			if(temps == 1){
				IntervalTreeNode tmp = Q.dequeue();
				Q.enqueue(tmp);
			}
		}
		IntervalTreeNode T = Q.dequeue();
		return T;
		
		
		
	}
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		// COMPLETE THIS METHOD
		Queue <IntervalTreeNode> levelOrder = new Queue <IntervalTreeNode>();
		IntervalTreeNode tmp;
		ArrayList<Interval> lSort = leftSortedIntervals;
		ArrayList<Interval> rSort = rightSortedIntervals;
		for(int i = 0; i<leftSortedIntervals.size(); i++){
			levelOrder.enqueue(root);
			do{
				tmp = levelOrder.dequeue();
				
				Interval lSortItem = lSort.get(i);
				Interval rSortItem = rSort.get(i);
				float lSortLeft = (float)lSortItem.leftEndPoint;
				float lSortRight = (float)lSortItem.rightEndPoint;
				float rSortLeft = (float)rSortItem.leftEndPoint;
				float rSortRight = (float)rSortItem.rightEndPoint;
				if (tmp.leftChild == null){
					;
				}
				else{
					levelOrder.enqueue(tmp.leftChild);
				}
				if(tmp.rightChild == null){
					;
				}
				else{
					levelOrder.enqueue(tmp.rightChild);
				}
				
				if(tmp.splitValue>= lSortLeft && tmp.splitValue <= lSortRight){
					tmp.leftIntervals.add(lSortItem);
					
					
				}
				if(tmp.splitValue>= rSortLeft && tmp.splitValue <= rSortRight){
					tmp.rightIntervals.add(rSortItem);
					
				}
				else 
					continue;
				
				
			}while(!levelOrder.isEmpty());
		}
		
			
			
			
		
	}
	
	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE PROGRAM COMPILE
		ArrayList <Interval> resultList = new ArrayList<Interval>(0);
		IntervalTreeNode tmp = root;
		float splitVal = tmp.splitValue;
		ArrayList LList = tmp.leftIntervals;
		ArrayList RList = tmp.rightIntervals;
		IntervalTreeNode Lsub = tmp.leftChild;
		IntervalTreeNode Rsub = tmp.rightChild;
		return null;
	}

}

