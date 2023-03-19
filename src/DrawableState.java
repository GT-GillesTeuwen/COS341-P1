
import java.awt.geom.*;
import java.util.Map;

public class DrawableState {
    public static final int SIZE = 40;
    Ellipse2D ellipse2d;
    State state;
    Map<OrderedPair<State, String>, Map<State, State>> transition;

    public DrawableState(int initialX, int initialY, State s,
            Map<OrderedPair<State, String>, Map<State, State>> transition) {
        this.ellipse2d = new Ellipse2D.Double(initialX, initialY, SIZE, SIZE);
        this.state = s;
        this.transition = transition;
    }

    public Ellipse2D getEllipse2d() {
        return ellipse2d;
    }

    public State getState() {
        return state;
    }

    public Map<OrderedPair<State, String>, Map<State, State>> getTransition() {
        return transition;
    }

    public Ellipse2D getFinalRing() {
        return new Ellipse2D.Double(ellipse2d.getMinX() - 5, ellipse2d.getMinY() - 5, SIZE + 10, SIZE + 10);
    }
}
