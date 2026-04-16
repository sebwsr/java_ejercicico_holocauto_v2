package holocaustoH;

import java.util.concurrent.ThreadLocalRandom;

public class Juego {
	public static final int LANZAR_DADO = 1;
	public static final int SALIR_JUEGO = 0;
	static JuegoManager gm = JuegoManager.getInstance();
	
	public static void pintarHabitacion(Habitacion h) {
		System.out.println("========== HABITACIÓN " + (h.getId() + 1) + " ==========");
		
		// Números de columna
		System.out.print("   ");
		for (int col = 0; col < Habitacion.ANCHO; col++) {
			if (col > 9) System.out.print(col % 10);
			else System.out.print(col);
		}
		System.out.println();
		
		// Dibujar mapa
		for (int fil = 0; fil < Habitacion.ALTO; fil++) {
			System.out.print((fil > 9 ? "" : " ") + fil + " ");
			for (int col = 0; col < Habitacion.ANCHO; col++) {
				Posicion posActual = new Posicion(col, fil);
				int objIndex = h.buscarObjeto(posActual);
				
				if (objIndex != -1) {
					ObjetoJuego obj = h.getObjeto(objIndex);
					
					if (obj.getTipoObjeto() == ObjetoJuego.PUERTA_OUT) {
						// Puerta de salida - se oculta con el símbolo de la pared correspondiente
						Jugador jugador = h.getJugadorComoJugador();
						if (jugador != null && jugador.getPiezaHabitacion()) {
							System.out.print('S');  // Mostrar 'S' cuando tiene la pieza
						} else {
							// Ocultar con el símbolo de la pared según la posición
							char paredChar = obtenerCaracterPared(col, fil);
							System.out.print(paredChar);
						}
					} else if (obj.getTipoObjeto() == ObjetoJuego.HADRON) {
						Hadron hadron = (Hadron) obj;
						if (hadron.isVisible()) {
							System.out.print('O');
						} else {
							System.out.print(" ");
						}
					} else {
						System.out.print(obj.getLetraMapa());
					}
				} else if (col == 0 || col == Habitacion.ANCHO - 1) {
					System.out.print("|");
				} else if (fil == 0 || fil == Habitacion.ALTO - 1) {
					System.out.print("=");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println("=========================================");
	}
	//funcion para ocultar la salidas de las habitaciones 
	private static char obtenerCaracterPared(int col, int fil) {
		if (fil == 0 || fil == Habitacion.ALTO - 1) {
			return '=';
		} else {
			return '|';
		}
		
	}
	public static void limitesHab(Posicion posJugador,Posicion  puertaSalidaPos) {
		boolean limiteAlcanzado = false;
		
		if ((posJugador.getPosX() < 1) && !posJugador.esIgual(puertaSalidaPos)) {
			posJugador.setPosX(1);
			limiteAlcanzado = true;
		}
		if ((posJugador.getPosX() >= Habitacion.ANCHO - 1) && !posJugador.esIgual(puertaSalidaPos)) {
			posJugador.setPosX(Habitacion.ANCHO - 2);
			limiteAlcanzado = true;
		}
		if ((posJugador.getPosY() < 1) && !posJugador.esIgual(puertaSalidaPos)) {
			posJugador.setPosY(1);
			limiteAlcanzado = true;
		}
		if ((posJugador.getPosY() >= Habitacion.ALTO - 1) && !posJugador.esIgual(puertaSalidaPos)) {
			posJugador.setPosY(Habitacion.ALTO - 2);
			limiteAlcanzado = true;
		}
		
		if (limiteAlcanzado ) {
			System.out.println(" ¡Has chocado contra una pared! No puedes salir del mapa.");
		}
		
	}
	public static void menureglas() {

		System.out.println("\n╔═══════════════════════════════════════════╗");
		System.out.println("║     BIENVENIDO A HOLOCAUSTO H - HADRON    ║");
		System.out.println("║              EDITION 1.0                  ║");
		System.out.println("╚═══════════════════════════════════════════╝");
		System.out.println("\n OBJETIVO: Supera " + gm.getTotalHabitaciones() + " habitaciones");
		System.out.println(" REGLAS:");
		System.out.println("   • Recoge las piezas (P) para abrir la puerta de salida");
		System.out.println("   • Evita los hadrones (H) o perderás vida");
		System.out.println("   • Los objetos misteriosos (?) pueden tener efectos aleatorios");
		System.out.println("   • Cada hadrón te quita vida según los pasos que hayas movido");
		System.out.println("   • Puedes moverte en cualquier dirección (números negativos para retroceder)");
		System.out.println("\n¡MUCHA SUERTE!\n");
		
	}
	
	public static void pintarMenu() {
		
		System.out.println("========== HOLOCAUSTO H ==========");
		System.out.println("❤️  VIDA: " + gm.getJugadorGlobal().getVida());
		System.out.println("📌 HABITACIÓN: " + (gm.getHabitacionActualNum() + 1) + 
						  " de " + gm.getTotalHabitaciones());
		System.out.println("===================================");
		System.out.println("[1] 🎲 LANZAR DADO DE MOVIMIENTO");
		System.out.println("[0] 🚪 SALIR DEL JUEGO");
		System.out.println("===================================");
	}
	
	public static int lanzarDado(int numCaras) {
		int dado = ThreadLocalRandom.current().nextInt(1, numCaras + 1);
		System.out.println("🎲 [DADO] Has sacado un " + dado);
		return dado;
	}
	
	public static int explotaHadron(int vidaPerdida, Jugador jugador) {
		System.out.println("========== ¡HADRON DETECTADO! ==========");
		System.out.println("💥 ¡¡¡HAS CHOCADO CONTRA UN HADRÓN!!!");
		System.out.println("❤️ Has perdido " + vidaPerdida + " puntos de vida");
		
		int nuevaVida = jugador.getVida() - vidaPerdida;
		jugador.setVida(nuevaVida);
		
		if (nuevaVida <= 0) {
			System.out.println("💀 ¡HAS MUERTO! 💀");
			System.out.println("=========================================");
			return Juego.SALIR_JUEGO;
		} else {
			System.out.println("❤️ Te quedan " + nuevaVida + " puntos de vida");
			System.out.println("=========================================");
			return Juego.LANZAR_DADO;
		}
	}
	
	public static void habitacionSuperada(Habitacion h) {
		System.out.println("========== ¡HABITACIÓN SUPERADA! ==========");
		System.out.println("✨ ¡Has encontrado la salida! ✨");
		System.out.println("=========================================");
	}
	
	public static void juegoCompletado() {
		System.out.println("\n🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉");
		System.out.println("     ¡FELICITACIONES! HAS COMPLETADO EL JUEGO    ");
		System.out.println("🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉🎉\n");
	}
	
	public static void mostrarAyuda() {
		System.out.println("\n📖 AYUDA DEL JUEGO:");
		System.out.println("  J = Tu personaje");
		System.out.println("  O = Hadron (enemigo) - ¡EVÍTALO! o Objeto misterioso (puede ayudar o perjudicar)");
		System.out.println("  P = Pieza (necesaria para abrir la salida)");
		System.out.println("  E = Puerta de entrada");
		System.out.println("  S = Puerta de salida ");
		System.out.println("  pero solo se ve como 'S' cuando tienes la pieza.");
		System.out.println("  ¡Busca la pieza (P) para poder salir!\n");
	}
}