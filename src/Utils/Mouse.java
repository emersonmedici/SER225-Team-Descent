package Utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// A small input helper, like Keyboard: remember a press until gameplay reads it.
public final class Mouse {
    private static boolean leftDown;
    private static boolean leftClickPending;

    private static final MouseListener LISTENER = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent event) {
            if (event.getButton() == MouseEvent.BUTTON1) {
                event.getComponent().requestFocusInWindow();
                pressLeft();
            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            if (event.getButton() == MouseEvent.BUTTON1) releaseLeft();
        }
    };

    private Mouse() { }

    private static synchronized void pressLeft() {
        if (!leftDown) leftClickPending = true;
        leftDown = true;
    }

    private static synchronized void releaseLeft() { leftDown = false; }

    public static MouseListener getMouseListener() { return LISTENER; }

    public static synchronized boolean consumeLeftClick() {
        boolean clicked = leftClickPending;
        leftClickPending = false;
        return clicked;
    }

    public static synchronized void discardClicks() { leftClickPending = false; }

    public static synchronized void reset() {
        leftDown = leftClickPending = false;
    }
}
