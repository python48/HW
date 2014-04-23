package tarea6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
	
	protected static Automaton automata = new Automaton();
	public static void main(String[] args) throws IOException{
		System.out.println("Welcome to tarea 6: DFA Minimization algorithm");
		int option=4;
		
		while(true){
			option=prompt();
				switch (option) {
				case 1:
					option1();
					break;
				case 2:
					option2();
					break;
				case 3:
					option3();
					break;
				case 4:
					option4();
					break;
		
				default:
					break;
				}
		}
		
	}
	public static void option1() {
		System.out.println("1.Create new automaton:");
		//create an automaton first:
		String[]alphabet=promptAlphabet().split(",");
		//String[] alphabet = temp.toString().toCharArray();//string separated by commas
		//STATES
		ArrayList<State> states = genStates();
		//ArrayList<State> states = genStates();
		
		//TRANSITIONS
		ArrayList<Transition> transitions=genTransitions(states,alphabet);
		
		states = addTransitionsToStates(transitions,states);
		//setting initial state
		State a = new State();
		for (State s : states) {
			if (s.getType()==0) {
				a=s;
			}			
		}
		
		ArrayList<State>finalStates = new ArrayList<State>();
		for (State s : states) {
			if (s.isFinal()) {
				finalStates.add(s);
			}
		}
		
		
		//ArrayList<Object>states, String alphabet,ArrayList<Object>transitions,State initialState,State finalState
		automata = new Automaton(states,alphabet,transitions,a,finalStates);
		pause();
	}
	private static void option2() {
		System.out.println("2.Minimization of automaton");
		try {
			automata=automata.minimize(automata);		
			System.out.println("your automaton is now minimized");
		} catch (Exception e) {
			System.err.println("My Bad\n");e.printStackTrace();
			System.out.println("Your automaton was not minimized");
		}
		pause();
	}
	private static void option3() {
		System.out.println("3.Print automaton:");
		automata.print(automata);
		pause();
	}
	private static void option4() {
		System.out.println("program will now exit...");
		System.exit(0);
	}
	private static void pause() {
		System.out.println("press any key to continue...");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
		    reader.readLine();
		} catch (IOException e) {
			System.err.println("IO ERR");
			e.printStackTrace();
		}
		
	}
	private static int prompt() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s=new String();
		System.out.println("Main menu:\n1.Create automaton\n2.Minimize automaton\n3.Print automaton\n4.Exit");
		int n=1;
		try {
			s = br.readLine();
		    n = Integer.parseInt(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}
	
	private static ArrayList<State> addTransitionsToStates(ArrayList<Transition> transitions, ArrayList<State> states) {
		// TODO Auto-generated method stub
		ArrayList<State> newStates = new ArrayList<State>();
		for (Iterator<State> iterator = states.iterator(); iterator.hasNext();) {
			State state = (State) iterator.next();
			for (Iterator<Transition> iterator2 = transitions.iterator(); iterator2.hasNext();) {
				Transition transition = (Transition) iterator2.next();
				if (transition.getOrigin().equals(state)) {
					state.addTransition(transition);
				}
			}newStates.add(state);
		}
		return newStates;
	}
	private static String promptAlphabet() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s=new String();
		System.out.println("Enter your alphabet:(example input: 'a,b' without quotations.)");
		try {
			s = br.readLine();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	private static ArrayList<State> genStates() {
		ArrayList<State> states = new ArrayList<State>();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Number of states of automaton:");
		 String n1;
		try {
			n1 = br.readLine();
			int n = Integer.parseInt(n1);
			int i = 0;
			while(i<n){
				String tag = tag();
				int type = type();
				State state = new State(tag,type);
				states.add(state);
				i++;
			}
		} catch (Exception e) {
			System.err.println("Invalid input in states\n");e.printStackTrace();
		}		
		return states;
		
	}
	
	static String tag(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String tag = "";
		try {
			System.out.println("enter tag:(example input: 'q1' without quotations.)");
			tag = br.readLine();			
		} catch (Exception e) {
			System.err.println("!^%^@");e.printStackTrace();
		}
		return tag;
	}
	static int type(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int type = 1;
		String s=new String();
		System.out.println("enter type:(0=initial 1=non-final 2=final)");
		try {
			s = br.readLine();
			type=Integer.parseInt(s);
		} catch (Exception e) {
			System.err.println("Invalid input\n");e.printStackTrace();
		}
		return type;
	}
	
	private static ArrayList<Transition> genTransitions(ArrayList<State>states,String[] alphabet) {
		
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		Scanner in = new Scanner(System.in);
		String s = "";
		
		for (Object object : states) {
			State a = (State) object;
			for(int i=1;i<=alphabet.length;i++){
				System.out.println("Adding transition "+(i)+" out of "+(alphabet.length)+" of "+a.getTag());
				System.out.println("(example input: 'a,q1' without quotations.)");
				s=in.next();
				//searchState in list;
				State b = foo(s,states);
				String[] splitted = s.split(",");
				//receives State origin, String inputCharacter, State destination
				Transition t = new Transition(a,splitted[0],b);
				transitions.add(t);
			}
		}
		return transitions;
	}
	
	/*extract the dest from input string*/
	public static State foo(String inputTransition, ArrayList<State>states){
		//input, destination... we want to extract destination
		String[] splitted = inputTransition.split(",");
		State a;
		
		for (Object object : states) {
			a=(State)object;
			if (a.getTag().equals(splitted[1])) {
				return a;
			}
		}
		System.err.println("State tag not found, initial state will be returned instead");
		a=(State)states.get(0);
		return a;
	}
}