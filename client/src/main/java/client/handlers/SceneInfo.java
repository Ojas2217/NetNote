package client.handlers;

import javafx.geometry.Point2D;

/**
 * Handler class for storing stage information
 *
 * <p>
 *     The {@code SceneInfo} class handles the storing of stage information so that it can be used to
 *     restore a scene with the same size and position as it was closed with.
 * </p>
 */
public class SceneInfo {
    private final Point2D size;
    private final Point2D pos;

    public SceneInfo(Point2D size, Point2D pos) {
        this.size = size;
        this.pos = pos;
    }

    public Point2D getSize() {
        return size;
    }

    public Point2D getPos() {
        return pos;
    }
}
