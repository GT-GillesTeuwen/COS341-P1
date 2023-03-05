import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.geom.*;

public class FAVisualiser extends JPanel implements MouseListener, MouseMotionListener {
    Graphics2D g2;
    DrawableState[] squares;
    Color colour;
    NFA n;

    double offsetX, offsetY;
    DrawableState selected;

    boolean dragging = false;

    public FAVisualiser(NFA n) {
        this.n = n;
        squares = new DrawableState[n.getStates().size()];
        int i = 0;
        for (State s : n.getStates().keySet()) {
            squares[i] = new DrawableState(80 * ((i % 8) + 1), 100 + 80 * (i / 8) + i, s, n.getStateTransitions(s));
            i++;
        }

        colour = Color.BLACK;

        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        this.requestFocus();

    }

    public void paintComponent(Graphics g) {
        final BasicStroke dashed = new BasicStroke(3.0f);

        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setStroke(dashed);
        g2.setColor(colour);
        for (int i = 0; i < squares.length; i++) {

            // Draw state circle
            g2.draw(squares[i].getEllipse2d());
            if (squares[i].getState().isFinal()) {
                g2.draw(squares[i].getFinalRing());
            }

            // Draw state name
            g2.drawString(squares[i].getState().getName(),
                    (int) squares[i].getEllipse2d().getCenterX() - squares[i].getState().getName().length() * 2,
                    (int) squares[i].getEllipse2d().getCenterY() + 5);
            int c = 0;

            // cater for arrow to initial node
            if (squares[i].getState().isInitial()) {
                drawArrowLine(g2, squares[i].getEllipse2d().getCenterX() - 50, squares[i].getEllipse2d().getCenterY(),
                        squares[i].getEllipse2d().getMinX(), squares[i].getEllipse2d().getCenterY(), 10, 10,
                        "");
            }

            // Draw transitions
            for (OrderedPair<State, String> op : squares[i].transition.keySet()) {
                for (State state : squares[i].getTransition().get(op).keySet()) {

                    drawArrowLine(g2, squares[i].getEllipse2d().getCenterX(), squares[i].getEllipse2d().getCenterY(),
                            getDrawableState(state).getEllipse2d().getCenterX(),
                            getDrawableState(state).getEllipse2d().getCenterY(), 10,
                            10, op.getObj2());
                }
            }

        }

    }

    private void drawArrowLine(Graphics2D g, double x1, double y1, double x2, double y2, int d, int h, String symbol) {
        int midX = (int) ((x1 + x2) / 2);
        int midY = (int) ((y1 + y2) / 2);

        double dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double sin = dy / D, cos = dx / D;

        // Initial down and left of final
        if (sin < 0 && cos > 0) {
            y1 -= (DrawableState.SIZE / 2);
            y2 -= (DrawableState.SIZE / 4);
            x2 -= (DrawableState.SIZE / 2);
            midY -= 40;
            midX -= 40;
        }
        // Initial up and left of final
        else if (sin > 0 && cos > 0) {
            y1 += (DrawableState.SIZE / 2);
            y2 += (DrawableState.SIZE / 4);
            x2 -= (DrawableState.SIZE / 2);
            midY += 40;
            midX -= 40;
        }

        // Initial down and right of final
        if (sin < 0 && cos < 0) {
            y1 -= (DrawableState.SIZE / 2);
            y2 += (DrawableState.SIZE / 4);
            x2 += (DrawableState.SIZE / 2);
            midY -= 40;
            midX += 40;
        }
        // Initial up and right of final
        else if (sin > 0 && cos < 0) {
            y1 += (DrawableState.SIZE / 2);
            y2 -= (DrawableState.SIZE / 4);
            x2 += (DrawableState.SIZE / 2);
            midY += 40;
            midX += 40;
        }

        dx = x2 - midX;
        dy = y2 - midY;
        D = Math.sqrt(dx * dx + dy * dy);
        sin = dy / D;
        cos = dx / D;
        double xm = D - d, xn = xm, ym = h, yn = -h, x;

        x = xm * cos - ym * sin + midX;
        ym = xm * sin + ym * cos + midY;
        xm = x;

        x = xn * cos - yn * sin + midX;
        yn = xn * sin + yn * cos + midY;
        xn = x;

        int[] xpoints = { (int) x2, (int) xm, (int) xn };
        int[] ypoints = { (int) y2, (int) ym, (int) yn };

        g2.drawString(symbol,
                midX,
                midY);

        // g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

        Path2D path = new Path2D.Double();
        path.moveTo(x1, y1);
        path.quadTo(midX,
                midY, x2, y2);
        g.draw(path);

        g.fillPolygon(xpoints, ypoints, 3);
    }

    @Override
    public void mouseDragged(MouseEvent ev) {
        if (dragging) {
            double mx = ev.getX();
            double my = ev.getY();

            double x1 = mx - offsetX;
            double y1 = my - offsetY;
            selected.getEllipse2d().setFrame(x1, y1, selected.getEllipse2d().getHeight(),
                    selected.getEllipse2d().getWidth());
            // selected = new Rectangle2D.Double(x1, y1, selected.getHeight(),
            // selected.getWidth());
            repaint();
        }

    }

    @Override
    public void mousePressed(MouseEvent ev) {
        double mx = ev.getX();
        double my = ev.getY();

        this.selected = getClickedShape(ev);

        if (selected != null) {
            double x1 = selected.getEllipse2d().getMinX();
            double y1 = selected.getEllipse2d().getMinY();

            if (mx > selected.getEllipse2d().getMinX() && mx < selected.getEllipse2d().getMaxX()
                    && my > selected.getEllipse2d().getMinY()
                    && my < selected.getEllipse2d().getMaxY()) {
                dragging = true;
                offsetX = mx - x1;
                offsetY = my - y1;
            }
        }

    }

    public DrawableState getClickedShape(MouseEvent ev) {
        for (int j = 0; j < squares.length; j++) {
            if (squares[j].getEllipse2d().contains(getMousePosition())) {
                System.out.println("yes");
                return squares[j];
            }
        }

        System.out.println("no");
        return null;
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        dragging = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public static void main(String args[]) {
        // create an instance of JFrame class
        JFrame frame = new JFrame();
        // set size, layout and location for frame.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RegularExpression r = new RegularExpression(
                "(-.(1|2|3|4|5|6|7|8|9).(0|1|2|3|4|5|6|7|8|9)*)|((1|2|3|4|5|6|7|8|9).(0|1|2|3|4|5|6|7|8|9)*)");
        r.insertBrackets();
        AbstractExpression a = r.buildTree();
        NFA nfa = a.interpret();
        FAVisualiser p = new FAVisualiser(nfa);
        frame.add(p);
        frame.setSize(800, 800);
        frame.setLocation(200, 200);
        frame.setVisible(true);

    }

    public DrawableState getDrawableState(State s) {
        for (int i = 0; i < squares.length; i++) {
            if (squares[i].state == s) {
                return squares[i];
            }
        }
        System.out.println("Could not find drawable state");
        return null;
    }

}