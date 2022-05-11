package juego;

import entorno.Entorno;
import entorno.Herramientas;

import java.awt.*;

public class Mikasa {
    private Rectangle rec;
    private Image img = Herramientas.cargarImagen("mikasa-derecha.png");
    private Image imgEspecial = Herramientas.cargarImagen("mikasa-titan.png");
    private int velocidad;
    private String estado = "normal";
    private double angulo = 0;

    public Mikasa(int x, int y, int velocidad) {
        this.rec = new Rectangle(x, y, 70, 70);
        this.imgEspecial = Herramientas.cargarImagen("mikasa-titan.png");
        this.velocidad = velocidad;
    }

    // Comprueba el estado de mikasa y dependiendo de cuál sea le cambia el color.
    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.rec.x, this.rec.y, this.rec.width, this.rec.height, this.getAngulo(), Color.WHITE);

        if (this.estado.equals("normal")) {
            entorno.dibujarImagen(this.img, this.getX(), this.getY(), this.getAngulo(), 3);
        } else if (this.estado.equals("especial")) {
            entorno.dibujarImagen(this.imgEspecial, this.getX(), this.getY(), this.getAngulo(), 0.5);
        }
    }

    // Funciones que mueven a Mikasa a un lado o a otro. Además verifican que no haya una colision con los edificios.

    public void mover_adelante() {
        double cos = Math.cos(this.getAngulo()) * 10;
        double sen = Math.sin(this.getAngulo()) * 10;

        this.setX(this.getX() + cos);
        this.setY(this.getY() + sen);
    }

    public void mover_atras() {
        double cos = Math.cos(this.getAngulo()) * 10;
        double sen = Math.sin(this.getAngulo()) * 10;

        this.setX(this.getX() - cos);
        this.setY(this.getY() - sen);
    }

    public void girar(double modificador) {
        this.setAngulo(this.getAngulo() + modificador);

        if (this.getAngulo() < 0) {
            this.setAngulo(this.getAngulo() + 2 * Math.PI);
        }

        if (this.getAngulo() > 2 * Math.PI) {
            this.setAngulo(this.getAngulo() - 2 * Math.PI);
        }
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

    public double getAngulo() {
        return this.angulo;
    }

    public void setX(double x) {
        this.rec.x = (int) x;
    }

    public void setY(double y) {
        this.rec.y = (int) y;
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

    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }
}