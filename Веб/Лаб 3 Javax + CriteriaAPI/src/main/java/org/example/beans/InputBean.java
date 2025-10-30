package org.example.beans;

//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.faces.application.FacesMessage;
//import jakarta.faces.context.FacesContext;
//import jakarta.inject.Inject;
//import jakarta.inject.Named;
import javax.ejb.EJB;
import javax.faces.annotation.ManagedProperty;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.example.entity.Result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//@Named("inputBean")
@ApplicationScoped
@ManagedBean(name = "inputBean")
public class InputBean implements Serializable {


    @EJB
    private transient ResultService resultService;

    private transient Validation validation;

    private ArrayList<Double> selectedRValues;
    private Double xValue;
    private Double hiddenXValue;
    private Double yValue;

    private final List<Double> VALID_X_VALUES = Arrays.asList(-3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0);
    private final List<Double> VALID_R_VALUES = Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0);

    public InputBean() {
        validation = new Validation();
    }

    public List<Double> getSelectedRValues() {
        return selectedRValues;
    }

    public void setResultService(ResultService resultService) {
        this.resultService = resultService;
    }

    public Double getXValue() {
        return xValue;
    }

    public Double getYValue() {
        return yValue;
    }

    public Double getHiddenXValue() {
        return hiddenXValue;
    }

    public void setHiddenXValue(Double hiddenXValue) {
        this.hiddenXValue = hiddenXValue;
    }

    public List<Double> getValidXValues() {
        return VALID_X_VALUES;
    }

    public List<Double> getValidRValues() {
        return VALID_R_VALUES;
    }

    public void setSelectedRValues(ArrayList<Double> selectedRValues) {
        this.selectedRValues = selectedRValues;
    }

    public void setXValue(Double xValue) {
        this.xValue = xValue;
    }

    public void setYValue(Double yValue) {
        this.yValue = yValue;
    }

    public List<Result> getResults() {
        List<Result> results = resultService.findAll();
        if (results != null) return results;
        return new ArrayList<>();
    }

    public void clearHistory() {
        resultService.clear();
    }

    public void submit() {
        Double validX = (hiddenXValue != null) ? hiddenXValue : xValue;
//        validateInput();
        for (Double rValue : selectedRValues) {
            boolean hit = validation.checkArea(validX, yValue, rValue);
            Result result = createResult(validX, yValue, rValue, hit);
            resultService.save(result);
        }
    }

    private Result createResult(Double x, Double y, Double r, boolean hit) {
        Result result = new Result();
        result.setX(x);
        result.setY(y);
        result.setR(r);
        result.setHit(hit);
        return result;
    }
}