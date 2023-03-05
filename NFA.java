import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NFA {
  public static final String EPSILON = "ε";
  private Map<State, State> states;
  private Map<String, String> alphabet;
  private Map<OrderedPair<State, String>, Map<State, State>> transition; // Structure (current state +
                                                                         // alphabet-element)->(set of states that can
                                                                         // be reached)
  private State initialState;
  private Map<State, State> acceptStates;

  public NFA(String symbol) {
    states = new HashMap<>();
    alphabet = new HashMap<>();
    transition = new HashMap<>();
    acceptStates = new HashMap<>();

    alphabet.put(symbol, symbol);

    initialState = new State("q" + states.size(), false, true);
    states.put(initialState, initialState);

    State finalState = new State("q" + states.size(), true, false);
    states.put(finalState, finalState);
    acceptStates.put(finalState, finalState);

    OrderedPair<State, String> op = new OrderedPair<State, String>(initialState, symbol);
    Map<State, State> set = new HashMap<>();
    set.put(finalState, finalState);
    transition.put(op, set);
  }

  public void manageStarOperator() {

    // Links all final states to the initial states
    // Used mainly for the * operator interpret
    for (State key : acceptStates.keySet()) {
      addTransition(key, EPSILON, initialState);
    }

    // Adds new initial state which epsilon links to old initial state
    State oldInitial = initialState;
    oldInitial.setInitial(false);
    initialState = new State("q" + states.size(), true, true);
    states.put(initialState, initialState);

    addTransition(initialState, EPSILON, oldInitial);

    acceptStates.put(initialState, initialState);
    

  }

  public void managePlusOperator(){
    // Links all final states to the initial states
    // Used mainly for the * operator interpret
    for (State key : acceptStates.keySet()) {
      addTransition(key, EPSILON, initialState);
    }

    // Adds new initial state which epsilon links to old initial state
    State oldInitial = initialState;
    oldInitial.setInitial(false);
    initialState = new State("q" + states.size(), false, true);
    states.put(initialState, initialState);

    addTransition(initialState, EPSILON, oldInitial);

    
  }

  public void manageOptionalOperator(){

    initialState.setInitial(false);
    State newInitialState =new State("q" + states.size(), false, true);
    states.put(newInitialState, newInitialState);
    addTransition(newInitialState, EPSILON, initialState);

    for(State s: acceptStates.keySet()){
      addTransition(newInitialState, EPSILON, s);
    }
    initialState=newInitialState;
  }

  public void manageAndOperator(NFA leftChild) {
    basicMerge(leftChild);

    // epsilon transition from all left child accept states to right child initial
    // state
    for (State key : leftChild.acceptStates.keySet()) {
      key.setFinal(false);
      addTransition(key, EPSILON, initialState);

    }

    // Set initial state of entire NFA to initial state of left NFA
    initialState.setInitial(false);
    initialState = leftChild.initialState;

  }

  public void manageOrOperator(NFA leftChild) {
    basicMerge(leftChild);
    for (State key : leftChild.acceptStates.keySet()) {
      acceptStates.put(key, key);
    }

    State newInitialState = new State("q" + states.size(), false, true);
    states.put(newInitialState, newInitialState);

    addTransition(newInitialState, EPSILON, leftChild.initialState);
    addTransition(newInitialState, EPSILON, initialState);

    initialState.setInitial(false);
    leftChild.initialState.setInitial(false);
    initialState = newInitialState;
  }

  private void basicMerge(NFA otherNfa) {
    // Add states from left child of the and operand to this NFA(right child)
    // Also rename them
    for (State key : otherNfa.states.keySet()) {
      key.setName("q" + states.size());
      states.put(key, key);
    }

    // Add transitions from left child of the and operand to this NFA(right child)
    for (OrderedPair<State, String> key : otherNfa.transition.keySet()) {
      transition.put(key, otherNfa.transition.get(key));
    }

    // Combine alphabets
    for (String key : otherNfa.alphabet.keySet()) {
      alphabet.put(key, key);
    }
  }

  private void addTransition(State current, String symbol, State next) {
    OrderedPair<State, String> orderedPair = new OrderedPair<State, String>(current, symbol);
    Map<State, State> s = transition.get(orderedPair);
    if (s == null) {
      s = new HashMap<>();
      s.put(next, next);
    } else {
      s.put(next, next);
    }
    transition.put(orderedPair, s);
  }

  public Map<State, State> getStates() {
    return states;
  }

  public Map<OrderedPair<State, String>, Map<State, State>> getStateTransitions(State s) {
    Map<OrderedPair<State, String>, Map<State, State>> stateTransitions = new HashMap<>();
    for (OrderedPair<State, String> op : transition.keySet()) {
      if (op.getObj1().equals(s)) {
        stateTransitions.put(op, transition.get(op));
      }
    }
    return stateTransitions;
  }

}