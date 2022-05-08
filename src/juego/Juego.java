package juego;

import java.awt.Image;

import entorno.*;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Image imgFondo;
	private Mikasa mikasa;
	
	public Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Attack on Titan - Grupo 6 - Apellido1 - Apellido2 -Apellido3 - V0.01", 800, 600);
		this.imgFondo = Herramientas.cargarImagen("img-fondo.jpg");
		// Inicializar lo que haga falta para el juego
		// ...

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
		this.entorno.dibujarImagen(this.imgFondo, 400, 300, 0, 1);
		this.movimiento_mikasa();
		this.mikasa.dibujar(this.entorno);
	}

	public void movimiento_mikasa() {
		// Comprobar si se presionaron las teclas de movimiento, y mover a Mikasa en tal caso.

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

		// Comprobar si Mikasa se salió de la pantalla. 

		if (this.mikasa.x < this.mikasa.ancho / 2) {
			this.mikasa.x = this.mikasa.ancho / 2;
		}

		if (this.mikasa.x > this.entorno.ancho() - this.mikasa.ancho / 2) {
			this.mikasa.x = this.entorno.ancho() - this.mikasa.ancho / 2;
		}

		if (this.mikasa.y < this.mikasa.alto / 2) {
			this.mikasa.y = this.mikasa.alto / 2;
		}

		if (this.mikasa.y > this.entorno.alto() - this.mikasa.alto / 2) {
			this.mikasa.y = this.entorno.alto() - this.mikasa.alto / 2;
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

}
