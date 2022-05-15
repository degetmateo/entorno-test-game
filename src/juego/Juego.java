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
	private Titan[] titanes;
	private Suero suero;
	private int contador_habilidad = 0;
	private int cooldown_disparo = 0;
	private int contador_titan = 300;
	
	public Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Attack on Titan - Grupo 6 - Apellido1 - Apellido2 -Apellido3 - V0.01", 800, 600);
		// Inicializar lo que haga falta para el juego
		// ...
		this.imgFondo = Herramientas.cargarImagen("img-fondo.jpg");
		this.imgInicio = Herramientas.cargarImagen("tapa.jpg");
		this.mikasa = new Mikasa(entorno.ancho() / 2, entorno.alto() / 2, 4);
		this.generar_edificios();
		this.generar_suero();
		this.generar_titanes(4);

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
				this.suero.dibujar(this.entorno);

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

			// Se comprueba si se presiona la tecla espacio y si terminó el enfriamiento del disparo.
			if (this.cooldown_disparo == 0 && this.entorno.estaPresionada(this.entorno.TECLA_ESPACIO)) {
				this.disparar();
			}

			// Actualiza el enfriamiento del disparo.
			if (this.cooldown_disparo > 0) this.cooldown_disparo--;

			if (this.disparos != null) {
				this.trayectoria_disparos();

				for (Disparo disparo: this.disparos) {
					disparo.mover();
					disparo.dibujar(this.entorno);
				}
			}

			for (int i = 0; i < this.titanes.length; i++) {
				this.titanes[i].mirar_mikasa(this.mikasa.getX(), this.mikasa.getY());
				this.titanes[i].mover_adelante();

				for (Edificio edificio: this.edificios) {
					if (this.colision(this.titanes[i].getRec(), edificio.getRec())) {
						this.titanes[i].mover_atras();
						this.titanes[i].rodear(edificio.getRec());
						break;
					}
				}

				for (int j = 0; j < this.titanes.length; j++) {
					if (i != j && this.colision(this.titanes[i].getRec(), this.titanes[j].getRec())) {
						this.titanes[i].mover_atras();
						break;
					}
				}

				this.titanes[i].dibujar(this.entorno);
			}

			if (this.contador_titan <= 0) {
				this.generar_titan();
				this.contador_titan = 300;
			} else {
				this.contador_titan--;
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
				// this.reiniciar_edificios();
				this.reiniciar_suero();
				this.reiniciar_titanes();

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

	// Funcion que comprueba si hay una colisión entre dos rectangulos.
	public boolean colision(Rectangle a, Rectangle b) {
		return a.x + a.width / 2 > b.x - b.width / 2 &&
			   a.x - a.width / 2 < b.x + b.width / 2 &&
			   a.y + a.height / 2 > b.y - b.height / 2 &&
			   a.y - a.height / 2 < b.y + b.height / 2;
	}

	// Esta funcion genera edificios (obstáculos) en una posición al azar.
	public void generar_edificios() {
		this.edificios = new Edificio[4];

		this.edificios[0] = new Edificio(200, 150, 70, 70);
		this.edificios[1] = new Edificio(600, 450, 70, 70);
		this.edificios[2] = new Edificio(200, 450, 70, 70);
		this.edificios[3] = new Edificio(600, 150, 70, 70);

		// for (int i = 0; i < this.edificios.length; i++) {
		// 	int ranX = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.ancho() - 99);
		// 	int ranY = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.alto() - 99);

		// 	this.edificios[i] = new Edificio(ranX, ranY, 70, 70);
		// }

		// Se comprueba que los edificios no choquen con mikasa al inicio.
		// for (int i = 0; i < this.edificios.length; i++) {
		// 	if (this.colision(this.edificios[i].getRec(), this.mikasa.getRec())) {
		// 		generar_edificios(cantidad);
		// 		return;
		// 	}
		// }

		// Se comprueba que los edificios no se solapen.
		// De lo contrario se vuelve a llamar a la función.
		// for (int i = 0; i < this.edificios.length; i++) {
		// 	for (int j = 0; j < this.edificios.length; j++) {
		// 		if (i != j) {
		// 			if (this.colision(this.edificios[i].getRec(), this.edificios[j].getRec())) {
		// 				generar_edificios(cantidad);
		// 				return;
		// 			}
		// 		}
		// 	}
		// }
	}

	// Esta funcion reinicia los edificios.
	// public void reiniciar_edificios() {
	// 	this.generar_edificios(this.edificios.length);
	// }
	
	// Esta funcion genera el suero en una posición al azar.
	public void generar_suero() {
		int ranX = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.ancho() - 99);
		int ranY = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.alto() - 99);

		this.suero = new Suero(ranX, ranY);

		// Se comprueba que el suero no se solape con un edificio.
		for (Edificio edificio: this.edificios) {
			if (this.colision(this.suero.getRec(), edificio.getRec())) {
				generar_suero();
				return;
			}
		}

		// Se comprueba que el suero no se solape con mikasa.
		if (this.colision(this.suero.getRec(), this.mikasa.getRec())) {
			generar_suero();
			return;
		}
	}

	// Esta funcion reinicia el suero.
	public void reiniciar_suero() {
		this.generar_suero();
	}

	public void generar_titanes(int cantidad) {
		this.titanes = new Titan[cantidad];

		for (int i = 0; i < this.titanes.length; i++) {
			int ranX = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.ancho() - 99);
			int ranY = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.alto() - 99);

			this.titanes[i] = new Titan(ranX, ranY, this.mikasa.getAngulo() + 180);
		}

		// Se comprueba que los titanes no choquen con mikasa al inicio.
		for (int i = 0; i < this.titanes.length; i++) {
			if (this.colision(this.titanes[i].getRec(), this.mikasa.getRec())) {
				generar_titanes(cantidad);
				return;
			}
		}

		// Se comprueba que los titanes no choquen con los edificios.
		for (int i = 0; i < this.titanes.length; i++) {
			for (Edificio edificio: this.edificios) {
				if (this.colision(this.titanes[i].getRec(), edificio.getRec())) {
					generar_titanes(cantidad);
					return;
				}
			}
		}

		// Se comprueba que los titanes no se solapen.
		// De lo contrario se vuelve a llamar a la función.
		for (int i = 0; i < this.titanes.length; i++) {
			for (int j = 0; j < this.titanes.length; j++) {
				if (i != j) {
					if (this.colision(this.titanes[i].getRec(), this.titanes[j].getRec())) {
						generar_titanes(cantidad);
						return;
					}
				}
			}
		}
	}

	// Esta funcion genera un nuevo titán.
	public void generar_titan() {
		Titan[] titanes_nuevo = new Titan[this.titanes.length + 1];

		for (int i = 0; i < this.titanes.length; i++) {
			titanes_nuevo[i] = this.titanes[i];
		}

		int ranX = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.ancho() - 99);
		int ranY = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.alto() - 99);

		titanes_nuevo[this.titanes.length] = new Titan(ranX, ranY, this.mikasa.getAngulo() + 180);

		if (this.colision(titanes_nuevo[this.titanes.length].getRec(), this.mikasa.getRec())) {
			generar_titan();
			return;
		}

		for (Edificio edificio: this.edificios) {
			if (this.colision(titanes_nuevo[this.titanes.length].getRec(), edificio.getRec())) {
				generar_titan();
				return;
			}
		}

		this.titanes = titanes_nuevo;
	}

	// Esta funcion reinicia los titanes.
	public void reiniciar_titanes() {
		this.generar_titanes(this.titanes.length);
	}

	// Esta funcion elimina un titán en la lista de titanes (this.titanes).
	// Para ello recibe la posición en la que se encuentra, y crea una nueva lista donde ese titán no existe.
	public void eliminar_titan(int pos) {
		Titan[] titanes_nuevo = new Titan[this.titanes.length - 1];

		for (int i = 0; i < pos; i++) {
			titanes_nuevo[i] = this.titanes[i];
		}

		for (int i = pos; i < this.titanes.length - 1; i++) {
			titanes_nuevo[i] = this.titanes[i + 1];
		}

		this.titanes = titanes_nuevo;
	}

	// Funcion que genera un nuevo disparo que mira hacia el angulo en el está mirando mikasa.
	public void disparar() {
		// Primero se comprueba si la lista de disparos es null.
		// Si es null se le asigna el valor de una lista y se crea el primer Disparo.
		if (this.disparos == null) {
			this.disparos = new Disparo[1];
			this.disparos[0] = new Disparo(this.mikasa.getX(), this.mikasa.getY(), this.mikasa.getAngulo());
		} else {
			// Si ya está creada la lista de disparos, se crea una nueva lista con un tamaño más grande.
			// De esta forma, podemos insertar un nuevo disparo.
			Disparo[] disparos_nuevo = new Disparo[this.disparos.length + 1];

			for (int i = 0; i < this.disparos.length; i++) {
				disparos_nuevo[i] = this.disparos[i];
			}

			disparos_nuevo[disparos_nuevo.length - 1] = new Disparo(this.mikasa.getX(), this.mikasa.getY(), this.mikasa.getAngulo());
			this.disparos = disparos_nuevo;
		}

		// Le asignamos un valor al enfriamiento del disparo para que no se puedan disparar más de una vez por segundo.
		this.cooldown_disparo = 60;
	}

	// Funcion que actualiza los disparos y los va moviendo hacia angulo con el que se crearon.
	// Además, si se salen de la ventana o chocan con algun edificio los elimina.
	public void trayectoria_disparos() {
		for (int i = 0; i < this.disparos.length; i++) {
			if (this.disparos[i].getX() > entorno.ancho()) {
				this.eliminar_disparo(i);
				return;
			}

			if (this.disparos[i].getX() < 0) {
				this.eliminar_disparo(i);
				return;
			}

			if (this.disparos[i].getY() > entorno.ancho()) {
				this.eliminar_disparo(i);
				return;
			}

			if (this.disparos[i].getX() < 0) {
				this.eliminar_disparo(i);
				return;
			}

			for (int e = 0; e < this.edificios.length; e++) {
				if (this.colision(this.disparos[i].getRec(), this.edificios[e].getRec())) {
					this.eliminar_disparo(i);
					return;
				}
			}

			for (int h = 0; h < this.titanes.length; h++) {
				if (this.colision(this.disparos[i].getRec(), this.titanes[h].getRec())) {
					this.eliminar_disparo(i);
					this.eliminar_titan(h);
					return;
				}
			}
		}
	}

	// Esta funcion elimina un disparo en la lista de disparos (this.disparos).
	// Para ello recibe la posición en la que se encuentra, y crea una nueva lista donde ese disparo no existe.
	public void eliminar_disparo(int pos) {
		Disparo[] disparos_nuevo = new Disparo[this.disparos.length - 1];

		for (int i = 0; i < pos; i++) {
			disparos_nuevo[i] = this.disparos[i];
		}

		for (int i = pos; i < this.disparos.length - 1; i++) {
			disparos_nuevo[i] = this.disparos[i + 1];
		}

		this.disparos = disparos_nuevo;
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
			this.mikasa.girar(Extras.radianes(-5));
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA) || this.entorno.estaPresionada('d')) {
			this.mikasa.girar(Extras.radianes(5));
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
