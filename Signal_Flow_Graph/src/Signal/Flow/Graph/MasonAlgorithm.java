package Signal.Flow.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MasonAlgorithm {
	
	private static MasonAlgorithm Mason = new MasonAlgorithm(); // singelton design pattern to generate one instance of object
	
	private double[][] adjacencyMatrix; // n*n as n is the number of nodes each vertex is the gain edge between the two nodes
	private int numOfNodes; // number of nodes in the graph

	private ArrayList<Integer[]> nonTouchingLoops; // each index is a sequence of nodes that nonTouching 
	private ArrayList<Double> nonTouchingLoopGains; // stores the gain of the correspond nonTouching loops

	private ArrayList<ArrayList<Integer>> forwardPaths; // store the forward paths as sequence of nodes from source node to sink node
	private ArrayList<boolean[]> forwardPathsMask; // check for visiting the node before or not to make the path forward
	private ArrayList<Double> forwardPathGains; // store the gain of the correspond forward path

	private ArrayList<ArrayList<Integer>> loops; // store loops as a sequence of nodes like 2 4 2
	private ArrayList<boolean[]> loopsMask; // check if loops stored before or not
	private ArrayList<Double> loopGains; // store  the gain of the correspond loop
	private double overAllTF; // store the overall TF value
	private int width, height; // the height and weight of the screen that represent the GUI
	
	private MasonAlgorithm() {} // private constructor because of using the singleton
	
	public static MasonAlgorithm getInstance() {  // on instance for the class MasonAlgorithm
		return Mason;
	}
	
	public void SFG() { // calls when user press the solve button after entering all edges between nodes 
		numOfNodes = adjacencyMatrix.length;
		calcMasonAlgorithm();
	}
	
	private void calcMasonAlgorithm() {
		// TODO Auto-generated method stub
		forwardPaths = new ArrayList<ArrayList<Integer>>();
		loops = new ArrayList<ArrayList<Integer>>();
		forwardPathsMask = new ArrayList<boolean[]>();
		loopsMask = new ArrayList<boolean[]>();
		forwardPathGains = new ArrayList<Double>();
		loopGains = new ArrayList<Double>();
		nonTouchingLoopGains = new ArrayList<Double>();
		nonTouchingLoops = new ArrayList<Integer[]>();

		generateFBAndLoops(); // generate forward paths and loops through the depth first search algorithm and back tracking through recursion
		ArrayList<ArrayList<Integer>> loopLabels = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < loops.size(); i++) {
			loopLabels.add(new ArrayList<Integer>());
			loopLabels.get(loopLabels.size() - 1).add(i);
		}
		generateNonTouching(loopLabels, 1); // generate all possible combination of the non-touching loops
	}
	
	private boolean[] mapNodes(ArrayList<Integer> arr) {
		boolean[] temp = new boolean[numOfNodes];
		for (int i = 0; i < arr.size(); i++) {
			temp[arr.get(i)] = true;
		}
		return temp;
	}
	
	// calculate the gain of the input sequence of nodes
	private double calcGain(ArrayList<Integer> arr) {
		double temp = 1;
		if (arr.size() > 1) {
			for (int i = 0; i < arr.size() - 1; i++)
				temp *= adjacencyMatrix[arr.get(i)][arr.get(i + 1)];
			return temp;
		}
		return adjacencyMatrix[arr.get(0)][arr.get(0)];

	}
	// add a new loop to the loops
	private void addToLoops(ArrayList<Integer> arr) {
		arr.add(arr.get(0));
		if (!isLoopFound(arr)) { // check first if this loop is stored before
			loops.add(arr);
			loopsMask.add(mapNodes(arr));
			loopGains.add(calcGain(arr));
		}
	}

	private boolean isLoopFound(ArrayList<Integer> arr) {
		boolean[] loop = mapNodes(arr);
		for (int i = 0; i < loops.size(); i++) {
			if (loops.get(i).size() == arr.size() && isEquivalentLoop(loop, loopsMask.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isEquivalentLoop(boolean[] arr1, boolean[] arr2) {
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i])
				return false;
		}
		return true;
	}
	// add a new forward path to forward Paths
	private void addToFP(ArrayList<Integer> arr) {

		forwardPaths.add(arr);
		forwardPathsMask.add(mapNodes(arr));
		forwardPathGains.add(calcGain(arr));
	}

	private void generateFBAndLoops() {
		generateForwardPathsAndLoops(new ArrayList<Integer>(), new boolean[numOfNodes], 0);
	}

	private void generateForwardPathsAndLoops(ArrayList<Integer> path, boolean[] visited, int node) {
		path.add(node);
		visited[node] = true;
		// forward path case
		if (path.size() > 1 && node == numOfNodes - 1) {
			addToFP(new ArrayList<>(path));
			return;
		}
		for (int neighbour = 0; neighbour < numOfNodes; neighbour++) {
			if (adjacencyMatrix[node][neighbour] != 0) {
				if (!visited[neighbour]) {
					generateForwardPathsAndLoops(path, visited, neighbour);
					path.remove(path.size() - 1); // here back tracking for new forward path
					visited[neighbour] = false;
					// loop case
				} else {
					int index = path.indexOf(neighbour);
					if (index != -1) {
						List<Integer> temp = path.subList(index, path.size());
						addToLoops(new ArrayList<Integer>(temp));
					}
				}
			}
		}
	}
	// generate all possible combination of non touching loops
	private void generateNonTouching(ArrayList<ArrayList<Integer>> arrList, int n) {
		Set<List<Integer>> hasGenerated = new HashSet<List<Integer>>();
		boolean flag = false;
		ArrayList<ArrayList<Integer>> nextArrList = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < arrList.size(); i++) {
			for (int j = i + 1; j < arrList.size(); j++) {
				for (int k = 0; k < arrList.get(j).size(); k++) {
					int x = arrList.get(j).get(k);
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.addAll(arrList.get(i));
					temp.add(x); 
					if (isNonTouching(temp)) {
						Collections.sort(temp);
						if (!hasGenerated.contains(temp)) {
							hasGenerated.add(temp);
							flag = true;
							nextArrList.add(new ArrayList<Integer>());
							nextArrList.get(nextArrList.size() - 1)
									.addAll(temp);
							nonTouchingLoops.add(temp.toArray(new Integer[temp
									.size()]));
							nonTouchingLoopGains.add(getNonTouchingGain(temp));
						}
					}
				}
			}

		}
		if (flag) {
			generateNonTouching(nextArrList, ++n);
		}
	}
	
	private boolean isNonTouching(ArrayList<Integer> arr) {
		int flag;
		// looping over columns
		for (int i = 0; i < numOfNodes; i++) {
			flag = 0;
			// looping over rows
			for (int j = 0; j < arr.size(); j++) {
				if (loopsMask.get(arr.get(j))[i])
					flag++;
			}
			if (flag > 1)
				return false;
		}
		return true;
	}
	
	
	private double getNonTouchingGain(ArrayList<Integer> arr) {
		double gain = 1;
		for (int j = 0; j < arr.size(); j++)
			gain *= loopGains.get(arr.get(j));
		return gain;
	}

	// get the Non touching loops with forward paths
	private LinkedList<Integer> getValidLoopsWithPath(ArrayList<Integer> path, ArrayList<ArrayList<Integer>> loops) {
		
		LinkedList<Integer> ans = new LinkedList<Integer>();
		
		for(int i=0;i<loops.size();i++) {
			int flag = 0;
			for(int j = 0;j < path.size();j++) {
				if(loops.get(i).contains(path.get(j))) {
					flag++;
					break;
				}
			}
			if(flag == 0 ) {
				System.out.println("---------------------");
				System.out.println("delta indcies - " + i);
				System.out.println("----------------------");
				ans.add(i);
			}
		}
		
		return ans;
		
	}
	
	public void setOverAllTF(double overAllTF) {
		this.overAllTF = overAllTF;
	}
	// calculate the over all gain using Mason's rules 
	public double getOverAllTF() {
		double delta = 1;
		int e = 1;
		int nth = 2;
	
		double sum = 0;
		for(int i=0;i<loops.size();i++) {
			sum += loopGains.get(i);
		}
		delta = delta - sum;
		for (int i = 0; i < nonTouchingLoops.size(); i++) {
			if (nonTouchingLoops.get(i).length == nth) {
				delta += e * nonTouchingLoopGains.get(i);
			} else {
				e *= -1;
				++nth;
				i--;
			}
		}
		double pathDeltaSum = 0;
		double deltaN;	
		for(int i = 0;i < forwardPaths.size();i++) {
			LinkedList<Integer> indcies = getValidLoopsWithPath(forwardPaths.get(i), loops);
			deltaN = 1;
			double loopSumGains = 0;
			for(int  j = 0;j < indcies.size();j++) {
				loopSumGains += loopGains.get(indcies.get(j));
			}
			deltaN = deltaN - loopSumGains;
			pathDeltaSum = pathDeltaSum + (forwardPathGains.get(i) * deltaN);
		}	
		return pathDeltaSum / delta;
	}
	
	//  getters and setters 
	
	public void setLoops(ArrayList<ArrayList<Integer>> newLoops) {
		this.loops= newLoops;
	}
	
	public String[] getLoops() {
		String loopsString[] = new String[loops.size()];
		int itr = 0;
		for (ArrayList<Integer> arr : loops) {
			loopsString[itr] = "";
			for (int i = 0; i < arr.size(); i++) {
				loopsString[itr] += (arr.get(i) + 1) + " ";
			}
			itr++;
		}
		return loopsString;
	}
	
	public void setNonTouchingloops(ArrayList<Integer[]> newNonTouchingloops) {
		this.nonTouchingLoops= newNonTouchingloops;
	}
	
	public String[] getNonTouchingloops() {
		String[] temp = getLoops();
		String nonString[] = new String[nonTouchingLoops.size()];
		int itr = 0;
		for (Integer[] arr : nonTouchingLoops) {
			nonString[itr] = "";

			if (arr.length > 0)
				nonString[itr] += temp[arr[0]];

			for (int i = 1; i < arr.length; i++)
				nonString[itr] += " , " + temp[arr[i]];

			itr++;
		}
		return nonString;
	}
	
	public void setForwardPaths(ArrayList<ArrayList<Integer>> newForwardPaths) {
		this.forwardPaths= newForwardPaths;
	}
	
	public String[] getForwardPaths() {
		String fbString[] = new String[forwardPaths.size()];
		int itr = 0;
		for (ArrayList<Integer> arr : forwardPaths) {
			fbString[itr] = "";
			for (int i = 0; i < arr.size(); i++) {
				fbString[itr] += (arr.get(i) + 1) + " ";
			}
			itr++;
		}
		return fbString;
	}
	
	public void setLoopsGain(ArrayList<Double> inputLoopsGain) {
		this.loopGains = inputLoopsGain;
	}
	public Double[] getLoopsGain() {
		return loopGains.toArray(new Double[loopGains.size()]);
	}
	
	public void setNonTouchingloopsGain(ArrayList<Double> inputNonTouchingloopsGain) {
		this.nonTouchingLoopGains = inputNonTouchingloopsGain;
	}
	public Double[] getNonTouchingloopsGain() {
		return nonTouchingLoopGains.toArray(new Double[nonTouchingLoopGains.size()]);
	}
	
	public void setForwardPathsGain(ArrayList<Double> inputForwardPathsGain) {
		this.forwardPathGains= inputForwardPathsGain;
	}
	public Double[] getForwardPathsGain() {
		return forwardPathGains.toArray(new Double[forwardPathGains.size()]);
	}
	
	public void setAdjacencyMatrix(double[][] inputAdjacencyMatrix) {
		this.adjacencyMatrix= inputAdjacencyMatrix;
	}
	public double[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}
	
	public void setNumOfNodes(int nodesNumber) {
		this.numOfNodes = nodesNumber;
	}
	public int getNumOfNodes() {
		return numOfNodes;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return height;
	}
	
}
