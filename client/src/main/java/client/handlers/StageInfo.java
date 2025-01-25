package client.handlers;

/**
 * Record class for storing stage information
 *
 * <p>
 * The {@code SceneInfo} class handles the storing of stage information so that it can be used to
 * restore a scene with the same size and position as it was closed with.
 * </p>
 */
public record StageInfo(double width, double height, double x, double y) {
    @Override
    public double width() {
        return width;
    }

    @Override
    public double height() {
        return height;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }
}
