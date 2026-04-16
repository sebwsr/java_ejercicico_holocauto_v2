package holocaustoH;

import java.util.Scanner;

public class Principal {
	
	public static int pregunta(String p) {
	
		Scanner in = new Scanner(System.in);
		System.out.print(0);
		int respueta= in.nextInt();
		return respueta;
	}

	
	public static void main(String[] args) {
		
		JuegoManager gameManager = JuegoManager.getInstance();
		
		// INICIALIZAR JUEGO CON 4 HABITACIONES
		gameManager.inicializarJuego(4);
		
		Juego.menureglas();
		
		Juego.mostrarAyuda();
		
		int accionJuego = -1;
		
		while (gameManager.isJuegoActivo()) {
			Habitacion habitacionActual = gameManager.getHabitacionActual();
			Posicion puertaSalidaPos = habitacionActual.getPuertaSalida();
			Jugador jugadorGlobal = gameManager.getJugadorGlobal();
			Personaje jugador = habitacionActual.getJugador();
			Posicion posJugador = jugador.getPos();
			int objetoEncontrado = -1;
			int numMov = 0;
			int movX = 0, movY = 0;
			
			// Pintar estado actual
			Juego.pintarHabitacion(habitacionActual);
			Juego.pintarMenu();
			
			accionJuego = pregunta( " Selecciona una opción 0-1: ");
			
			if (accionJuego == Juego.LANZAR_DADO) {
				numMov = Juego.lanzarDado(10);
				
				// Guardar posición anterior para mostrarla en la consola 
				Posicion posAnterior = new Posicion(jugador.getPos().getPosX(), jugador.getPos().getPosY());
				
				System.out.println(" Tienes " + numMov + " pasos para moverte.");
				System.out.println("  Usa números negativos para retroceder.");
				
				// Movimiento en X (horizontal) - Permitir valores negativos
				movX = pregunta("¿Cuántas columnas mover? (-" + numMov + " a +" + numMov + "): ");
				
				if (Math.abs(movX) > numMov) {
					System.out.println("⚠️ No tienes suficientes pasos. Movimiento ajustado a " + numMov);
					movX = (movX > 0) ? numMov : -numMov;
				}
				jugador.movX(movX);
				numMov -= movX;
				
				// Movimiento en Y (vertical)
				if (numMov > 0) {
					movY =pregunta("⬆️ ⬇️  ¿Cuántas filas mover? (-" + numMov + " a +" + numMov + "): ");
					
					if (Math.abs(movY) > numMov) {
						System.out.println("⚠️ No tienes suficientes pasos. Movimiento ajustado a " + numMov);
						movY = (movY > 0) ? numMov : -numMov;
					}
					jugador.movY(movY);
					numMov -= movY;
				}
				
				// Validar límites de la habitación
			
				Juego.limitesHab(posJugador,puertaSalidaPos);
				
				int distanciaRecorrida = movX + movY;
				
				System.out.println("\n Movimiento: X=" + movX + ", Y=" + movY);
				System.out.println(" Posición anterior: (" + posAnterior.getPosX() + "," + posAnterior.getPosY() + ")");
				System.out.println(" Nueva posición: (" + posJugador.getPosX() + "," + posJugador.getPosY() + ")");
			
				// 🔴 IMPORTANTE: Verificar primero si hay puerta de salida (antes que otros objetos)
				
				
				if (puertaSalidaPos != null && posJugador.esIgual(puertaSalidaPos)) {
					System.out.println(" ¡Estás en la puerta de salida!");
					if (jugadorGlobal.getPiezaHabitacion()) {
						Juego.habitacionSuperada(habitacionActual);
						gameManager.siguienteHabitacion();
						if (!gameManager.isJuegoActivo()) {
							Juego.juegoCompletado();
						}
						continue; // Saltar al siguiente ciclo
					} else {
						System.out.println(" La puerta está cerrada. Necesitas encontrar la pieza (P)");
					}
				}
				
				// Verificar colisiones con otros objetos
				objetoEncontrado = habitacionActual.buscarObjetoSinJugador(posJugador);
				
				if (objetoEncontrado != -1) {
					ObjetoJuego objeto = habitacionActual.getObjeto(objetoEncontrado);
					
					System.out.println(" ¡Has encontrado un objeto en la casilla!");
					
					switch (objeto.getTipoObjeto()) {
						case ObjetoJuego.HADRON:
							System.out.println(" TIPO: HADRÓN (ENEMIGO)");
							int vidaPerdida = Juego.lanzarDado(2) * distanciaRecorrida;
							if (vidaPerdida < 1) vidaPerdida = 1;
							accionJuego = Juego.explotaHadron(vidaPerdida, jugadorGlobal);
							
							habitacionActual.eliminarObjetoPorIndice(objetoEncontrado);
							System.out.println(" El hadrón ha sido eliminado después del choque.");
							
							if (accionJuego == Juego.SALIR_JUEGO) {
								gameManager.terminarJuego();
							}
							break;
							
						case ObjetoJuego.PIEZA:
							System.out.println(" TIPO: PIEZA");
							System.out.println(" ¡Has encontrado una pieza! Ahora puedes salir de la habitación");
							jugadorGlobal.setPiezaHabitacion(true);
							Jugador jugadorHab = habitacionActual.getJugadorComoJugador();
							if (jugadorHab != null) {
								jugadorHab.setPiezaHabitacion(true);
							}
							System.out.println("=========================================");
							habitacionActual.eliminarObjetoPorIndice(objetoEncontrado);
							break;
							
						case ObjetoJuego.OBJETO:
					   //aqui va a ocupar una nueva clase llamada mochila que para guardar estos ojetos que van hacer 2 una cura te cura un 30% de la vida  o un escudo te salva de morrir por en hadron  
						
							break;
					}
				} else {
					System.out.println(" No hay objetos en la nueva posición.");
				}
				
				// Verificar si el jugador murió
				if (gameManager.getJugadorGlobal().getVida() <= 0) {
					System.out.println("\n GAME OVER - Has muerto 💀");
					System.out.println("No has podido completar las " + gameManager.getTotalHabitaciones() + " habitaciones");
	
					break;
				}
				
				
			} else if (accionJuego == Juego.SALIR_JUEGO) {
				System.out.println("\n Gracias por jugar. ¡Hasta pronto!");
				break;
			} else {
				System.out.println("\n Opción no válida. Intenta de nuevo.\n");
			}
		}
		
		
	}
	
}