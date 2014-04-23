package tarea6;

public class Transition {
	private State origin;
	private String input;
	private State destination;
	
	//Constructor of class
	public Transition(State origin, String input, State destination){
		this.origin=origin;
		this.input=input;
		this.destination=destination;
	}
	public Transition(){}
	
	public State getTransition(){
		return this.destination;
	}
	
	//Getters and Setters
	public State getOrigin() {
		return origin;
	}
	public void setOrigin(State origin) {
		this.origin = origin;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public State getDestination() {
		return destination;
	}
	public void setDestination(State destination) {
		this.destination = destination;
	}
	
}