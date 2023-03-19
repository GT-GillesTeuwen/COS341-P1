
import java.awt.geom.*;
import java.util.Map;

public class DrawableCompoundState {
    public static final int SIZE = 40;
    Ellipse2D ellipse2d;
    CompoundState compoundState;
    Map<OrderedPair<CompoundState, String>, CompoundState> transition;

    public DrawableCompoundState(int initialX, int initialY, CompoundState cs,
            Map<OrderedPair<CompoundState, String>, CompoundState> transition) {
        this.ellipse2d = new Ellipse2D.Double(initialX, initialY, SIZE, SIZE);
        this.compoundState = cs;
        this.transition = transition;
    }

    public Ellipse2D getEllipse2d() {
        return ellipse2d;
    }

    public CompoundState getCompoundState() {
        return compoundState;
    }

    public Map<OrderedPair<CompoundState, String>, CompoundState> getTransition() {
        return transition;
    }

    public Ellipse2D getFinalRing() {
        return new Ellipse2D.Double(ellipse2d.getMinX() - 5, ellipse2d.getMinY() - 5, SIZE + 10, SIZE + 10);
    }
}
