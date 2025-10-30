package org.example.beans;

//import jakarta.enterprise.context.ApplicationScoped;

import javax.faces.bean.ApplicationScoped;
import java.io.Serializable;

@ApplicationScoped
public class Validation implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean checkArea(Double x, Double y, Double r) {

        return isInRectangle(x, y, r) ||
                isInTriangle(x, y, r) ||
                isInSector(x, y, r);
    }

    private boolean isInRectangle(double x, double y, double r) {
        return x >= 0 && x <= r && y >= 0 && y <= r/2;
    }

    private boolean isInTriangle(double x, double y, double r) {
        return x <= 0 && y <= 0 && y >= -x - r;
    }

    private boolean isInSector(double x, double y, double r) {
        return x <= 0 && y >= 0 && (x*x + y*y <= r*r);
    }
}
