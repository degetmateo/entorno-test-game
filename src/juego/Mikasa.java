package juego;

import entorno.Entorno;
import java.awt.*;

public class Mikasa {
    private int x;
    private int y;
    private int ancho = 50;
    private int alto = 50;
    private int velocidad;
    private String estado = "normal";

    public Mikasa(int x, int y, int velocidad) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
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
        this.x += this.velocidad;

        for (int i = 0; i < Juego.edificios.length; i++) {
            if (Juego.colision(this.x, this.y, 
                Juego.edificios[i].getX(), Juego.edificios[i].getY(), 
                this.ancho, this.alto, 
                this.ancho, this.alto)) {
                
                this.x = Juego.edificios[i].getX() - this.ancho;
            }
        }
    }

    public void moverIzquierda() {
        this.x -= this.velocidad;

        for (int i = 0; i < Juego.edificios.length; i++) {
            if (Juego.colision(this.x, this.y,
                Juego.edificios[i].getX(), Juego.edificios[i].getY(),
                this.ancho, this.alto,
                this.ancho, this.alto)) {

                this.x = Juego.edificios[i].getX() + Juego.edificios[i].getAncho();
            }
        }
    }

    public void moverArriba() {
        this.y -= this.velocidad;

        for (int i = 0; i < Juego.edificios.length; i++) {
            if (Juego.colision(this.x, this.y,
                Juego.edificios[i].getX(), Juego.edificios[i].getY(),
                this.ancho, this.alto,
                this.ancho, this.alto)) {

                this.y = Juego.edificios[i].getY() + Juego.edificios[i].getAlto();
            }
        }
    }

    public void moverAbajo() {
        this.y += this.velocidad;

        for (int i = 0; i < Juego.edificios.length; i++) {
            if (Juego.colision(this.x, this.y,
                Juego.edificios[i].getX(), Juego.edificios[i].getY(),
                this.ancho, this.alto,
                this.ancho, this.alto)) {

                this.y = Juego.edificios[i].getY() - this.alto;
            }
        }
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

    public int getVelocidad() {
        return velocidad;
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

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}