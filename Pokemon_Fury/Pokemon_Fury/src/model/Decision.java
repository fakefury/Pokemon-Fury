package model;

public class Decision {

    private int idDecision;
    private String descripcion;
    private String opcionA;
    private String opcionB;
    private String consecuenciaA;
    private String consecuenciaB;

    public Decision() {
    }

    public Decision(int idDecision, String descripcion, String opcionA, String opcionB, String consecuenciaA, String consecuenciaB) {
        this.idDecision = idDecision;
        this.descripcion = descripcion;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.consecuenciaA = consecuenciaA;
        this.consecuenciaB = consecuenciaB;
    }

    public int getIdDecision() {return idDecision;}
    public void setIdDecision(int idDecision) {this.idDecision = idDecision;}
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public String getOpcionA() {return opcionA;}
    public void setOpcionA(String opcionA) {this.opcionA = opcionA;}
    public String getOpcionB() {return opcionB;}
    public void setOpcionB(String opcionB) {this.opcionB = opcionB;}
    public String getConsecuenciaA() {return consecuenciaA;}
    public void setConsecuenciaA(String consecuenciaA) {this.consecuenciaA = consecuenciaA;}
    public String getConsecuenciaB() {return consecuenciaB;}
    public void setConsecuenciaB(String consecuenciaB) {this.consecuenciaB = consecuenciaB;}

}
