import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BTSolver
{

	// =================================================================
	// Properties
	// =================================================================

	private ConstraintNetwork network;
	private SudokuBoard sudokuGrid;
	private Trail trail;

	private boolean hasSolution = false;

	public String varHeuristics;
	public String valHeuristics;
	public String cChecks;

	// =================================================================
	// Constructors
	// =================================================================

	public BTSolver ( SudokuBoard sboard, Trail trail, String val_sh, String var_sh, String cc )
	{
		this.network    = new ConstraintNetwork( sboard );
		this.sudokuGrid = sboard;
		this.trail      = trail;

		varHeuristics = var_sh;
		valHeuristics = val_sh;
		cChecks       = cc;
	}

	// =================================================================
	// Consistency Checks
	// =================================================================

	// Basic consistency check, no propagation done
	private boolean assignmentsCheck ( )
	{
		for ( Constraint c : network.getConstraints() )
			if ( ! c.isConsistent() )
				return false;

		return true;
	}

	/**
	 * Part 1 TODO: Implement the Forward Checking Heuristic
	 *
	 * This function will do both Constraint Propagation and check
	 * the consistency of the network
	 *
	 * (1) If a variable is assigned then eliminate that value from
	 *     the square's neighbors.
	 *
	 * Note: remember to trail.push variables before you change their domain
	 * Return: true is assignment is consistent, false otherwise
	 */
	private boolean forwardChecking ( )
	{
		// Iteration 2
		for (Constraint c : network.getModifiedConstraints()) {
			for (Variable v : c.vars) {
				if (v.isAssigned()) {
					for (Variable neighbor : network.getNeighborsOfVariable(v)) {
						if (neighbor.getDomain().contains(v.getAssignment())) {
							trail.push(neighbor);
							neighbor.removeValueFromDomain(v.getAssignment());
						}
						if (neighbor.size() == 0) {
							return false;
						}
					}
				}
				if (!c.isConsistent()) {
					return false;
				}
			}
		}
		return true;

		// // Iteration 1
		// for (Variable v : network.getVariables()) {
		// 	if (v.isModified()) {
		// 		List<Variable> neighbors = network.getNeighborsOfVariable(v);
		// 		for (Variable neighbor : neighbors) {
		// 			if (neighbor.getValues().contains(v.getAssignment())) {
		// 				trail.push(neighbor);
		// 				neighbor.removeValueFromDomain(v.getAssignment());
		// 			}
		// 			if (neighbor.size() == 0) {
		// 				return false;
		// 			}
		// 		}
		// 		v.setModified(false);
		// 		for (Constraint c : network.getConstraintsContainingVariable(v)) {
		// 			if (!c.isConsistent()) {
		// 				return false;
		// 			}
		// 		}
		// 	}
		// }
		// return true;

		// // Iteration 0
		// for (Constraint c : network.getModifiedConstraints()) {
		// 	for (Variable v : c.vars) {
		// 		if (v.isAssigned()) {
		// 			List<Variable> neighbors = network.getNeighborsOfVariable(v);
		// 			for (Variable neighbor : neighbors) {
		// 				if (neighbor.getValues().contains(v.getAssignment())) {
		// 					trail.push(neighbor);
		// 					neighbor.removeValueFromDomain(v.getAssignment());
		// 				}
		// 				if (neighbor.size() == 0) {
		// 					return false;
		// 				}
		// 			}
		// 		}
		// 	}
		// 	if (!c.isConsistent()) {
		// 		return false;
		// 	}
		// }
		// return true;
	}

	/**
	 * Part 2 TODO: Implement both of Norvig's Heuristics
	 *
	 * This function will do both Constraint Propagation and check
	 * the consistency of the network
	 *
	 * (1) If a variable is assigned then eliminate that value from
	 *     the square's neighbors.
	 *
	 * (2) If a constraint has only one possible place for a value
	 *     then put the value there.
	 *
	 * Note: remember to trail.push variables before you change their domain
	 * Return: true is assignment is consistent, false otherwise
	 */
	private boolean norvigCheck ( )
	{
		return false;
	}

	/**
	 * Optional TODO: Implement your own advanced Constraint Propagation
	 *
	 * Completing the three tourn heuristic will automatically enter
	 * your program into a tournament.
	 */
	private boolean getTournCC ( )
	{
		return false;
	}

	// =================================================================
	// Variable Selectors
	// =================================================================

	// Basic variable selector, returns first unassigned variable
	private Variable getfirstUnassignedVariable()
	{
		for ( Variable v : network.getVariables() )
			if ( ! v.isAssigned() )
				return v;

		// Everything is assigned
		return null;
	}

	/**
	 * Part 1 TODO: Implement the Minimum Remaining Value Heuristic
	 *
	 * Return: The unassigned variable with the smallest domain
	 */
	private Variable getMRV ( )
	{
		Variable minVar = null;
		for (Variable v : network.getVariables()) {
			if (!v.isAssigned()) {
				if (minVar == null || v.size() < minVar.size()) {
					minVar = v;
				}
			}
		}
		return minVar;
	}

	/**
	 * Part 2 TODO: Implement the Degree Heuristic
	 *
	 * Return: The unassigned variable with the most unassigned neighbors
	 */
	private Variable getDegree ( )
	{
		return null;
	}

	/**
	 * Part 2 TODO: Implement the Minimum Remaining Value Heuristic
	 *                with Degree Heuristic as a Tie Breaker
	 *
	 * Return: The unassigned variable with, first, the smallest domain
	 *         and, second, the most unassigned neighbors
	 */
	private Variable MRVwithTieBreaker ( )
	{
		return null;
	}

	/**
	 * Optional TODO: Implement your own advanced Variable Heuristic
	 *
	 * Completing the three tourn heuristic will automatically enter
	 * your program into a tournament.
	 */
	private Variable getTournVar ( )
	{
		return null;
	}

	// =================================================================
	// Value Selectors
	// =================================================================

	// Default Value Ordering
	public List<Integer> getValuesInOrder ( Variable v )
	{
		List<Integer> values = v.getDomain().getValues();

		Comparator<Integer> valueComparator = new Comparator<Integer>(){

			@Override
			public int compare(Integer i1, Integer i2) {
				return i1.compareTo(i2);
			}
		};
		Collections.sort(values, valueComparator);
		return values;
	}

	/**
	 * Part 1 TODO: Implement the Least Constraining Value Heuristic
	 *
	 * The Least constraining value is the one that will knock the least
	 * values out of it's neighbors domain.
	 *
	 * Return: A list of v's domain sorted by the LCV heuristic
	 *         The LCV is first and the MCV is last
	 */
	public List<Integer> getValuesLCVOrder ( Variable v )
	{
		/**
		* Initialize a HashMap with v's domain and iterate through v's
		* neighbors, and increment each variable by 1 every time a
		* neighbor's variable has the same value in its domain.
		*/
		HashMap<Integer, Integer> map = new HashMap<>();
		for (int value : v.getValues()) {
			map.put(value, 0);
		}
		for (Variable neighbor : network.getNeighborsOfVariable(v)) {
			for (int neighborVal : neighbor.getDomain()) {
				if (map.containsKey(neighborVal)) {
					map.put(neighborVal, map.get(neighborVal)+1);
				}
			}
		}

		/**
		* Create an ArrayList out of the map and sort it by the map
		* values (i.e. sort by the counter of how many other neighboring
		* variables share the same values).
		*/
		List<Map.Entry<Integer, Integer>> LCVList = new ArrayList<
		Map.Entry<Integer, Integer>>(map.entrySet());

		Collections.sort(LCVList, new Comparator<Map.Entry<Integer, 
			Integer>>() {
			public int compare(Map.Entry<Integer, Integer> entry1,
				Map.Entry<Integer, Integer> entry2) {
				return entry1.getValue().compareTo(entry2.getValue());
			}
		});

		// Copy the sorted map's keys into an array and return array
		List<Integer> sortedLCVList = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : LCVList) {
			sortedLCVList.add(entry.getKey());
		}
		return sortedLCVList;
	}

	/**
	 * Optional TODO: Implement your own advanced Value Heuristic
	 *
	 * Completing the three tourn heuristic will automatically enter
	 * your program into a tournament.
	 */
	public List<Integer> getTournVal ( Variable v )
	{
		return null;
	}

	//==================================================================
	// Engine Functions
	//==================================================================

	public void solve ( )
	{
		if ( hasSolution )
			return;

		// Variable Selection
		Variable v = selectNextVariable();

		if ( v == null )
		{
			for ( Variable var : network.getVariables() )
			{
				// If all variables haven't been assigned
				if ( ! var.isAssigned() )
				{
					System.out.println( "Error" );
					return;
				}
			}

			// Success
			hasSolution = true;
			return;
		}

		// Attempt to assign a value
		for ( Integer i : getNextValues( v ) )
		{
			// Store place in trail and push variable's state on trail
			trail.placeTrailMarker();
			trail.push( v );

			// Assign the value
			v.assignValue( i );

			// Propagate constraints, check consistency, recurse
			if ( checkConsistency() )
				solve();

			// If this assignment succeeded, return
			if ( hasSolution )
				return;

			// Otherwise backtrack
			trail.undo();
		}
	}

	private boolean checkConsistency ( )
	{
		switch ( cChecks )
		{
			case "forwardChecking":
				return forwardChecking();

			case "norvigCheck":
				return norvigCheck();

			case "tournCC":
				return getTournCC();

			default:
				return assignmentsCheck();
		}
	}

	private Variable selectNextVariable ( )
	{
		switch ( varHeuristics )
		{
			case "MinimumRemainingValue":
				return getMRV();

			case "Degree":
				return getDegree();

			case "MRVwithTieBreaker":
				return MRVwithTieBreaker();

			case "tournVar":
				return getTournVar();

			default:
				return getfirstUnassignedVariable();
		}
	}

	public List<Integer> getNextValues ( Variable v )
	{
		switch ( valHeuristics )
		{
			case "LeastConstrainingValue":
				return getValuesLCVOrder( v );

			case "tournVal":
				return getTournVal( v );

			default:
				return getValuesInOrder( v );
		}
	}

	public boolean hasSolution ( )
	{
		return hasSolution;
	}

	public SudokuBoard getSolution ( )
	{
		return network.toSudokuBoard ( sudokuGrid.getP(), sudokuGrid.getQ() );
	}

	public ConstraintNetwork getNetwork ( )
	{
		return network;
	}
}
