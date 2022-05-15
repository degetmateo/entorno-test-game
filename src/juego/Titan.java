package juego;

import java.awt.*;
// import java.util.concurrent.ThreadLocalRandom;
import entorno.*;

public class Titan {
    private Rectangle rec;
    private int velocidad = 2;
    private double angulo;

    public Titan(int x, int y, double angulo) {
        this.rec = new Rectangle(x, y, 60, 60);
        this.angulo = angulo;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.getX(), this.getY(), this.getAncho(), this.getAlto(), this.angulo, Color.RED);
        entorno.dibujarTriangulo(this.rec.x, this.rec.y, this.rec.height, this.rec.width / 2, this.angulo, Color.PINK);
    }

    public void mover_adelante() {
        this.setX(this.getX() + (int) (Math.cos(this.angulo) * this.velocidad));
        this.setY(this.getY() + (int) (Math.sin(this.angulo) * this.velocidad));
    }

    public void mover_atras() {
        this.setX(this.getX() - (int) (Math.cos(this.angulo) * this.velocidad));
        this.setY(this.getY() - (int) (Math.sin(this.angulo) * this.velocidad));
    }

    public void mirar_mikasa(int mx, int my) {
        double dx = mx - this.getX();
        double dy = my - this.getY();

        this.setAngulo(Math.atan2(dy, dx));
    }

    public void rodear(Rectangle r) {
        double dx = r.x - this.getX();
        double dy = r.y - this.getY();

        this.setAngulo(Math.atan2(dy, dx) + Math.PI / 2);
        
        this.mover_adelante();
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
        return this.velocidad;
    }

    public double getAngulo() {
        return this.angulo;
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

    public void setAngulo(double angulo) {
        this.angulo = angulo;

        if (this.angulo < 0) {
			this.angulo += 2 * Math.PI;
		}

        if (this.angulo > 2 * Math.PI) {
        	this.angulo -= 2 * Math.PI;
        }
    }
}