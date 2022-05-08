package juego;

import entorno.Entorno;
import java.awt.*;

public class Edificio {
    private int x;
    private int y;
    private int ancho = 50;
    private int alto = 50;

    public Edificio(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(x, y, this.ancho, this.alto, 0, Color.YELLOW);
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