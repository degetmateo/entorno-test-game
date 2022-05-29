package juego;

import entorno.*;
import java.awt.*;

public class TitanJefe {
    private Rectangle rec;
    private double angulo = 0;
    private int velocidad = 2;
    private int salud = 10;

    public TitanJefe(int x, int y) {
        this.rec = new Rectangle(x, y, 70, 70);
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.rec.x, this.rec.y, this.rec.width, this.rec.height, 0, Color.RED);
        entorno.dibujarTriangulo(this.rec.x, this.rec.y, this.rec.height, this.rec.width / 2, this.angulo, Color.WHITE);
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

    // public void rodear(Rectangle r) {
    //     double dx = r.x - this.getX();
    //     double dy = r.y - this.getY();

    //     this.setAngulo(Math.atan2(dy, dx) + 90);
    //     this.mover_adelante();
    // }

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

    public double getAngulo() {
        return this.angulo;
    }

    public int getSalud() {
        return this.salud;
    }

    public void setX(int x) {
        this.rec.x = x;
    }

    public void setY(int y) {
        this.rec.y = y;
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

    public void setSalud(int salud) {
        this.salud = salud;
    }
}