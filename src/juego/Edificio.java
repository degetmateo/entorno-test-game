package juego;

import entorno.Entorno;
import java.awt.*;

public class Edificio {
    private int x;
    private int y;
    private int ancho;
    private int alto;

    public Edificio(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(x, y, this.ancho, this.alto, 0, Color.YELLOW);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getAncho() {
        return this.ancho;
    }

    public int getAlto() {
        return this.alto;
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
}