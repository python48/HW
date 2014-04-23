package tarea6;

import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class Automaton {

	/*Variable declarations*/
	private ArrayList<State> states = new ArrayList<State>();
	private String[] alphabet;
	private ArrayList<Transition> transitions = new ArrayList<Transition>();
	private State initialState;
	private ArrayList<State> finalStates;
	/*End of variable declarations*/
	
	/*Constructor of class*/
	public Automaton(ArrayList<State>states, String[] alphabet,ArrayList<Transition>transitions,State initialState,ArrayList<State> finalStates){
		this.states=states;
		this.alphabet=alphabet;
		this.transitions=transitions;
		this.initialState=initialState;
		this.finalStates=finalStates;
	}
	
	public Automaton(){}
	
	public void print(Automaton automata){
		ArrayList<Transition>temp=automata.getTransitions();
		for (Object object : temp) {
			Transition trans = (Transition)object;
			String s = "\u03B4("+trans.getOrigin().getTag()+","+trans.getInput()+")="+trans.getDestination().getTag();
			System.out.println(s);			
		}

	}
	
	public ArrayList<State> findUnreachable(Automaton automata){
		//list of unreachable states
		ArrayList<State>unr = new ArrayList<State>();
		//the same set X. The set of states that lead to A on input c
		ArrayList<State>X = new ArrayList<State>();

		//get the set of states that lead to A on input c 
		for (Object ob : automata.getTransitions()) {
			Transition transition = (Transition)ob;
			State x = transition.getTransition();
			if (!X.contains(x)) {
				X.add(x);				
			}
		}
		
		for (Object ob : automata.getStates()) {
			State st=(State)ob;
			if(!X.contains(st))
				unr.add(st);
		}
		//del
		//states to remove
		ArrayList<State>Y=new ArrayList<State>();
		for (State unrst : unr) 
			if (!(unrst.getType()==0))
				Y.add(unrst);
				//Y.add(unrst);
		
		for (State y : Y) 
				unr.remove(y);
		
		
		return unr;
	}
	
	
	/*
	 * 
	 * DFA minimization
	 * 
	 * */
	public Automaton minimize(Automaton automata)throws Exception{
		ArrayList<State> unr = findUnreachable(automata);
		destroyUnreachable(unr);
		ArrayList<State>nonFinalStateSet = getNonFinalStates(automata);
		ArrayList<State>finalStateSet = getFinalStates(automata);

		ArrayList<Object>W=new ArrayList<Object>();
		W.add(nonFinalStateSet);
		W.add(finalStateSet);
		
		//let Z be the new sets after hopcroft's partitioning
		ArrayList<Object>Z = new ArrayList<Object>();
		Z = hopcroftAlgorithm(W,automata);
		
		automata.setStates(simpStates(Z, automata));
		automata.refreshTransitions();
		
		automata.refreshFinalStates();
		automata.refreshInitialStates();
		
		return automata;
	}

	private void refreshInitialStates() {
		
	}

	private void refreshFinalStates() {
		// TODO Auto-generated method stub
	}

	private String  foo(ArrayList<State>X){
		String s = "{";
		int i = 1;
		for (Iterator<State> iterator = X.iterator(); iterator.hasNext();) {
			State currState = (State) iterator.next();
			if (i++<X.size())
				s+=currState.getTag()+",";
			else
				s+=currState.getTag();
		}
		s+="}";
		return s;
		}
	/*
	 * Z = {{1,3},{2},{4}}
	 * */
	private ArrayList<State> simpStates(ArrayList<Object>Z,Automaton automata)
	{
		ArrayList<State>newStates=new ArrayList<State>();		
		ArrayList<State>states = automata.getStates();
		ArrayList<Object>checkedList = new ArrayList<Object>();
		
		for (Iterator<Object> iterator = Z.iterator(); iterator.hasNext();) 
		{
			Object object = (Object) iterator.next();
			@SuppressWarnings("unchecked")
			ArrayList<State>X = (ArrayList<State>) object;
			for (Iterator<State> iterator2 = states.iterator(); iterator2.hasNext();) 
			{
				State currentState = (State) iterator2.next();
				if (!checkedList.contains(currentState)) 
				{
					if(X.contains(currentState))
					{
						for (State state : X) 
							checkedList.add(state);
						currentState.setTag(foo(X));
						newStates.add(currentState);
					}
					else
					{
						checkedList.add(currentState);
						newStates.add(currentState);		
					}					
				}
			}
		}
//		newStates=purge(newStates);
			return newStates;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Object> purge(ArrayList<Object> newStates) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HashSet h = new HashSet(newStates);
		newStates.clear();
		newStates.addAll(h);
		
		return newStates;
	}

	public ArrayList<Object> hopcroftAlgorithm(ArrayList<Object>W,Automaton automata)
	{
		/*
		 * W={non-final,final}
		 * let A be each subset of W
		 * let X be a subset of states that lead to A on input symbol c
		 * let Y be a subset of states that do not lead to A on input symbol c
		 * Z = List of resultant partitions
		 * */
		String[] sigma = automata.getAlphabet();
		ArrayList<Object>Z=new ArrayList<Object>();
		while(!W.isEmpty())
		{
			Object ob=W.get(0);
			@SuppressWarnings("unchecked")
			ArrayList<State> A = (ArrayList<State>) ob;
			W.remove(A);
			//X the set of states that lead to A on c
			ArrayList<State>X=new ArrayList<State>();
			//the new set or the set that holds the states not accepted by A
			ArrayList<State>Y=new ArrayList<State>();
			Stack<State> stack = new Stack<State>();
			ArrayList<State>temp=new ArrayList<State>();
			for (String c : sigma) {
				if(A.size()<2)break;
				for (Iterator<State> iterator = A.iterator(); iterator.hasNext();) {
					State state = (State) iterator.next();
					State stateX = state.transition(state, c);
					X.add(stateX);
					stack.push(stateX);
				}
				for (Iterator<State> iterator = X.iterator(); iterator.hasNext();) {
					State stateX = (State) iterator.next();
					if(!A.contains(stateX)){
						int index=stack.size()-1;
						while(!stack.isEmpty()){
							if(stateX.equals(stack.pop())){
								Y.add(A.get(index));//
								temp.add(A.get(index));//
								index--;
							}else
								index--;
						}
					}
				}
				X.clear();
				stack.clear();
				//Y = elements not accepted by A on input c
				if (!Y.isEmpty())
				{
					for (Iterator iterator = Y.iterator(); iterator.hasNext();) 
					{
						State st = (State) iterator.next();
						W.add(st);
					}
				}
				
				
			}
			//subtract elements Y from A
			for (Iterator<State> iterator = temp.iterator(); iterator.hasNext();) {
				State state = (State) iterator.next();
				A.remove(state);
			}
			//add the new partition to a list Z
			Z.add(A);
		}
		return Z;
	}
	
	
	
	public ArrayList<State> getNonFinalStates(Automaton automata){
		ArrayList<State> newSet = new ArrayList<State>();
		for (Iterator<State> iterator = automata.getStates().iterator(); iterator.hasNext();) {
			State state = (State) iterator.next();
			if(state.getType()!=2)
			newSet.add(state);
		}
		return newSet;
	}
	
	private void destroyUnreachable(ArrayList<State>unr){
		for (State state : unr) {
			destroyState(state);
		}
		//list of transitions of unreachable states
		ArrayList<Object> list = new ArrayList<Object>();
		
		for (Object ob : this.getTransitions()) {
			Transition tran = (Transition)ob;
			if (unr.contains(tran.getOrigin())) {
				list.add(tran);
			}
		}
		for (Object object : list) {
			Transition tran = (Transition)object;
			destroyTransition(tran);
		}
	}
	
	private void destroyTransition(Transition transition){
		String s = "\u03B4("+transition.getOrigin().getTag()+","+transition.getInput()+")="+transition.getDestination().getTag();
		if (this.transitions.remove(transition)) {
			System.out.println(s+" removed");
		}
}
	
	private void destroyState(State stateToDestroy){
			String s = stateToDestroy.getTag();
			if (this.states.remove(stateToDestroy)) {
				System.out.println(s+" removed");
			}
		
	}
	
	//internal use
	protected char[] generateAlphabeth(String alphabeth){
		char[]temp = alphabeth.toCharArray();
		return temp;
	}
	
	/*Setters*/
	public void setStates(ArrayList<State>newStates){
		this.states=newStates;
	}
	/*Getters*/
	public ArrayList<State> getStates() {
		return this.states;
	}
	public String[] getAlphabet() {
		return this.alphabet;
	}
	public ArrayList<Transition> getTransitions() {
		return this.transitions;
	}
	public void refreshTransitions() {
		ArrayList<Transition>newTransitions=new ArrayList<Transition>();
		for (Object ob : this.getStates()) {
			State state = (State)ob;
			newTransitions.addAll(state.getTransitions());
		}
		this.transitions=newTransitions;
	}
	public State getInitialState() {
		return this.initialState;
	}
	/*returns final states from this automata*/
	public ArrayList<State> getFinalStates() {
		return this.finalStates;
	}
	/*returns final states from the automata that receives*/
	public ArrayList<State> getFinalStates(Automaton automata){
		ArrayList<State> newSet = new ArrayList<State>();
		for (Iterator<State> iterator = automata.getStates().iterator(); iterator.hasNext();) {
			State state = (State) iterator.next();
			if(state.getType()==2)
				newSet.add(state);
		}
		return newSet;
	}
	
}