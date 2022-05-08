package juego;

import entorno.Entorno;
import java.awt.*;

public class Mikasa {
    int x;
    int y;
    int ancho = 50;
    int alto = 50;
    int factor_velocidad;
    String estado = "normal";

    public Mikasa(int x, int y, int factor_velocidad) {
        this.x = x;
        this.y = y;
        this.factor_velocidad = factor_velocidad;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(x, y, this.ancho, this.alto, 0, Color.WHITE);
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
}