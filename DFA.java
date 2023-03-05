import java.util.Map;

public class DFA {
    public static final String EPSILON = "ε";
    private Map<State, State> states;
    private Map<String, String> alphabet;
    private Map<OrderedPair<State, String>, State> transition; // Structure (current state +
                                                               // alphabet-element)->(state that will be reached)
    private State initialState;
    private Map<State, State> acceptStates;

}
