package org.example.cords;

public class CordValidation {

    public boolean validate(double x, double y, double r) {
        return isHitSquare(x, y, r) || isHitCircle(x, y, r) || isHitTriangle(x, y, r);
    }

    private boolean isHitSquare(double x, double y, double r) {
        return (-r <= x && x <= 0) && (0 <= y && y <= r);
    }

    private boolean isHitTriangle(double x, double y, double r) {
        return (x <= 0) && (y <= 0) && (y <= x + r);
    }

    private boolean isHitCircle(double x, double y, double r) {
        return (x >= 0) && (y <= 0) && (x*x + y*y <= (r/2) * (r/2));
    }
}

