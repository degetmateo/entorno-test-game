package juego;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import entorno.*;

import javax.sound.sampled.Clip;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private String estado = "inicio";
	private Entorno entorno;
	private int nivel = 1;
	private Image fondo_inicio = Herramientas.cargarImagen("fondo-inicio.jpg");
	private Image fondo_juego = Herramientas.cargarImagen("fondo-juego.jpg");
	private Image fondo_victoria = Herramientas.cargarImagen("fondo-victoria.jpg");
	private Image fondo_derrota = Herramientas.cargarImagen("fondo-derrota.jpg");
	private Image imagen_corazon = Herramientas.cargarImagen("corazon.png");
	private Mikasa mikasa;
	private Edificio[] edificios;
	private Disparo[] disparos;
	private Titan[] titanes;
	private TitanJefe jefe;
	private Suero suero;
	private int titanes_eliminados = 0;
	private int cooldown_disparo = 0;
	private int contador_titan = 180;
	private int cooldown_suero = 1500;

	private Clip sonido_disparo = Sonido.getSound("crash.wav");
	private Clip sonido_suero = Sonido.getSound("suero.wav");
	private Clip sonido_vida = Sonido.getSound("vida.wav");
	private Clip sonido_final = Sonido.getSound("final.wav");
	
	public Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "TP1 - Attack on Titan - Grupo 6 - Deget - Borlenghi - Rosa - Madariaga - V0.01", 800, 600);
		// Inicializar lo que haga falta para el juego
		// ...
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

			this.actualizar_edificios();
			this.actualizar_mikasa();
			this.actualizar_titanes();
			this.actualizar_disparos();

			// Verificar si el suero aún existe.
			if (this.suero != null) {
				// Dibujar el suero.
				this.suero.dibujar(this.entorno);

				// Verificar si hay una colisión con el suero.
				if (this.colision(this.mikasa.getRec(), this.suero.getRec())) {
					this.recoger_suero();
				}
			}

			// Se van a generar hasta 5 titanes en pantalla cada cierto tiempo mientras no exista el jefe.
			if (this.titanes.length < 5 && this.titanes_eliminados < 10 && this.jefe == null) {
				if (this.contador_titan <= 0) {
					this.generar_titan();
					this.contador_titan = 180;
				} else {
					this.contador_titan--;
				}
			}

			// El suero se va a generar cada cierto tiempo mientras el juego no tenga un jefe generado.
			if (this.suero == null && this.jefe == null) {
				this.cooldown_suero--;
				if (this.cooldown_suero <= 0) {
					this.generar_suero();
					this.cooldown_suero = 1500;
					return;
				}
			}

			// Verificar si hay una colision entre un titan y mikasa.
			// Si es así, a mikasa se le resta una vida y el titán se elimina para que no pueda seguir atacando.
			if (this.mikasa.getEstado().equals("normal")) {
				for (int i = 0; i < this.titanes.length; i++) {
					if (this.colision(this.mikasa.getRec(), this.titanes[i].getRec())) {
						Sonido.playSound(this.sonido_vida);
						this.mikasa.setVidas(this.mikasa.getVidas() - 1);
						this.eliminar_titan(i);
		
						if (this.mikasa.getVidas() <= 0) {
							this.estado = "derrota";
						}
					}
				}
				
				// Se verifica si el jefe tocó a mikasa.
				// Si es así, se pierde el juego.
				if (this.jefe != null) {
					if (this.colision(this.mikasa.getRec(), this.jefe.getRec())) {
						this.estado = "derrota";
					}
				}
			}

			// Se verifica si mikasa tocó a un titán en su estado especial.
			// Si es así, el titán se elimina y mikasa vuelve a su estado normal.
			if (this.mikasa.getEstado().equals("especial")) {
				for (int i = 0; i < this.titanes.length; i++) {
					if (this.colision(this.mikasa.getRec(), this.titanes[i].getRec())) {
						this.eliminar_titan(i);
						this.titanes_eliminados += 1;
						this.mikasa.setEstado("normal");
					}
				}
			}

			// Si los titanes eliminados llegan a 10, se genera el Jefe.
			// En la etapa del jefe, los efectos del suero se desactivan y no se genera ningún titán extra.
			if (this.titanes_eliminados >= 10 && this.jefe == null) {
				for (int i = 0; i < this.titanes.length; i++) {
					this.eliminar_titan(i);
				}

				this.suero = null;
				this.mikasa.setEstado("normal");
				this.generar_jefe();
			}

			if (this.jefe != null) {
				this.actualizar_jefe();

				// Se escribe la salud del jefe en pantalla.
				entorno.cambiarFont("Arial", 30, Color.BLACK);
				this.entorno.escribirTexto("Salud Jefe: " + this.jefe.getSalud(), 50, 50);

				// Si la salud del jefe es igual a 0, el estado del juego pasa a "victoria".
				if (this.jefe.getSalud() <= 0) {
					this.estado = "victoria";
				}
			}

			// Escribir puntaje en la pantalla.
			entorno.cambiarFont("Arial", 30, Color.BLACK);
			this.entorno.escribirTexto("ENEMIGOS ELIMINADOS: " + this.titanes_eliminados, 50, 580);
			
			// Dibujar corazones de vidas en pantalla.
			for (int i = 1; i <= this.mikasa.getVidas(); i++) {
				this.entorno.dibujarImagen(this.imagen_corazon, 550 + 60 * i, 550, 0, 0.05);
			}

		} else if (this.estado.equals("derrota")) {
			this.dibujar_derrota();

			// Si se presiona la tecla ENTER se cambia el estado del objeto juego.
			// En consecuencia se cambia la pantalla a la de juego.
			if (entorno.estaPresionada(entorno.TECLA_ENTER)) {
				this.reiniciar();
				this.estado = "juego";
			}
		} else if (this.estado.equals("victoria")) {
			this.dibujar_victoria();

			if (this.entorno.estaPresionada('1')) {
				this.nivel = 1;
				this.reiniciar();
				this.estado = "juego";
			}

			if (this.entorno.estaPresionada('2')) {
				this.nivel = 2;
				this.reiniciar();
				this.estado = "juego";
			}

			if (this.entorno.estaPresionada('3')) {
				this.nivel = 3;
				this.reiniciar();
				this.estado = "juego";
			}

			if (entorno.estaPresionada(entorno.TECLA_ENTER)) {
				this.reiniciar();
				this.estado = "juego";
			}
		}
	}

	// Estas funciones dibujan las distintas pantallas del juego.
	public void dibujar_inicio() {
		this.entorno.dibujarImagen(this.fondo_inicio, 400, 300, 0, 1);
	}

	public void dibujar_fondo() {
		this.entorno.dibujarImagen(this.fondo_juego, 400, 300, 0, 1);
	}

	public void dibujar_victoria() {
		this.entorno.dibujarImagen(this.fondo_victoria, 400, 300, 0, 1);
	}

	public void dibujar_derrota() {
		this.entorno.dibujarImagen(this.fondo_derrota, 400, 300, 0, 1);
	}

	// Funcion que reinicia las variables del juego para que este se pueda volver a jugar.
	public void reiniciar() {
		this.jefe = null;
		this.titanes_eliminados = 0;

		this.mikasa.setX(entorno.ancho() / 2);
		this.mikasa.setY(entorno.alto() / 2);

		this.reiniciar_suero();
		this.reiniciar_titanes();
		this.mikasa.setVidas(3);
	}

	// Funcion que comprueba si hay una colisión entre dos rectangulos.
	private boolean colision(Rectangle a, Rectangle b) {
		return a.x + a.width / 2 > b.x - b.width / 2 &&
			   a.x - a.width / 2 < b.x + b.width / 2 &&
			   a.y + a.height / 2 > b.y - b.height / 2 &&
			   a.y - a.height / 2 < b.y + b.height / 2;
	}

	// Funcion que evita que dos rectángulos se sobrepongan.
	// Si se sobreponen, a uno de los rectangulos se le cambia solamente una de sus coordenadas para que no se detenga por completo.
	private void evitar_colision(Rectangle a, Rectangle b) {
		if (a.x + a.width > b.x && a.x + a.width < b.x + b.width / 2 && a.y < b.y + b.height - 5 && a.y + a.height > b.y + 5) {
			a.x = b.x - a.width;
		}

		if (a.y < b.y + b.height && a.y > b.y + b.height / 2 && a.x + a.width > b.x + 5 && a.x < b.x + b.width - 5) {
			a.y = b.y + a.height;
		}
		
		if (a.x < b.x + b.width && a.x > b.x + b.width / 2 && a.y + a.height > b.y + 5 && a.y < b.y + b.height - 5) {
			a.x = b.x + b.width;
		}
		
		if (a.y + a.height > b.y && a.y + a.height < b.y + b.height / 2 && a.x < b.x + b.width - 5 && a.x + a.width > b.x + 5) {
			a.y = b.y - a.height;
		}
	}

	// Esta funcion genera los obstáculos en unas posiciones definidas.
	public void generar_edificios() {
		this.edificios = new Edificio[4];

		this.edificios[0] = new Edificio(200, 150, 70, 70);
		this.edificios[1] = new Edificio(600, 450, 70, 70);
		this.edificios[2] = new Edificio(200, 450, 70, 70);
		this.edificios[3] = new Edificio(600, 150, 70, 70);
	}
	
	// Esta funcion genera el suero en una posición al azar.
	public void generar_suero() {
		int ranX = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.ancho() - 99);
		int ranY = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.alto() - 99);

		this.suero = new Suero(ranX, ranY);

		// Se comprueba que el suero no se choque con un edificio al generarse.
		for (Edificio edificio: this.edificios) {
			if (this.colision(this.suero.getRec(), edificio.getRec())) {
				generar_suero();
				return;
			}
		}

		// Se comprueba que el suero no se choque con mikasa al generarse.
		if (this.colision(this.suero.getRec(), this.mikasa.getRec())) {
			generar_suero();
			return;
		}
	}

	// Esta funcion reinicia el suero.
	public void reiniciar_suero() {
		this.generar_suero();
	}

	// Esta función hace que mikasa recoja el suero.
	// Se elimina el suero con el que colisinó y se cambia el estado de mikasa.
	public void recoger_suero() {
		this.suero = null;
		this.mikasa.setEstado("especial");
		Sonido.playSound(this.sonido_suero);
	}

	// Choque con edificios MAR
	public boolean choque_titan_edificio(Titan titan) {
		for (Edificio edificio: this.edificios) {
			if (this.colision(titan.getRec(), edificio.getRec())) {
				return true;
			}
		}

		return false;
	}

	// Choque titanes con Mikasa
	public boolean choque_titan_mikasa(Titan titan){
		return this.colision(titan.getRec(), this.mikasa.getRec());
	}
	
	// Interseccion de titanes
	public boolean choque_titan_titan(Titan titan, int indice) {
		for (int i = 0; i < this.titanes.length; i++) {
			if (i != indice && this.titanes[i] != null) {
				if (this.colision(titan.getRec(), this.titanes[i].getRec())) {
					return true;
				}
			}
		}

		return false;
	}
	
	// Funcion que genera una cierta cantidad de titanes en una posición al azar.
	public void generar_titanes(int cantidad) {
		this.titanes = new Titan[cantidad];

		for (int i = 0; i < this.titanes.length; i++) {
			int ranX = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.ancho() - 99);
			int ranY = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.alto() - 99);

			double vel = 2;
			switch (this.nivel) {
				case 1: vel = 2; break;
				case 2: vel = 2.5; break;
				case 3: vel = 3; break;
			}

			this.titanes[i] = new Titan(ranX, ranY, this.mikasa.getAngulo() + 180, vel);
			
			// While que comprueba que los titanes no choquen entre ellos, con los edificios o mikasa al ser generados.
			while (
				this.choque_titan_edificio(this.titanes[i]) ||
				this.choque_titan_mikasa(this.titanes[i]) ||
				this.choque_titan_titan(this.titanes[i], i)
			) {
				ranX = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.ancho() - 99);
				ranY = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.alto() - 99);
				
				this.titanes[i] = new Titan(ranX, ranY, this.mikasa.getAngulo() + 180, vel);
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

		double vel = 2;
		switch (this.nivel) {
			case 1: vel = 2; break;
			case 2: vel = 2.5; break;
			case 3: vel = 3; break;
		}

		titanes_nuevo[this.titanes.length] = new Titan(ranX, ranY, this.mikasa.getAngulo() + 180, vel);

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

		for (Titan titan: this.titanes) {
			if (this.colision(titanes_nuevo[this.titanes.length].getRec(), titan.getRec())) {
				generar_titan();
				return;
			}
		}

		this.titanes = titanes_nuevo;
	}

	// Esta funcion reinicia los titanes.
	public void reiniciar_titanes() {
		this.generar_titanes(4);
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

	public void actualizar_disparos() {
		// Se comprueba si se presiona la tecla espacio y si terminó el enfriamiento del disparo.
		if (this.cooldown_disparo == 0 && this.entorno.estaPresionada(this.entorno.TECLA_ESPACIO)) {
			this.disparar();
		}

		// Actualiza el enfriamiento del disparo.
		if (this.cooldown_disparo > 0) this.cooldown_disparo--;

		// Actualiza la trayectoria de los disparos.
		if (this.disparos != null) {
			this.trayectoria_disparos();

			for (Disparo disparo: this.disparos) {
				disparo.mover();
				disparo.dibujar(this.entorno);
			}
		}
	}

	// Funcion que genera un nuevo disparo que mira hacia el angulo en el está mirando mikasa.
	public void disparar() {
		Sonido.playSound(this.sonido_disparo);
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
					this.titanes_eliminados += 1;
					return;
				}
			}

			if (this.jefe != null) {
				if (this.colision(this.disparos[i].getRec(), this.jefe.getRec())) {
					this.eliminar_disparo(i);
					this.jefe.setSalud(this.jefe.getSalud() - 1);
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

	// Funcion que genera al jefe final en una posición al azar.
	// Verifica que no se choque con nada al generarse.
	public void generar_jefe() {
		int ranX = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.ancho() - 99);
		int ranY = (int) ThreadLocalRandom.current().nextInt(100, this.entorno.alto() - 99);

		this.jefe = new TitanJefe(ranX, ranY);

		for (Edificio edificio: this.edificios) {
			if (this.colision(this.jefe.getRec(), edificio.getRec())) {
				this.generar_jefe();
				return;
			}
		}

		for (Titan titan: this.titanes) {
			if (this.colision(this.jefe.getRec(), titan.getRec())) {
				this.generar_jefe();
				return;
			}
		}

		if (this.colision(this.jefe.getRec(), this.mikasa.getRec())) {
			this.generar_jefe();
			return;
		}

		Sonido.playSound(this.sonido_final);
	}

	public void actualizar_jefe() {
		// Hacer que el jefe se mueva hacia mikasa.
		this.jefe.mirar_mikasa(this.mikasa.getX(), this.mikasa.getY());
		this.jefe.mover_adelante();

		// Evitar que el jefe choque con edificios.
		for (Edificio edificio: this.edificios) {
			if (this.colision(this.jefe.getRec(), edificio.getRec())) {
				this.jefe.mover_atras();

				double dx = edificio.getX() - this.jefe.getX();
				double dy = edificio.getY() - this.jefe.getY();

				this.jefe.setAngulo(Math.atan2(dy, dx) + Math.PI / 2);
				this.jefe.mover_adelante();

				this.evitar_colision(this.jefe.getRec(), edificio.getRec());
			}
		}

		// Evitar que el jefe se salga de la pantalla.
		if (this.jefe.getX() < this.jefe.getAncho() / 2) {
			this.jefe.setX(this.jefe.getAncho() / 2);
		}

		if (this.jefe.getX() > this.entorno.ancho() - this.jefe.getAncho() / 2) {
			this.jefe.setX(this.entorno.ancho() - this.jefe.getAncho() / 2);
		}

		if (this.jefe.getY() < this.jefe.getAlto() / 2) {
			this.jefe.setY(this.jefe.getAlto() / 2);
		}

		if (this.jefe.getY() > this.entorno.alto() - this.jefe.getAlto() / 2) {
			this.jefe.setY(this.entorno.alto() - this.jefe.getAlto() / 2);
		}

		this.jefe.dibujar(this.entorno);
	}

	public void actualizar_edificios() {
		for (int i = 0; i < this.edificios.length; i++) {
			this.edificios[i].dibujar(this.entorno);
		}
	}

	// Funcion que actualiza a mikasa.
	public void actualizar_mikasa() {
		// Comprobar si una tecla de movimiento está presionada y mover a mikasa en consecuencia.
		if (this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA) || this.entorno.estaPresionada('w')) {
			this.mikasa.mover_adelante();
			this.mikasa.animar();
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_ABAJO) || this.entorno.estaPresionada('s')) {
			this.mikasa.mover_atras();
			this.mikasa.animar();
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA) || this.entorno.estaPresionada('a')) {
			this.mikasa.girar(-0.1);
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA) || this.entorno.estaPresionada('d')) {
			this.mikasa.girar(0.1);
		}

		// Evitar que mikasa colisione con algun edificio.
		for (Edificio edificio: this.edificios) {
			if (this.colision(this.mikasa.getRec(), edificio.getRec())) {
				this.evitar_colision(this.mikasa.getRec(), edificio.getRec());
			}
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

		this.mikasa.dibujar(this.entorno);
	}
	
	// Funcion que actualiza a todos los titanes.
	public void actualizar_titanes() {
		for (int i = 0; i < this.titanes.length; i++) {
			this.titanes[i].mirar_mikasa(this.mikasa.getX(), this.mikasa.getY());
			this.titanes[i].mover_adelante();

			// Hacer que los titanes esquiven los edificios.
			for (Edificio edificio: this.edificios) {
				if (this.colision(this.titanes[i].getRec(), edificio.getRec())) {
					this.titanes[i].mover_atras();

					double dx = edificio.getX() - this.titanes[i].getX();
					double dy = edificio.getY() - this.titanes[i].getY();

					this.titanes[i].setAngulo(Math.atan2(dy, dx) + Math.PI / 2);
					this.titanes[i].mover_adelante();

					this.evitar_colision(this.titanes[i].getRec(), edificio.getRec());
				}
			}

			// Verificar que los titanes no se choquen entre ellos.
			for (int j = 0; j < this.titanes.length; j++) {
				if (i != j && this.colision(this.titanes[i].getRec(), this.titanes[j].getRec())) {
					this.evitar_colision(this.titanes[i].getRec(), this.titanes[j].getRec());
				}
			}

			// Verificar que los titanes no se choquen con el jefe.
			if (this.jefe != null) {
				if (this.colision(this.titanes[i].getRec(), this.jefe.getRec())) {
					this.evitar_colision(this.titanes[i].getRec(), this.jefe.getRec());
				}
			}

			// Verificar que los titanes no se salgan de la pantalla.
			if (this.titanes[i].getX() < this.titanes[i].getAncho() / 2) {
				this.titanes[i].setX(this.titanes[i].getAncho() / 2);
			}
	
			if (this.titanes[i].getX() > this.entorno.ancho() - this.titanes[i].getAncho() / 2) {
				this.titanes[i].setX(this.entorno.ancho() - this.titanes[i].getAncho() / 2);
			}
	
			if (this.titanes[i].getY() < this.titanes[i].getAlto() / 2) {
				this.titanes[i].setY(this.titanes[i].getAlto() / 2);
			}
	
			if (this.titanes[i].getY() > this.entorno.alto() - this.titanes[i].getAlto() / 2) {
				this.titanes[i].setY(this.entorno.alto() - this.titanes[i].getAlto() / 2);
			}

			this.titanes[i].dibujar(this.entorno);
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}