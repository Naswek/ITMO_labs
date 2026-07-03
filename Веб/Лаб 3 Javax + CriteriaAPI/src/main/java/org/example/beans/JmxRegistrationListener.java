package org.example.mbean;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.management.ManagementFactory;

@WebListener
public class JmxRegistrationListener implements ServletContextListener {

    private ObjectName pointsCounterName;
    private ObjectName missPercentageName;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            
            pointsCounterName = new ObjectName("org.example.mbean:type=PointsCounter");
            if (mbs.isRegistered(pointsCounterName)) {
                mbs.unregisterMBean(pointsCounterName);
            }
            mbs.registerMBean(MBeanRegistry.getPointsCounter(), pointsCounterName);

            missPercentageName = new ObjectName("org.example.mbean:type=MissPercentage");
            if (mbs.isRegistered(missPercentageName)) {
                mbs.unregisterMBean(missPercentageName);
            }
            mbs.registerMBean(MBeanRegistry.getMissPercentage(), missPercentageName);
            
            System.out.println("MBeans успешно зарегистрированы с JMX.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            if (pointsCounterName != null && mbs.isRegistered(pointsCounterName)) {
                mbs.unregisterMBean(pointsCounterName);
            }
            if (missPercentageName != null && mbs.isRegistered(missPercentageName)) {
                mbs.unregisterMBean(missPercentageName);
            }
            System.out.println("MBeans успешно отрегистрированы с JMX.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
