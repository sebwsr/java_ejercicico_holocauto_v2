package holocaustoH;

import java.util.Scanner;

public class Principal {
    
    public static int pregunta(String p) {
        Scanner in = new Scanner(System.in);
        System.out.print(p);
        int respuesta = in.nextInt();
        return respuesta;
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
            Mochila mochila = gameManager.getMochila();
            int objetoEncontrado = -1;
            int numMov = 0;
            int movX = 0, movY = 0;
            
            // Pintar estado actual
            Juego.pintarHabitacion(habitacionActual);
            Juego.pintarMenu();
            mochila.mostrarMochila();      
            accionJuego = pregunta(" Selecciona una opción (0-2): ");
            
            // Opción para usar objeto de la mochila
            if (accionJuego == 2) {
                if (mochila.estaVacia()) {
                    System.out.println(" No tienes objetos en la mochila.");
                } else {
                    mochila.mostrarMochila();
                    int indice = pregunta("¿Qué objeto quieres usar? (número): ");
                    Objeto objeto = mochila.usarObjeto(indice);
                    if (objeto != null) {
                        objeto.aplicarEfecto(jugadorGlobal);
                        // Actualizar vida en la habitación actual
                        Jugador jugadorHab = habitacionActual.getJugadorComoJugador();
                        if (jugadorHab != null) {
                            jugadorHab.setVida(jugadorGlobal.getVida());
                        }
                    }
                }
                continue;
            }
            
            if (accionJuego == Juego.LANZAR_DADO) {
                numMov = Juego.lanzarDado(10);
                
                // Guardar posición anterior
                Posicion posAnterior = new Posicion(jugador.getPos().getPosX(), jugador.getPos().getPosY());
                
                System.out.println("   Tienes " + numMov + " pasos para moverte.");
                System.out.println("   Usa números negativos para retroceder.");
                
                // Movimiento en X
                movX = pregunta("  ¿Cuántas columnas mover? (-" + numMov + " a +" + numMov + "): ");
                
                if (Math.abs(movX) > numMov) {
                    System.out.println(" No tienes suficientes pasos. Movimiento ajustado a " + numMov);
                    movX = (movX > 0) ? numMov : -numMov;
                }
                jugador.movX(movX);
                numMov -= Math.abs(movX);
                
                // Movimiento en Y
                if (numMov > 0) {
                    movY = pregunta("  ¿Cuántas filas mover? (-" + numMov + " a +" + numMov + "): ");
                    
                    if (Math.abs(movY) > numMov) {
                        System.out.println(" No tienes suficientes pasos. Movimiento ajustado a " + numMov);
                        movY = (movY > 0) ? numMov : -numMov;
                    }
                    jugador.movY(movY);
                    numMov -= Math.abs(movY);
                }
                
                // Validar límites
                Juego.limitesHab(posJugador, puertaSalidaPos);
                
                int distanciaRecorrida = Math.abs(movX) + Math.abs(movY);
                
                System.out.println("\n Movimiento: X=" + movX + ", Y=" + movY);
                System.out.println(" Posición anterior: (" + posAnterior.getPosX() + "," + posAnterior.getPosY() + ")");
                System.out.println(" Nueva posición: (" + posJugador.getPosX() + "," + posJugador.getPosY() + ")");
                
                // Verificar puerta de salida
                if (puertaSalidaPos != null && posJugador.esIgual(puertaSalidaPos)) {
                    System.out.println(" ¡Estás en la puerta de salida!");
                    if (jugadorGlobal.getPiezaHabitacion()) {
                        Juego.habitacionSuperada(habitacionActual);
                        gameManager.siguienteHabitacion();
                        if (!gameManager.isJuegoActivo()) {
                            Juego.juegoCompletado();
                        }
                        continue;
                    } else {
                        System.out.println(" La puerta está cerrada. Necesitas encontrar la pieza (P)");
                    }
                }
                
                // Verificar colisiones
                objetoEncontrado = habitacionActual.buscarObjetoSinJugador(posJugador);
                
                if (objetoEncontrado != -1) {
                    ObjetoJuego objetoJuego = habitacionActual.getObjeto(objetoEncontrado);
                    
                    System.out.println(" ¡Has encontrado un objeto");
                    
                    switch (objetoJuego.getTipoObjeto()) {
                        case ObjetoJuego.HADRON:
                            System.out.println(" TIPO: HADRÓN (ENEMIGO)");
                            int vidaPerdida = Juego.lanzarDado(2) * distanciaRecorrida;
                            if (vidaPerdida < 1) vidaPerdida = 1;
                            
                            // Buscar escudo activo en la mochila
                            Objeto escudo = mochila.buscarEscudoActivo();
                            if (escudo != null) {
                                if (escudo.proteger(jugadorGlobal, vidaPerdida)) {
                                    // Eliminar escudo de la mochila si ya fue usado
                                    for (int i = 0; i < mochila.getObjetos().size(); i++) {
                                        if (mochila.getObjetos().get(i) == escudo) {
                                            mochila.usarObjeto(i);
                                            break;
                                        }
                                    }
                                    vidaPerdida = 0;
                                }
                            }
                            
                            if (vidaPerdida > 0) {
                                accionJuego = Juego.explotaHadron(vidaPerdida, jugadorGlobal);
                            } else {
                                System.out.println(" El escudo absorbió todo el daño!");
                            }
                            
                            habitacionActual.eliminarObjetoPorIndice(objetoEncontrado);
                            System.out.println(" El hadrón ha sido eliminado.");
                            
                            if (accionJuego == Juego.SALIR_JUEGO && jugadorGlobal.getVida() <= 0) {
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
                            habitacionActual.eliminarObjetoPorIndice(objetoEncontrado);
                            break;
                            
                        case ObjetoJuego.OBJETO:
                            System.out.println(" TIPO: OBJETO MISTERIOSO");
                            Objeto objeto = (Objeto) objetoJuego;
                            System.out.println(" " + objeto.getNombre());
                            System.out.println("   " + objeto.getDescripcion());
                            
                            // Intentar agregar a la mochila
                            if (mochila.agregarObjeto(objeto)) {
                                System.out.println(" Objeto guardado en la mochila.");
                                habitacionActual.eliminarObjetoPorIndice(objetoEncontrado);
                            } else {
                                System.out.println(" Mochila llena. El objeto se queda en el suelo.");
                            }
                            break;
                    }
                } else {
                    System.out.println(" No hay objetos en la nueva posición.");
                }
                
                // Verificar si el jugador murió
                if (gameManager.getJugadorGlobal().getVida() <= 0) {
                    System.out.println("\n GAME OVER - Has muerto ");
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