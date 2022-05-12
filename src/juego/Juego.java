package juego;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import entorno.*;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private String estado = "inicio";
	private Entorno entorno;
	private Image imgFondo;
	private Image imgInicio;
	private Mikasa mikasa;
	private Edificio[] edificios;
	private Disparo[] disparos;
	private Suero suero;
	private int contador_habilidad = 0;
	private int cooldown_disparo = 0;
	
	public Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Attack on Titan - Grupo 6 - Apellido1 - Apellido2 -Apellido3 - V0.01", 800, 600);
		// Inicializar lo que haga falta para el juego
		// ...
		this.imgFondo = Herramientas.cargarImagen("img-fondo.jpg");
		this.imgInicio = Herramientas.cargarImagen("tapa.jpg");
		this.mikasa = new Mikasa(200, 200, 4);
		this.generar_edificios(4);
		this.generar_suero();

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

		// Se verifica el valor del String estado de la clase juego.
		// Dependiendo de cuál sea el valor, se mostrara una pantalla u otra.
		if (this.estado.equals("inicio")) {
			// Dibujar la pantalla de inicio.
			this.dibujar_inicio();

			// Si se presiona la tecla ENTER se cambia el estado del objeto juego.
			// En consecuencia se cambia la pantalla a la de juego.
			if (entorno.estaPresionada(entorno.TECLA_ENTER)) {
				this.estado = "juego";
			}
		} else if (this.estado.equals("juego")) {
			this.dibujar_fondo();

			for (int i = 0; i < this.edificios.length; i++) {
				this.edificios[i].dibujar(this.entorno);
			}

			this.movimiento_mikasa();
			this.mikasa.dibujar(this.entorno);

			// Verificar si el suero aún existe.
			if (this.suero != null) {
				// Dibujar el suero.
				this.suero.dibujar(entorno);

				// Verificar si hay una colisión con el suero.
				if (this.colision(this.mikasa.getRec(), this.suero.getRec())) {
					// Si Mikasa colisiona con el suero este se elimina y se cambia el varlor de contador_habilidad.
					this.suero = null;
					this.contador_habilidad = 900;
				}
			}

			// Si el contador_habilidad es mayor a cero mikasa gana el estado "especial" y cambia de color.
			// Además, el contador_habilidad irá disminuyendo en cada tick.
			if (this.contador_habilidad > 0) {
				this.contador_habilidad--;
				this.mikasa.setEstado("especial");
			} else {
				this.mikasa.setEstado("normal");
			}

			// if (this.cooldown_disparo == 0 && this.entorno.estaPresionada(this.entorno.TECLA_ESPACIO)) {
			// 	this.disparar();
			// }

			if (this.cooldown_disparo > 0) this.cooldown_disparo--;

			this.trayectoria_disparos();

			if (this.disparos != null) {
				for (int i = 0; i < this.disparos.length; i++) {
					if (this.disparos[i] != null) {
						if (this.disparos[i].getX() > entorno.ancho()) this.disparos[i] = null;
						if (this.disparos[i].getX() < 0) this.disparos[i] = null;
						if (this.disparos[i].getY() > entorno.ancho()) this.disparos[i] = null;
						if (this.disparos[i].getX() < 0) this.disparos[i] = null;
					}
				}
			}
		} else if (this.estado.equals("final")) {
			entorno.cambiarFont("Arial", 32, Color.white);
			entorno.escribirTexto("Has Perdido", 50, 60);

			entorno.cambiarFont("Arial", 28, Color.white);
			entorno.escribirTexto("Presione ENTER para volver a jugar", 50, 100);

			// Si se presiona la tecla ENTER se cambia el estado del objeto juego.
			// En consecuencia se cambia la pantalla a la de juego.
			if (entorno.estaPresionada(entorno.TECLA_ENTER)) {
				this.mikasa.setX(entorno.ancho() / 2);
				this.mikasa.setY(entorno.alto() / 2);
				this.reiniciar_edificios();
				this.reiniciar_suero();

				this.estado = "juego";
			}
		}
	}

	public void dibujar_fondo() {
		this.entorno.dibujarImagen(this.imgFondo, 400, 300, 0, 1);
	}

	public void dibujar_inicio() {
		this.entorno.dibujarImagen(this.imgInicio, 400, 300, 0, 1);
	}

	// Funcion que comprueba si hay una colisión entre dos rectangulos.
	// public boolean colision(Rectangle a, Rectangle b) {
	// 	return (a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y);
	// }

	// public boolean colision(Rectangle a, Rectangle b) {
	// 	return a.intersects(b);
	// }

	//	r.y - r.alto/2 < s.y() + s.alto()/2 &&
	//	r.y + r.alto/2 > s.y() - s.alto()/2 &&
	//	r.x - r.ancho/2 < s.x() + s.ancho()/2 &&
	//	r.x + r.ancho/2 > s.x() - s.ancho()/2;

	public boolean colision(Rectangle a, Rectangle b) {
		return a.x + a.width / 2 > b.x - b.width / 2 &&
			   a.x - a.width / 2 < b.x + b.width / 2 &&
			   a.y + a.height / 2 > b.y - b.height / 2 &&
			   a.y - a.height / 2 < b.y + b.height / 2;
	}

	// Esta funcion genera edificios (obstáculos) en una posición al azar.
	public void generar_edificios(int cantidad) {
		this.edificios = new Edificio[cantidad];

		for (int i = 0; i < this.edificios.length; i++) {
			int ranX = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.ancho() - 99);
			int ranY = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.alto() - 99);

			this.edificios[i] = new Edificio(ranX, ranY, 70, 70);
		}

		// Se comprueba que los edificios no choquen con mikasa al inicio.
		for (int i = 0; i < this.edificios.length; i++) {
			if (this.colision(this.edificios[i].getRec(), this.mikasa.getRec())) {
				generar_edificios(cantidad);
			}
		}

		// Se comprueba que los edificios no se solapen.
		// De lo contrario se vuelve a llamar a la función.
		for (int i = 0; i < this.edificios.length; i++) {
			for (int j = 0; j < this.edificios.length; j++) {
				if (i != j) {
					if (this.colision(this.edificios[i].getRec(), this.edificios[j].getRec())) {
						generar_edificios(cantidad);
					}
				}
			}
		}
	}

	// Esta funcion reinicia los edificios.
	public void reiniciar_edificios() {
		this.generar_edificios(this.edificios.length);
	}
	
	// Esta funcion genera el suero en una posición al azar.
	public void generar_suero() {
		int ranX = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.ancho() - 99);
		int ranY = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.alto() - 99);

		this.suero = new Suero(ranX, ranY);

		// Se comprueba que el suero no se solape con un edificio.
		for (Edificio edificio: this.edificios) {
			if (this.colision(this.suero.getRec(), edificio.getRec())) {
				generar_suero();
			}
		}

		// Se comprueba que el suero no se solape con mikasa.
		if (this.colision(this.suero.getRec(), this.mikasa.getRec())) {
			generar_suero();
		}
	}

	// Esta funcion reinicia el suero.
	public void reiniciar_suero() {
		this.generar_suero();
	}

	// public void disparar() {
	// 	if (this.disparos == null) {
	// 		this.disparos = new Disparo[1];
	// 		this.disparos[0] = new Disparo(this.mikasa.getX(), this.mikasa.getY(), this.mikasa.getDireccion());
	// 	} else {
	// 		Disparo[] disparos_nuevo = new Disparo[this.disparos.length + 1];

	// 		for (int i = 0; i < this.disparos.length; i++) {
	// 			disparos_nuevo[i] = this.disparos[i];
	// 		}

	// 		disparos_nuevo[disparos_nuevo.length - 1] = new Disparo(this.mikasa.getX(), this.mikasa.getY(), this.mikasa.getDireccion());
	// 		this.disparos = disparos_nuevo;
	// 	}

	// 	this.cooldown_disparo = 60;
	// }

	public void trayectoria_disparos() {
		if (this.disparos != null && this.disparos.length > 0) {
			for (Disparo disparo: this.disparos) {
				if (disparo != null) {
					disparo.mover();
					disparo.dibujar(this.entorno);
				}
			}
		}
	}

	public void eliminar_disparo(int disparo) {
		this.disparos[disparo] = null;
	}

	public void movimiento_mikasa() {
		// Comprobar si una tecla de movimiento está presionada y mover a mikasa en consecuencia.
		// Comprobar si mikasa colisiona con un edificio.
		if (this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA) || this.entorno.estaPresionada('w')) {
			this.mikasa.mover_adelante();

			for (Edificio edificio: this.edificios) {
				if (this.colision(this.mikasa.getRec(), edificio.getRec())) {
					this.mikasa.mover_atras();
					break;
				}
			}
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_ABAJO) || this.entorno.estaPresionada('s')) {
			this.mikasa.mover_atras();

			for (Edificio edificio: this.edificios) {
				if (this.colision(this.mikasa.getRec(), edificio.getRec())) {
					this.mikasa.mover_adelante();
					break;
				}
			}
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA) || this.entorno.estaPresionada('a')) {
			this.mikasa.girar(Extras.radianes(-4));
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA) || this.entorno.estaPresionada('d')) {
			this.mikasa.girar(Extras.radianes(4));
		}

		// Comprobar si Mikasa se sale de la pantalla. 
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
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

}
