package org.example.mbean;

public class MBeanRegistry {
    private static final PointsCounter pointsCounter = new PointsCounter();
    private static final MissPercentage missPercentage = new MissPercentage(pointsCounter);

    public static PointsCounter getPointsCounter() {
        return pointsCounter;
    }

    public static MissPercentage getMissPercentage() {
        return missPercentage;
    }
}
