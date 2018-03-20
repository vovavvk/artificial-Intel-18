package lab1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* 	(1) Put the start node s on a list, called OPEN, of unexpanded nodes. 
 * 			Calculate f(s) and associate its value with node s.
   	(2) If OPEN is empty, exit with failure, no solution exists.
	(3) Select from OPEN a node i at which f is minimum. If several nodes qualify, 
				choose a goal node if there is one, otherwise choose among them arbitrarily.
	(4) Remove node i from OPEN and place it on a list called CLOSED, of expanded nodes.
	(5) If i is a goal node, exit with success; a solution has been found.
	(6) Expand node i, creating nodes for all of its successors. For every successor node j of i:
	
		a. Calculate f(j)- DONE!!!!
		
		b. If j is neither in OPEN nor in CLOSED, then add it to OPEN with its f value. 
				Attach a pointer from j back to its predecessor i 
					(in order to track back a solution path once a goal node is found). DONE
					
		c. If j was already on either OPEN or CLOSED, compare the f value just calculated for j with 
						the value previously associated with the node. If the new value is lower:
			i.   Substitute it for the old value.
			ii.  Point j back to i instead of to its previously found predecessor.
			iii. If node j was on the CLOSED list, move it back to OPEN
			
	(7) Go to (2)
*/

//TODO (2) - implement 2 heuristics functions within this context (At least 2)

public class A_StarSolver {	
	static FibonacciHeap<State> OPEN_LIST = new FibonacciHeap<>();
	static Map<String,FibonacciHeapNode<State> > OPEN_LIST_HELPER = new HashMap<>();
	static Map<String,FibonacciHeapNode<State>  > CLOSED_LIST = new HashMap<>();
	//private static int id = 1;
	State root;
	
	public A_StarSolver(State initialState){
		this.root = initialState;
	}
	
	
	
	/**
	 * A* algorithm implementation
	 * @return boolean
	 */
	public boolean solve(){
		root.setParent(null);
		calculate_Heuristics(root);
		FibonacciHeapNode<State> rootHeapNode = new FibonacciHeapNode<State>(root, root.getHValue());
		OPEN_LIST.insert(rootHeapNode, root.getHValue());
		OPEN_LIST_HELPER.put(root.disc, rootHeapNode);
		
		
		while(!OPEN_LIST.isEmpty()){
			/*System.out.println("###### " + i++);*/
			FibonacciHeapNode<State> min = OPEN_LIST.min();
			//min.data.show();System.out.println();
			//remove min from OPEN_LIST
			min = remove_min();
			
			
			CLOSED_LIST.put(min.data.disc, min);
			if(min.data.isGoal()){
				/*System.out.println(min.data.cars.get('X'+"").status());*/
				System.out.println("Solution FOUND!");
				State path = min.data;
				/**
				 * TODO - critical bug!
				 */
				while(path.getParent() != null){
					System.out.println(path.getOp());
					//path.show();
					path = path.getParent();
				}
				return true;
			}
			ArrayList<Op> operations = min.data.generatePossibleMoves().getPossibleOperations();
			for(Op op: operations){
				
				State s = new State(min.data);
				s.setOp(op.constructOperation());
				s.compress();
				//System.out.println("##DEBUG: Operation: " +s.disc + "  " +  op.constructOperation());
				s.makeMove(op);
				
				s.draw();
				s.compress();
				s.setParent(min.data);
				calculate_Heuristics(s);
				
				//Checks if the state was opened/closed previously
				
				boolean opened = OPEN_LIST_HELPER.containsKey(s.disc)
						,closed = CLOSED_LIST.containsKey(s.disc);
				FibonacciHeapNode<State> stateHelper = null;
				if(closed || opened){
					stateHelper = (closed)? CLOSED_LIST.get(s.disc): OPEN_LIST_HELPER.get(s.disc);
					if(s.getHValue() < stateHelper.key){
						//stateHelper.setHValue(s.getHValue());
						if(opened){
							/**
							 * TODO - validate that updating in one end causes updates on the other end
							 */
							OPEN_LIST.decreaseKey(stateHelper, s.getHValue());
							stateHelper.data.setHValue(s.getHValue());
						}else{
							//fbn equals to stateHelper
							FibonacciHeapNode<State> fbn = CLOSED_LIST.remove(s.disc);
							fbn.data.setHValue(s.getHValue());
							insert_opened(fbn);
							
						}
						stateHelper.data.setParent(min.data);
					}
					System.out.println("YEPPE");
					continue;
				}
				
				FibonacciHeapNode<State> new_fbn = new FibonacciHeapNode<State>(s, s.getHValue());
				insert_opened(new_fbn);
				/*OPEN_LIST.insert(new_fbn, s.getHValue());
				OPEN_LIST_HELPER.put(s.disc, new_fbn);*/
			}
		}
		
		return false;
	}
	
	private void insert_opened(FibonacciHeapNode<State> fbn) {
		OPEN_LIST.insert(fbn, fbn.data.getHValue());
		OPEN_LIST_HELPER.put(fbn.data.disc, fbn);
		
	}



	/**
	 * 
	 * 
	TODO - implement F(n) = g(n) + h(n). 
	#NOTE# h(n) needs to be ADMISSIBLE!
	
	this is a sample calculation ONLY!!
	*
	*
	**/
	private void calculate_Heuristics(State node) {
		if(node.isGoal()){
			node.setHValue(-100.0);
		}else{
			node.setHValue(0);
		}
	}
	
	/**
	 * TODO - needs to be configured to choose goal state if it exists AND handling collisions
	 * @return
	 */
	public State getMin(){
		return OPEN_LIST.min().data;
	}
	public FibonacciHeapNode<State> remove_min(){
		FibonacciHeapNode<State> min = OPEN_LIST.removeMin();
		OPEN_LIST_HELPER.remove(min.data.disc);
		return min;
	}
	
	
	
	public static void main(String[] args){
		State s = new State();
		s.initilizeState("AA...........XX.....................");
		s.setParent(null);
		s.show();
		A_StarSolver solver = new A_StarSolver(s);
		System.out.println(solver.solve());
	}
	

}

