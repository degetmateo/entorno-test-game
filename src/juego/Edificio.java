package juego;

import entorno.Entorno;
import entorno.Herramientas;

import java.awt.*;

public class Edificio {
    private Rectangle rec;
    private Image img;

    public Edificio(int x, int y, int ancho, int alto) {
        this.rec = new Rectangle(x, y, ancho, alto);
        this.img = Herramientas.cargarImagen("edificio.png");
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.getX(), this.getY(), this.getAncho(), this.getAlto(), 0, Color.YELLOW);
        entorno.dibujarImagen(this.getImg(), this.getX(), this.getY(), 0, 0.2);
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

    public Image getImg() {
        return this.img;
    }

    public void setX(int x) {
        this.rec.x = x;
    }

    public void setY(int y) {
        this.rec.height = y;
    }

    public void setAncho(int ancho) {
        this.rec.width = ancho;
    }

    public void setAlto(int alto) {
        this.rec.height = alto;
    }

    public void setImg(String img) {
        this.img = Herramientas.cargarImagen(img);
    }
}