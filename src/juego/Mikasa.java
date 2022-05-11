package juego;

import entorno.Entorno;
import entorno.Herramientas;

import java.awt.*;

public class Mikasa {
    private Rectangle rec;
    private Image imgArriba;
    private Image imgAbajo;
    private Image imgIzquierda;
    private Image imgDerecha;
    private Image imgEspecial;
    private int velocidad;
    private String estado = "normal";
    private String direccion = "abajo";

    public Mikasa(int x, int y, int velocidad) {
        this.rec = new Rectangle(x, y, 70, 70);
        this.imgArriba = Herramientas.cargarImagen("mikasa-arriba.png");
        this.imgAbajo = Herramientas.cargarImagen("mikasa-abajo.png");
        this.imgIzquierda = Herramientas.cargarImagen("mikasa-izquierda.png");
        this.imgDerecha = Herramientas.cargarImagen("mikasa-derecha.png");
        this.imgEspecial = Herramientas.cargarImagen("mikasa-titan.png");
        this.velocidad = velocidad;
    }

    // Comprueba el estado de mikasa y dependiendo de cuál sea le cambia el color.
    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.rec.x, this.rec.y, this.rec.width, this.rec.height, 0, Color.WHITE);

        if (this.estado.equals("normal")) {
            switch(this.getDireccion()) {
                case "arriba":
                    entorno.dibujarImagen(this.imgArriba, this.getX(), this.getY(), 0, 3);
                break;
                case "abajo":
                    entorno.dibujarImagen(this.imgAbajo, this.getX(), this.getY(), 0, 3);
                break;
                case "izquierda":
                    entorno.dibujarImagen(this.imgIzquierda, this.getX(), this.getY(), 0, 3);
                break;
                case "derecha":
                    entorno.dibujarImagen(this.imgDerecha, this.getX(), this.getY(), 0, 3);
                break;
            }
        } else if (this.estado.equals("especial")) {
            switch(this.getDireccion()) {
                case "arriba":
                    entorno.dibujarImagen(this.imgEspecial, this.getX(), this.getY(), 0, 0.5);
                break;
                case "abajo":
                    entorno.dibujarImagen(this.imgEspecial, this.getX(), this.getY(), 0, 0.5);
                break;
                case "izquierda":
                    entorno.dibujarImagen(this.imgEspecial, this.getX(), this.getY(), 0, 0.5);
                break;
                case "derecha":
                    entorno.dibujarImagen(this.imgEspecial, this.getX(), this.getY(), 0, 0.5);
                break;
            }
        }
    }

    // Funciones que mueven a Mikasa a un lado o a otro. Además verifican que no haya una colision con los edificios.

    public void moverArriba() {
        this.setY(this.getY() - this.getVelocidad());
        this.setDireccion("arriba");
    }

    public void moverAbajo() {
        this.setY(this.getY() + this.getVelocidad());
        this.setDireccion("abajo");
    }

    public void moverIzquierda() {
        this.setX(this.getX() - this.getVelocidad());
        this.setDireccion("izquierda");
    }

    public void moverDerecha() {
        this.setX(this.getX() + this.getVelocidad());
        this.setDireccion("derecha");
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

    public String getDireccion() {
        return this.direccion;
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

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}