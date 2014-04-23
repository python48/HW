package tarea6;
import java.util.ArrayList;
import java.util.Iterator;

public class State {

	private String tag="";
	private int type=1;
	private ArrayList<Transition> transitions=new ArrayList<Transition>();
	
	/*tag for the name as string and type as int(0=initial, 1=non-terminal, 2=terminal)*/
	public State(String tag,int type){
		this.tag=tag;
		this.type=type;
	}
	/*non initialized State*/
	public State(){}
	
	
	public boolean isFinal(){
		if (this.getType()==2) {
			return true;
		}else return false;
	}
	
	public State transition(State origin,String input){
		for (Iterator<Transition> object = this.transitions.iterator(); object.hasNext();) {
			Transition transition = (Transition) object.next();
			if (transition.getInput().equals(input)) {
				return transition.getTransition();
			}
		}
		return null;
	}
	public ArrayList<Transition>getTransitions(){
		return transitions;
	}
	
	public String getTag(){
		return tag;
	}
	public int getType(){
		return type;
	}
	
	public void addTransition(Transition t){
		this.transitions.add(t);
	}
	/*set tag or name of node*/
	public void setTag(String tag){
		this.tag=tag;
	}
	/*set type of node*/
	public void setType(int type){
		this.type=type;
	}
	public void setTransitions(ArrayList<Transition>t)
	{
		this.transitions=t;
	}
}