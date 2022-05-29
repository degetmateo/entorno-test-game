package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.*;

public class Mikasa {
    private Rectangle rec;

    private Image mikasa_quieta = Herramientas.cargarImagen("mikasa_quieta.png");
    private Image mikasa_mov_1 = Herramientas.cargarImagen("mikasa_mov_1.png");
    private Image mikasa_mov_2 = Herramientas.cargarImagen("mikasa_mov_2.png");
    
    private Image mikasa_titan_mov_1 = Herramientas.cargarImagen("mikasa_titan_mov_1.png");
    private Image mikasa_titan_mov_2 = Herramientas.cargarImagen("mikasa_titan_mov_2.png");

    private Image imagen_actual = this.mikasa_quieta;
    private Image imagen_actual_especial = this.mikasa_titan_mov_1;

    private int velocidad;
    private String estado = "normal";
    private double angulo = 0;
    private int vidas = 3;

    private int frames = 0;

    public Mikasa(int x, int y, int velocidad) {
        this.rec = new Rectangle(x, y, 70, 70);
        this.velocidad = velocidad;
    }

    // Comprueba el estado de mikasa y dependiendo de cuál sea le cambia el color.
    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.rec.x, this.rec.y, this.rec.width, this.rec.height, 0, Color.WHITE);

        if (this.estado.equals("normal")) {
            entorno.dibujarImagen(this.imagen_actual, this.rec.x, this.rec.y, this.angulo - Math.PI / 2, 3);
            //entorno.dibujarTriangulo(this.rec.x, this.rec.y, this.rec.height, this.rec.width / 2, this.grados, Color.PINK);
        } else if (this.estado.equals("especial")) {
            entorno.dibujarImagen(this.imagen_actual_especial, this.rec.x, this.rec.y, this.angulo - Math.PI / 2, 3);
            //entorno.dibujarTriangulo(this.rec.x, this.rec.y, this.rec.height, this.rec.width / 2, this.grados, Color.ORANGE);
        }
    }

    public void animar() {
        if (this.estado.equals("normal")) {
            if (this.frames <= 20) {
                this.imagen_actual = this.mikasa_mov_2;
            } else if (this.frames <= 40) {
                this.imagen_actual = this.mikasa_mov_1;
            } else if (this.frames <= 60) {
                this.imagen_actual = this.mikasa_mov_2;
            } else {
                this.frames = 0;
            }
        } else {
            if (this.frames <= 20) {
                this.imagen_actual_especial = this.mikasa_titan_mov_2;
            } else if (this.frames <= 40) {
                this.imagen_actual_especial = this.mikasa_titan_mov_1;
            } else if (this.frames <= 60) {
                this.imagen_actual_especial = this.mikasa_titan_mov_2;
            } else {
                this.frames = 0;
            }
        }

        this.frames++;
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

    public int getVidas() {
        return this.vidas;
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

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }
}