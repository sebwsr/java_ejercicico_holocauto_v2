package holocaustoH;

import java.util.concurrent.ThreadLocalRandom;

public class HabitacionFactory {
	private static int contadorPosiciones = 0;
	private static Posicion[] posicionesUsadas = new Posicion[100];
	
	public static Habitacion crearHabitacion(int id, Posicion puertaEntrada, Jugador jugadorReferencia) {
		Habitacion habitacion = new Habitacion(id);
		
		// Configurar puertas
		Posicion puertaSalida = generarPosicionAleatoriaEnOrilla();
		habitacion.setPuertaEntrada(puertaEntrada);
		habitacion.setPuertaSalida(puertaSalida);
		
		// Crear objetos de puerta
		ObjetoJuego puertaInObj = crearObjetoPuerta(puertaEntrada, ObjetoJuego.PUERTA_IN, 'E');
		ObjetoJuego puertaOutObj = crearObjetoPuerta(puertaSalida, ObjetoJuego.PUERTA_OUT, 'S');
		
		// Clonar jugador para esta habitación
		Jugador jugadorHabitacion = new Jugador();
		jugadorHabitacion.setPos(new Posicion(puertaEntrada.getPosX(), puertaEntrada.getPosY()));
		jugadorHabitacion.setLetraMapa('J');
		jugadorHabitacion.setTipoObjeto(ObjetoJuego.JUGADOR);
		jugadorHabitacion.setVida(jugadorReferencia.getVida());
		jugadorHabitacion.setPiezaHabitacion(jugadorReferencia.getPiezaHabitacion());
		
		// Agregar objetos a la habitación
		habitacion.agregarObjeto(jugadorHabitacion);
		habitacion.agregarObjeto(puertaInObj);
		habitacion.agregarObjeto(puertaOutObj);
		
		// Agregar elementos aleatorios (solo dentro del mapa, no en bordes)
		agregarHadrones(habitacion, 1);
		agregarPiezas(habitacion, 1);
		agregarObjetosOcultos(habitacion, 1);
		
		return habitacion;
	}
	
	
	private static Posicion generarPosicionAleatoriaEnOrilla() {
		int intentos = 0;
		while (intentos < 100) {
			// Elegir aleatoriamente qué pared (0=superior, 1=inferior, 2=izquierda, 3=derecha)
			int pared = ThreadLocalRandom.current().nextInt(0, 4);
			int posX, posY;
			
			switch (pared) {
				case 0: // Pared superior (fila 0)
					posX = ThreadLocalRandom.current().nextInt(1, Habitacion.ANCHO - 1);
					posY = 0;
					break;
				case 1: // Pared inferior (fila ALTO-1)
					posX = ThreadLocalRandom.current().nextInt(1, Habitacion.ANCHO - 1);
					posY = Habitacion.ALTO - 1;
					break;
				case 2: // Pared izquierda (columna 0)
					posX = 0;
					posY = ThreadLocalRandom.current().nextInt(1, Habitacion.ALTO - 1);
					break;
				default: // Pared derecha (columna ANCHO-1)
					posX = Habitacion.ANCHO - 1;
					posY = ThreadLocalRandom.current().nextInt(1, Habitacion.ALTO - 1);
					break;
			}
			
			Posicion nuevaPos = new Posicion(posX, posY);
			
			// Verificar que no sea la misma que la puerta de entrada
			boolean esValida = true;
			for (int i = 0; i < contadorPosiciones; i++) {
				if (posicionesUsadas[i] != null && posicionesUsadas[i].esIgual(nuevaPos)) {
					esValida = false;
					break;
				}
			}
			
			if (esValida) {
				posicionesUsadas[contadorPosiciones++] = nuevaPos;
				return nuevaPos;
			}
			intentos++;
		}
		// Fallback: puerta en pared derecha
		return new Posicion(Habitacion.ANCHO - 1, 5);
	}
	
	// Método original para posiciones internas (para hadrones, piezas, objetos)
	private static Posicion generarPosicionAleatoriaInterna() {
		int intentos = 0;
		while (intentos < 100) {
			int posX = ThreadLocalRandom.current().nextInt(2, Habitacion.ANCHO - 2);
			int posY = ThreadLocalRandom.current().nextInt(2, Habitacion.ALTO - 2);
			Posicion nuevaPos = new Posicion(posX, posY);
			
			// Evitar la posición de entrada del jugador (3,0)
			if (nuevaPos.getPosX() == 3 && nuevaPos.getPosY() == 0) {
				intentos++;
				continue;
			}
			
			boolean esUnica = true;
			for (int i = 0; i < contadorPosiciones; i++) {
				if (posicionesUsadas[i] != null && posicionesUsadas[i].esIgual(nuevaPos)) {
					esUnica = false;
					break;
				}
			}
			
			if (esUnica) {
				posicionesUsadas[contadorPosiciones++] = nuevaPos;
				return nuevaPos;
			}
			intentos++;
		}
		return new Posicion(5, 5);
	}
	
	private static ObjetoJuego crearObjetoPuerta(Posicion pos, int tipo, char letra) {
		ObjetoJuego puerta = new ObjetoJuego();
		puerta.setPos(pos);
		puerta.setLetraMapa(letra);
		puerta.setTipoObjeto(tipo);
		return puerta;
	}
	
	private static void agregarHadrones(Habitacion habitacion, int cantidad) {
		for (int i = 0; i < cantidad; i++) {
			Hadron hadron = new Hadron();
			hadron.setPos(generarPosicionAleatoriaInterna());
			hadron.setLetraMapa('O');
			hadron.setTipoObjeto(ObjetoJuego.HADRON);
			habitacion.agregarObjeto(hadron);
		}
	}
	
	private static void agregarPiezas(Habitacion habitacion, int cantidad) {
		for (int i = 0; i < cantidad; i++) {
			ObjetoJuego pieza = new ObjetoJuego();
			pieza.setPos(generarPosicionAleatoriaInterna());
			pieza.setLetraMapa('P');
			pieza.setTipoObjeto(ObjetoJuego.PIEZA);
			habitacion.agregarObjeto(pieza);
		}
	}
	
	private static void agregarObjetosOcultos(Habitacion habitacion, int cantidad) {
		for (int i = 0; i < cantidad; i++) {
			Objeto objeto = new Objeto();
			objeto.setPos(generarPosicionAleatoriaInterna());
			objeto.setLetraMapa('O');
			objeto.setTipoObjeto(ObjetoJuego.OBJETO);
			
    	        // Determinar aleatoriamente si es cura o escudo (50% cada uno) Y el peso del objeto
			objeto.setTipoEspecial( ThreadLocalRandom.current().nextInt(1, 3));
	        objeto.setPeso(ThreadLocalRandom.current().nextInt(1, 4));		
			habitacion.agregarObjeto(objeto);
		}
	}
	
	public static void resetPosiciones() {
		contadorPosiciones = 0;
		posicionesUsadas = new Posicion[100];
	}
}