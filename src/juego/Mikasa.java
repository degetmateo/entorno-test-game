package juego;

import entorno.Entorno;
import java.awt.*;

public class Mikasa {
    private int x;
    private int y;
    private int ancho = 50;
    private int alto = 50;
    private int factor_velocidad;
    private String estado = "normal";

    public Mikasa(int x, int y, int factor_velocidad) {
        this.x = x;
        this.y = y;
        this.factor_velocidad = factor_velocidad;
    }

    public void dibujar(Entorno entorno) {
        if (this.estado.equals("normal")) {
            entorno.dibujarRectangulo(x, y, this.ancho, this.alto, 0, Color.WHITE);
        } else if (this.estado.equals("especial")) {
            entorno.dibujarRectangulo(x, y, this.ancho, this.alto, 0, Color.BLUE);
        } else if (this.estado.equals("colision")) {
            entorno.dibujarRectangulo(x, y, this.ancho, this.alto, 0, Color.RED);
        }
    }

    public void moverDerecha() {
        this.x += this.factor_velocidad;
    }

    public void moverIzquierda() {
        this.x -= this.factor_velocidad;
    }

    public void moverArriba() {
        this.y -= this.factor_velocidad;
    }

    public void moverAbajo() {
        this.y += this.factor_velocidad;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public int getFactor_velocidad() {
        return factor_velocidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public void setFactor_velocidad(int factor_velocidad) {
        this.factor_velocidad = factor_velocidad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}