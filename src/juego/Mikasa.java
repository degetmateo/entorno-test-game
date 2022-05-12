package juego;

import entorno.Entorno;
import entorno.Herramientas;

import java.awt.*;

public class Mikasa {
    private Rectangle rec;

    private Image img_arriba = Herramientas.cargarImagen("mikasa-arriba.png");
    private Image img_abajo = Herramientas.cargarImagen("mikasa-abajo.png");
    private Image img_izquierda = Herramientas.cargarImagen("mikasa-izquierda.png");
    private Image img_derecha = Herramientas.cargarImagen("mikasa-derecha.png");

    private Image img_especial = Herramientas.cargarImagen("mikasa-titan.png");

    private Image img = this.img_derecha;

    private int velocidad;
    private String estado = "normal";
    private double angulo = 0;

    public Mikasa(int x, int y, int velocidad) {
        this.rec = new Rectangle(x, y, 70, 70);
        this.velocidad = velocidad;
    }

    // Comprueba el estado de mikasa y dependiendo de cuál sea le cambia el color.
    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.rec.x, this.rec.y, this.rec.width, this.rec.height, 0, Color.WHITE);

        if (this.estado.equals("normal")) {
            // Cambiar la imagen de mikasa dependiendo de su angulo.
            
            // if (grados >= 0 && grados < 90) {
            //     this.img = this.img_derecha;
            // } else if (grados >= 90 && grados < 180) {
            //     this.img = this.img_abajo;
            // } else if (grados >= 180 && grados < 270) {
            //     this.img = this.img_izquierda;
            // } else if (grados >= 270 && grados < 360) {
            //     this.img = this.img_arriba;
            // }

            entorno.dibujarImagen(this.img, this.rec.x, this.rec.y, this.angulo, 2.5);
        } else if (this.estado.equals("especial")) {
            entorno.dibujarImagen(this.img_especial, this.rec.x, this.rec.y, this.angulo, 0.4);
        }
    }

    // Funciones que mueven a Mikasa hacia adelante o hacia atrás.
    public void mover_adelante() {
        this.setX(this.getX() + (int) (Math.cos(this.angulo) * this.velocidad));
        this.setY(this.getY() + (int) (Math.sin(this.angulo) * this.velocidad));
    }

    public void mover_atras() {
        this.setX(this.getX() - (int) (Math.cos(this.angulo) * this.velocidad));
        this.setY(this.getY() - (int) (Math.sin(this.angulo) * this.velocidad));
    }

    public void girar(double modificador) {
        this.setAngulo(this.angulo + modificador);
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

    public String getEstado() {
        return this.estado;
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

        if (this.angulo < 0) {
			this.angulo += 2 * Math.PI;
		}

        if (this.angulo > 2 * Math.PI) {
        	this.angulo -= 2 * Math.PI;
        }
    }
}