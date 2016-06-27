package br.projeto.democanvasandroid.model;

public class Reta {

    private float xInicio;
    private float yInicio;
    private float xFinal;
    private float yFinal;

    public Reta(float xInicio, float yInicio, float xFinal, float yFinal) {
        this.xInicio = xInicio;
        this.yInicio = yInicio;
        this.xFinal = xFinal;
        this.yFinal = yFinal;
    }

    public float getxInicio() {
        return xInicio;
    }

    public void setxInicio(float xInicio) {
        this.xInicio = xInicio;
    }

    public float getyInicio() {
        return yInicio;
    }

    public void setyInicio(float yInicio) {
        this.yInicio = yInicio;
    }

    public float getxFinal() {
        return xFinal;
    }

    public void setxFinal(float xFinal) {
        this.xFinal = xFinal;
    }

    public float getyFinal() {
        return yFinal;
    }

    public void setyFinal(float yFinal) {
        this.yFinal = yFinal;
    }
}