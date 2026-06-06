package io.github.maze.util;

public class Angle {

    private double radians;

    public Angle(double radians) {
        setRadians(radians);
    }

    public double getRadians() { return radians; }
    public void setRadians(double radians) {
        // clamps radians to
        // 0 <= radians <= 2 * PI
        this.radians = radians % (2 * Math.PI);
        if (this.radians < 0) {
            this.radians += (2 * Math.PI);
        }
    }

    public double getDegrees() { return Math.toDegrees(radians); }
    public void setDegrees(double degrees) {
        setRadians(Math.toRadians(degrees));
    }

    public double cos() { return Math.cos(radians); }
    public double sin() { return Math.sin(radians); }

    public void add(double radiansToAdd) {
        setRadians(radians + radiansToAdd);
    }

    public void lookAt(double startX, double startY, double targetX, double targetY) {
        double dx = targetX - startX;
        double dy = targetY - startY;
        setRadians(Math.atan2(dy, dx));
    }

    public static double between(double startX, double startY, double targetX, double targetY) {
        double dx = targetX - startX;
        double dy = targetY - startY;
        return Math.atan2(dy, dx);
    }
}
