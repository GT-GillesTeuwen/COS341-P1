
import java.awt.geom.*;
import java.util.Map;

public class DrawableStateGroup {
    public static final int SIZE = 40;
    Ellipse2D ellipse2d;
    StateGroup stateGroup;
    Map<OrderedPair<StateGroup, String>, StateGroup> transition;

    public DrawableStateGroup(int initialX, int initialY, StateGroup sg,
            Map<OrderedPair<StateGroup, String>, StateGroup> transition) {
        this.ellipse2d = new Ellipse2D.Double(initialX, initialY, SIZE, SIZE);
        this.stateGroup = sg;
        this.transition = transition;
    }

    public Ellipse2D getEllipse2d() {
        return ellipse2d;
    }

    public StateGroup getCompoundState() {
        return stateGroup;
    }

    public Map<OrderedPair<StateGroup, String>, StateGroup> getTransition() {
        return transition;
    }

    public Ellipse2D getFinalRing() {
        return new Ellipse2D.Double(ellipse2d.getMinX() - 5, ellipse2d.getMinY() - 5, SIZE + 10, SIZE + 10);
    }
}
