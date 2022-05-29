package juego;

import java.awt.*;
import entorno.*;

public class Titan {
    private Rectangle rec;
    private double angulo;
    private double velocidad;
    private int frames = 0;

    private Image titan_quieto = Herramientas.cargarImagen("titan_quieto.png");
    private Image titan_mov_1 = Herramientas.cargarImagen("titan_mov_1.png");
    private Image titan_mov_2 = Herramientas.cargarImagen("titan_mov_2.png");
    private Image imagen_actual = titan_quieto;

    public Titan(int x, int y, double angulo, double velocidad) {
        this.rec = new Rectangle(x, y, 70, 70);
        this.angulo = angulo;
        this.velocidad = velocidad;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.rec.x, this.rec.y, this.rec.width, this.rec.height, 0, Color.RED);
        entorno.dibujarTriangulo(this.rec.x, this.rec.y, this.rec.height, this.rec.width / 2, this.angulo, Color.PINK);

        if (this.frames <= 20) {
            this.imagen_actual = this.titan_mov_2;
        } else if (this.frames <= 40) {
            this.imagen_actual = this.titan_mov_1;
        } else if (this.frames <= 60) {
            this.imagen_actual = this.titan_mov_2;
        } else {
            this.frames = 0;
        }

        this.frames++;
        entorno.dibujarImagen(this.imagen_actual, this.rec.x, this.rec.y, this.angulo - Math.PI / 2, 3.5);
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

    public double getVelocidad() {
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

    public void setVelocidad(double velocidad) {
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