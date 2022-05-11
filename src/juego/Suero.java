package juego;

import entorno.Entorno;
import java.awt.*;

public class Suero {
    private Rectangle rec;

    public Suero(int x, int y) {
        this.rec = new Rectangle(x, y, 70, 70);
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.getX(), this.getY(), this.getAncho(), this.getAlto(), 0, Color.GREEN);
    }

    public int getX() {
        return this.rec.x;
    }

    public int getY() {
        return this.rec.y;
    }

    public int getAncho() {
        return this.rec.width;
    }

    public int getAlto() {
        return this.rec.height;
    }

    public Rectangle getRec() {
        return this.rec;
    }

    public void setX(int x) {
        this.rec.x = x;
    }

    public void setY(int y) {
        this.rec.y = y;
    }

    public void setAncho(int ancho) {
        this.rec.width = ancho;
    }

    public void setAlto(int alto) {
        this.rec.height = alto;
    }
}