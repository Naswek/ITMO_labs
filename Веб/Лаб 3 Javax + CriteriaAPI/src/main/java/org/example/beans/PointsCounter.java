package org.example.mbean;

import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class PointsCounter extends NotificationBroadcasterSupport implements PointsCounterMBean {
    private int totalPoints = 0;
    private int hitPoints = 0;
    private long sequenceNumber = 1;

    @Override
    public synchronized int getTotalPoints() {
        return totalPoints;
    }

    @Override
    public synchronized int getHitPoints() {
        return hitPoints;
    }

    public synchronized void setCounts(int total, int hits) {
        this.totalPoints = total;
        this.hitPoints = hits;
    }

    public synchronized void addPoint(Double x, Double y, boolean hit) {
        totalPoints++;
        if (hit) {
            hitPoints++;
        }
        if (x == null || y == null) {
            return;
        }
        if (x < -3.0 || x > 5.0 || y < -3.0 || y > 5.0) {
            Notification notification = new Notification(
                "org.example.mbean.outOfBounds",
                this,
                sequenceNumber++,
                System.currentTimeMillis(),
                String.format("Точка находится вне видимой области координат [-3, 5]", x, y)
            );
            sendNotification(notification);
        }
    }

    public synchronized void reset() {
        totalPoints = 0;
        hitPoints = 0;
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] { "org.example.mbean.outOfBounds" };
        String name = Notification.class.getName();
        String description = "Точка находится вне видимой области координат [-3, 5]";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[] { info };
    }
}
