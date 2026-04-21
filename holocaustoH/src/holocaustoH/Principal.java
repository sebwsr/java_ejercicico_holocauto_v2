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
        gameManager.inicializarJuego(4);
        
        Juego.mostrarBienvenida();
        Juego.mostrarAyuda();
        
        while (gameManager.isJuegoActivo()) {
            Habitacion habitacionActual = gameManager.getHabitacionActual();
            Posicion puertaSalidaPos = habitacionActual.getPuertaSalida();
            Jugador jugadorGlobal = gameManager.getJugadorGlobal();
            Personaje jugador = habitacionActual.getJugador();
            Posicion posJugador = jugador.getPos();
            Mochila mochila = gameManager.getMochila();
            
            Juego.pintarHabitacion(habitacionActual);
            Juego.mostrarEstado();
            
            // --- NUEVO MENÚ INTERACTIVO ---
            System.out.println("\n--- MENU DE TURNO ---");
            System.out.println("1. Moverse");
            System.out.println("2. Abrir Mochila");
            System.out.println("3. Salir del juego (999)");
            int accion = pregunta("Elige una opcion: ");

            if (accion == 999 || accion == 3) {
                System.out.println("\nGracias por jugar. Hasta pronto!");
                break;
            }

            // GESTIÓN MANUAL DE LA MOCHILA
            if (accion == 2) {
                mochila.mostrarMochila();
                if (!mochila.estaVacia()) {
                    int idx = pregunta("Indice del objeto a usar (-1 para volver): ");
                    if (idx >= 0 && idx < mochila.getNumeroObjetos()) {
                        Objeto obj = mochila.getObjetos().get(idx);
                        if (obj.getTipoEspecial() == Objeto.TIPO_CURA) {
                            obj = mochila.usarObjeto(idx); // Lo saca de la mochila
                            obj.aplicarEfecto(jugadorGlobal); // Aplica la cura
                            
                            // Sincronizar la vida visual en la habitacion actual
                            Jugador jugadorHab = habitacionActual.getJugadorComoJugador();
                            if (jugadorHab != null) {
                                jugadorHab.setVida(jugadorGlobal.getVida());
                            }
                        } else {
                            System.out.println(" Los escudos son pasivos. Se activan solos cuando recibes dano.");
                        }
                    }
                }
                continue; // Vuelve a imprimir el mapa sin saltar de turno
            }

            // LÓGICA DE MOVIMIENTO
            if (accion == 1) {
                int numMov = Juego.lanzarDado(10);
                System.out.println("Tienes " + numMov + " pasos. Usa negativos para retroceder.");
                
                int movX = pregunta("Cuantas columnas mover (-" + numMov + " a +" + numMov + "): ");
                if (Math.abs(movX) > numMov) {
                    System.out.println("Ajustando a " + numMov + " pasos");
                    movX = (movX > 0) ? numMov : -numMov;
                }
                jugador.movX(movX);
                numMov -= Math.abs(movX);
                
                int movY = 0;
                if (numMov > 0) {
                    movY = pregunta("Cuantas filas mover (-" + numMov + " a +" + numMov + "): ");
                    if (Math.abs(movY) > numMov) {
                        System.out.println("Ajustando a " + numMov + " pasos");
                        movY = (movY > 0) ? numMov : -numMov;
                    }
                    jugador.movY(movY);
                    numMov -= Math.abs(movY);
                }
                
                Juego.limitesHab(posJugador, puertaSalidaPos);
                int distanciaRecorrida = Math.abs(movX) + Math.abs(movY);
                System.out.println("\nTe has movido a (" + posJugador.getPosX() + "," + posJugador.getPosY() + ")");
                
                // Verificar puerta de salida
                if (puertaSalidaPos != null && posJugador.esIgual(puertaSalidaPos)) {
                    if (jugadorGlobal.tieneTodasLasPiezas()) {
                        System.out.println("Tienes todas las piezas! La puerta se abre!");
                        Juego.habitacionSuperada();
                        gameManager.siguienteHabitacion();
                        if (!gameManager.isJuegoActivo()) {
                            Juego.juegoCompletado();
                        }
                        continue;
                    } else {
                        System.out.println("Puerta cerrada. Necesitas " + jugadorGlobal.getPiezasNecesarias() + " piezas. Tienes: " + jugadorGlobal.getPiezasRecogidas());
                    }
                }
                
                // Verificar colisiones
                int objetoEncontrado = habitacionActual.buscarObjetoSinJugador(posJugador);
                
                if (objetoEncontrado != -1) {
                    ObjetoJuego objetoJuego = habitacionActual.getObjeto(objetoEncontrado);
                    
                    switch (objetoJuego.getTipoObjeto()) {
                        case ObjetoJuego.HADRON:
                            System.out.println("\n--- HADRON ENCONTRADO ---");
                            int vidaPerdida = Juego.lanzarDado(2) * distanciaRecorrida;
                            if (vidaPerdida < 1) vidaPerdida = 1;
                            
                            // El escudo se gasta solo en combate
                            Objeto escudo = mochila.buscarEscudoActivo();
                            if (escudo != null) {
                                System.out.println("Escudo protector activo! Absorbe el dano.");
                                for (int i = 0; i < mochila.getObjetos().size(); i++) {
                                    if (mochila.getObjetos().get(i) == escudo) {
                                        mochila.usarObjeto(i);
                                        break;
                                    }
                                }
                                vidaPerdida = 0;
                            }
                            
                            if (vidaPerdida > 0) {
                                int nuevaVida = jugadorGlobal.getVida() - vidaPerdida;
                                jugadorGlobal.setVida(nuevaVida);
                                System.out.println("Has perdido " + vidaPerdida + " puntos de vida");
                                System.out.println("Vida restante: " + nuevaVida);
                            }
                            
                            habitacionActual.eliminarObjetoPorIndice(objetoEncontrado);
                            System.out.println();
                            break;
                            
                        case ObjetoJuego.PIEZA:
                            System.out.println("\n--- PIEZA ENCONTRADA ---");
                            jugadorGlobal.incrementarPiezas();
                            Jugador jugadorHab = habitacionActual.getJugadorComoJugador();
                            if (jugadorHab != null) {
                                jugadorHab.setPiezasRecogidas(jugadorGlobal.getPiezasRecogidas());
                            }
                            habitacionActual.eliminarObjetoPorIndice(objetoEncontrado);
                            System.out.println();
                            break;
                            
                        case ObjetoJuego.OBJETO:
                            System.out.println("\n--- OBJETO ENCONTRADO ---");
                            Objeto objeto = (Objeto) objetoJuego;
                            System.out.println("Objeto: " + objeto.getNombre());
                            System.out.println("Peso: " + objeto.getPeso());
                            
                            if (mochila.agregarObjeto(objeto)) {
                                habitacionActual.eliminarObjetoPorIndice(objetoEncontrado);
                            } else {
                                System.out.println("Mochila llena. No puedes llevar mas objetos.");
                            }
                            System.out.println();
                            break;
                    }
                } // Fin de verificación de colisiones
                
                if (jugadorGlobal.getVida() <= 0) {
                    Juego.gameOver();
                    break;
                }
            } // Fin de Lógica de Movimiento
        }
    }
}