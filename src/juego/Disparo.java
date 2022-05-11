package juego;

import java.awt.*;
import entorno.Entorno;

public class Disparo {
	private Rectangle rec;
    private Image img;
    private String direccion;
    private int velocidad = 3;

    public Disparo(int x, int y, String direccion) {
        this.rec = new Rectangle(x, y, 20, 20);
        this.direccion = direccion;
    }

	public void dibujar(Entorno entorno) {
	    entorno.dibujarRectangulo(this.getX(), this.getY(), this.getAncho(), this.getAlto(), 0, Color.CYAN);
	}
	
    public void mover() {
	    switch(this.getDireccion()) {
            case "arriba": this.setY(this.getY() - this.getVelocidad());
            break;
            case "abajo": this.setY(this.getY() + this.getVelocidad());
            break;
            case "izquierda": this.setX(this.getX() - this.getVelocidad());
            break;
            case "derecha": this.setX(this.getX() + this.getVelocidad());
            break;
        }
    }

    public Rectangle getRec() {
        return this.rec;
    }

    public Image getImg() {
        return this.img;
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

    public String getDireccion() {
        return this.direccion;
    }

    public int getVelocidad() {
        return this.velocidad;
    }

    public void setX(int x) {
        this.rec.x = x;
    }

    public void setY(int y) {
        this.rec.y = y;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
