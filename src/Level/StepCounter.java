package Level;

// Turns distance travelled into footsteps: every strideLength pixels walked, addDistance() says "step now".
// Counting distance instead of time means running makes faster steps, mud makes slower ones,
// and walking into a wall makes none.
public class StepCounter {
    private final float strideLength;
    private float distanceSinceLastStep;

    public StepCounter(float strideLength) {
        this.strideLength = strideLength;
        reset();
    }

    // call with how far the walker moved this frame; returns true when it's time for a footstep
    public boolean addDistance(float distance) {
        distanceSinceLastStep += distance;
        if (distanceSinceLastStep >= strideLength) {
            distanceSinceLastStep -= strideLength;
            return true;
        }
        return false;
    }

    // call when the walker stops: the first step after starting again comes half a stride in, so it feels responsive
    public void reset() {
        distanceSinceLastStep = strideLength / 2f;
    }
}
