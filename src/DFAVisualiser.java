import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.geom.*;

public class DFAVisualiser extends JPanel implements MouseListener, MouseMotionListener {
    Graphics2D g2;
    DrawableCompoundState[] squares;
    Color colour;
    DFA d;

    double offsetX, offsetY;
    DrawableCompoundState selected;

    boolean dragging = false;

    public DFAVisualiser(DFA d) {
        this.d = d;
        squares = new DrawableCompoundState[d.getStates().size()];
        int i = 0;
        for (CompoundState cs : d.getStates()) {
            squares[i] = new DrawableCompoundState(80 * ((i % 8) + 1), 100 + 80 * (i / 8) + i, cs, d.getStateTransitions(cs));
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
            if (squares[i].getCompoundState().isFinal()) {
                g2.draw(squares[i].getFinalRing());
            }

            // Draw state name
            g2.drawString(squares[i].getCompoundState().getName(),
                    (int) squares[i].getEllipse2d().getCenterX() - squares[i].getCompoundState().getName().length() * 2,
                    (int) squares[i].getEllipse2d().getCenterY() + 5);
            int c = 0;

            // cater for arrow to initial node
            if (squares[i].getCompoundState().isInitial()) {
                drawArrowLine(g2, squares[i].getEllipse2d().getCenterX() - 50, squares[i].getEllipse2d().getCenterY(),
                        squares[i].getEllipse2d().getMinX(), squares[i].getEllipse2d().getCenterY(), 10, 10,
                        "",0);
            }
            Set<OrderedPair<CompoundState, String>> set=squares[i].transition.keySet();
           int strNum=0;
            // Draw transitions
            for (OrderedPair<CompoundState, String> op : squares[i].transition.keySet()) {
                
                    CompoundState compoundState=squares[i].getTransition().get(op);
                 
                    drawArrowLine(g2, squares[i].getEllipse2d().getCenterX(), squares[i].getEllipse2d().getCenterY(),
                            getDrawableState(compoundState).getEllipse2d().getCenterX(),
                            getDrawableState(compoundState).getEllipse2d().getCenterY(), 10,
                            10, op.getObj2(),strNum);
                            strNum++;
                
            }

        }

    }

    private void drawArrowLine(Graphics2D g, double x1, double y1, double x2, double y2, int d, int h, String symbol,int strNum) {
        
        
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

        if(x1==x2 && y1==y2){
            midY-=100;
            x1-=DrawableCompoundState.SIZE/4;
            x2+=DrawableCompoundState.SIZE/4;
            y1-=DrawableCompoundState.SIZE/2;
            y2-=DrawableCompoundState.SIZE/2;
            g2.drawString(symbol,
                (midX+strNum*10-15),
                midY+30);
        }else{
            g2.drawString(symbol,
            (midX+strNum*10),
            midY);

        }
        int[] xpoints = { (int) x2, (int) xm, (int) xn };
        int[] ypoints = { (int) y2, (int) ym, (int) yn };

        
        
        

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

    public DrawableCompoundState getClickedShape(MouseEvent ev) {
        for (int j = 0; j < squares.length; j++) {
            if (squares[j].getEllipse2d().contains(getMousePosition())) {
                
                return squares[j];
            }
        }

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
        

    }

    public DrawableCompoundState getDrawableState(CompoundState s) {
        for (int i = 0; i < squares.length; i++) {
            if (squares[i].getCompoundState() == s) {
                return squares[i];
            }
        }
        System.out.println("Could not find drawable state");
        return null;
    }

}