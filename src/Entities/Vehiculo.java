package Entities;

import java.io.Serializable;

public class Vehiculo implements Serializable {

    private int id;

    private String placa;

    private int tipoVehiculo;

   private float tiempoEstacionado;

    public Vehiculo() {}

    public Vehiculo(int id, String placa, int tipoVehiculo) {
        this.id = id;
        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(int tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public float getTiempoEstacionado() {
        return tiempoEstacionado;
    }

    public void setTiempoEstacionado(float tiempoEstacionado) {
        this.tiempoEstacionado = tiempoEstacionado;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", tipoVehiculo=" + tipoVehiculo +
                ", tiempoEstacionado=" + tiempoEstacionado +
                '}';
    }
}
