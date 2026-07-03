package org.example.mbean;

import org.junit.jupiter.api.Test;

import javax.management.Notification;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MBeanMetricsTest {

    @Test
    void pointsCounterCountsTotalHitsAndOutOfBoundsNotifications() {
        PointsCounter counter = new PointsCounter();
        List<Notification> notifications = new ArrayList<>();
        counter.addNotificationListener((notification, handback) -> notifications.add(notification), null, null);

        counter.addPoint(1.0, 1.0, true);
        counter.addPoint(6.0, 0.0, false);

        assertEquals(2, counter.getTotalPoints());
        assertEquals(1, counter.getHitPoints());
        assertEquals(1, notifications.size());
        assertEquals("org.example.mbean.outOfBounds", notifications.get(0).getType());
    }

    @Test
    void missPercentageUsesCurrentPointsCounterValues() {
        PointsCounter counter = new PointsCounter();
        MissPercentage missPercentage = new MissPercentage(counter);

        counter.addPoint(0.0, 0.0, true);
        counter.addPoint(1.0, 1.0, false);
        counter.addPoint(2.0, 2.0, false);
        counter.addPoint(3.0, 3.0, true);

        assertEquals(50.0, missPercentage.getMissPercentage());
    }
}
