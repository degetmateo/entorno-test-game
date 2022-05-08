package juego;

import java.awt.Image;
import java.util.Random;
import java.awt.Color;

import entorno.*;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private String estado = "inicio";
	private Entorno entorno;
	private Image imgFondo;
	private Mikasa mikasa;
	public static Edificio[] edificios;
	
	public Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Attack on Titan - Grupo 6 - Apellido1 - Apellido2 -Apellido3 - V0.01", 800, 600);
		this.imgFondo = Herramientas.cargarImagen("img-fondo.jpg");
		// Inicializar lo que haga falta para el juego
		// ...

		this.generar_edificios(4);
		this.mikasa = new Mikasa(200, 200, 4);

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick() {
		// Procesamiento de un instante de tiempo
		// ...

		if (this.estado.equals("inicio")) {
			entorno.cambiarFont("Arial", 32, Color.white);
			entorno.escribirTexto("Attack on Titan", 50, 60);

			entorno.cambiarFont("Arial", 26, Color.white);
			entorno.escribirTexto("The Game", 50, 100);

			entorno.cambiarFont("Arial", 28, Color.white);
			entorno.escribirTexto("Presione ENTER para comenzar", 50, 180);

			if (entorno.estaPresionada(entorno.TECLA_ENTER)) {
				this.estado = "juego";
			}
		} else if (this.estado.equals("juego")) {
			this.dibujar_fondo();
	
			for (int i = 0; i < edificios.length; i++) {
				edificios[i].dibujar(this.entorno);
			}
	
			this.movimiento_mikasa();
			this.mikasa.dibujar(this.entorno);
		} else if (this.estado.equals("final")) {
			entorno.cambiarFont("Arial", 32, Color.white);
			entorno.escribirTexto("Has Perdido", 50, 60);

			entorno.cambiarFont("Arial", 28, Color.white);
			entorno.escribirTexto("Presione ENTER para volver a jugar", 50, 100);

			if (entorno.estaPresionada(entorno.TECLA_ENTER)) {
				this.mikasa.setX(entorno.ancho() / 2);
				this.mikasa.setY(entorno.alto() / 2);
				this.reiniciar_edificios();

				this.estado = "juego";
			}
		}
	}

	public void dibujar_fondo() {
		this.entorno.dibujarImagen(this.imgFondo, 400, 300, 0, 1);
	}

	public static boolean colision(double x1, double y1, double x2, double y2, double w1, double h1, double w2, double h2) {
		if (x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && h1 + y1 > y2) {
			return true;
		} else {
			return false;
		}
	}

	public void generar_edificios(int cantidad) {
		edificios = new Edificio[cantidad];

		for (int i = 0; i < edificios.length; i++) {
			edificios[i] = new Edificio(new Random().nextInt(600), new Random().nextInt(400));
		}
	}

	public void reiniciar_edificios() {
		for (int i = 0; i < edificios.length; i++) {
			edificios[i].setX(new Random().nextInt(600));
			edificios[i].setY(new Random().nextInt(400));
		}
	}

	public void movimiento_mikasa() {
		// Comprobar si Mikasa se salió de la pantalla. 

		if (this.mikasa.getX() < this.mikasa.getAncho() / 2) {
			this.mikasa.setX(this.mikasa.getAncho() / 2);
		}

		if (this.mikasa.getX() > this.entorno.ancho() - this.mikasa.getAncho() / 2) {
			this.mikasa.setX(this.entorno.ancho() - this.mikasa.getAncho() / 2);
		}

		if (this.mikasa.getY() < this.mikasa.getAlto() / 2) {
			this.mikasa.setY(this.mikasa.getAlto() / 2);
		}

		if (this.mikasa.getY() > this.entorno.alto() - this.mikasa.getAlto() / 2) {
			this.mikasa.setY(this.entorno.alto() - this.mikasa.getAlto() / 2);
		}

		// Comprobar si se presionaron las teclas de movimiento, y mover a Mikasa en tal caso.

		if (this.entorno.estaPresionada(entorno.TECLA_ESPACIO)) {
			this.mikasa.setEstado("especial");
		} else {
			this.mikasa.setEstado("normal");
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
			this.mikasa.moverDerecha();
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
			this.mikasa.moverIzquierda();
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA)) {
			this.mikasa.moverArriba();
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_ABAJO)) {
			this.mikasa.moverAbajo();
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

}
