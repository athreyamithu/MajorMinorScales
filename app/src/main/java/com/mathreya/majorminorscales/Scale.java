package com.mathreya.majorminorscales;

/**
 * Created by mithileshathreya on 10/25/17.
 */

public class Scale {
    private String name;
    private String formula;

    public Scale() {
    }

    public Scale(String name, String formula) {
        this.name = name;
        this.formula = formula;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
