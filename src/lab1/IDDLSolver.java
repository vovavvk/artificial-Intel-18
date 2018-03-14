package lab1;
//TODO implement Iterative Deepening with DLS

	/*Pseudo Code:
		
		// Returns true if target is reachable from
		// src within max_depth
		bool IDDFS(src, target, max_depth)
		    for limit from 0 to max_depth
		       if DLS(src, target, limit) == true
		           return true
		    return false   

		bool DLS(src, target, limit)
		    if (src == target)
		        return true;

		    // If reached the maximum depth, 
		    // stop recursing.
		    if (limit <= 0)
		        return false;   

		    foreach adjacent i of src
		        if DLS(i, target, limit?1)             
		            return true

		    return false
*/
public class IDDLSolver {

}
