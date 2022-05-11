package juego;

import entorno.Entorno;
import java.awt.*;

public class Mikasa {
    private int velocidad;
    private String estado = "normal";
    private Rectangle rec;

    public Mikasa(int x, int y, int velocidad) {
        this.velocidad = velocidad;
        this.rec = new Rectangle(x, y, 70, 70);
    }

    // Comprueba el estado de mikasa y dependiendo de cuál sea le cambia el color.
    public void dibujar(Entorno entorno) {
        if (this.estado.equals("normal")) {
            entorno.dibujarRectangulo(this.rec.x, this.rec.y, this.rec.width, this.rec.height, 0, Color.WHITE);
        } else if (this.estado.equals("especial")) {
            entorno.dibujarRectangulo(this.rec.x, this.rec.y, this.rec.width, this.rec.height, 0, Color.BLUE);
        } else if (this.estado.equals("colision")) {
            entorno.dibujarRectangulo(this.rec.x, this.rec.y, this.rec.width, this.rec.height, 0, Color.GRAY);
        }
    }

    // Funciones que mueven a Mikasa a un lado o a otro. Además verifican que no haya una colision con los edificios.

    public void moverArriba() {
        this.setY(this.getY() - this.getVelocidad());
    }

    public void moverAbajo() {
        this.setY(this.getY() + this.getVelocidad());
    }

    public void moverIzquierda() {
        this.setX(this.getX() - this.getVelocidad());
    }

    public void moverDerecha() {
        this.setX(this.getX() + this.getVelocidad());
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

    public int getVelocidad() {
        return velocidad;
    }

    public String getEstado() {
        return estado;
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

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}