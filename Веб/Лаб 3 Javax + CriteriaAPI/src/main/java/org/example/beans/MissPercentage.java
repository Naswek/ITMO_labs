package org.example.mbean;

public class MissPercentage implements MissPercentageMBean {
    private final PointsCounter pointsCounter;

    public MissPercentage(PointsCounter pointsCounter) {
        this.pointsCounter = pointsCounter;
    }

    @Override
    public double getMissPercentage() {
        int total = pointsCounter.getTotalPoints();
        if (total == 0) {
            return 0.0;
        }
        int hits = pointsCounter.getHitPoints();
        int misses = total - hits;
        return ((double) misses / total) * 100.0;
    }
}
