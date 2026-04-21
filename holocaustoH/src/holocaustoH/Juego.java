package holocaustoH;

import java.util.concurrent.ThreadLocalRandom;

public class Juego {
    static JuegoManager gm = JuegoManager.getInstance();
    
    public static void pintarHabitacion(Habitacion h) {
        System.out.println("========== HABITACION " + (h.getId() + 1) + " ==========");
        
        System.out.print("   ");
        for (int col = 0; col < Habitacion.ANCHO; col++) {
            if (col > 9) System.out.print(col % 10);
            else System.out.print(col);
        }
        System.out.println();
        
        for (int fil = 0; fil < Habitacion.ALTO; fil++) {
            System.out.print((fil > 9 ? "" : " ") + fil + " ");
            for (int col = 0; col < Habitacion.ANCHO; col++) {
                Posicion posActual = new Posicion(col, fil);
                int objIndex = h.buscarObjeto(posActual);
                
                if (objIndex != -1) {
                    ObjetoJuego obj = h.getObjeto(objIndex);
                    
                    if (obj.getTipoObjeto() == ObjetoJuego.PUERTA_OUT) {
                        Jugador jugador = h.getJugadorComoJugador();
                        if (jugador != null && jugador.tieneTodasLasPiezas()) {
                            System.out.print('S');
                        } else {
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
    
    private static char obtenerCaracterPared(int col, int fil) {
        if (fil == 0 || fil == Habitacion.ALTO - 1) {
            return '=';
        } else {
            return '|';
        }
    }
    
    public static void limitesHab(Posicion posJugador, Posicion puertaSalidaPos) {
        if ((posJugador.getPosX() < 1) && !posJugador.esIgual(puertaSalidaPos)) {
            posJugador.setPosX(1);
            System.out.println("Has chocado contra una pared. Movimiento ajustado.");
        }
        if ((posJugador.getPosX() >= Habitacion.ANCHO - 1) && !posJugador.esIgual(puertaSalidaPos)) {
            posJugador.setPosX(Habitacion.ANCHO - 2);
            System.out.println("Has chocado contra una pared. Movimiento ajustado.");
        }
        if ((posJugador.getPosY() < 1) && !posJugador.esIgual(puertaSalidaPos)) {
            posJugador.setPosY(1);
            System.out.println("Has chocado contra una pared. Movimiento ajustado.");
        }
        if ((posJugador.getPosY() >= Habitacion.ALTO - 1) && !posJugador.esIgual(puertaSalidaPos)) {
            posJugador.setPosY(Habitacion.ALTO - 2);
            System.out.println("Has chocado contra una pared. Movimiento ajustado.");
        }
    }
    
    public static void mostrarBienvenida() {
        System.out.println("\n===============================================");
        System.out.println("     BIENVENIDO A HOLOCAUSTO H - HADRON");
        System.out.println("===============================================");
        System.out.println("\nOBJETIVO: Supera " + gm.getTotalHabitaciones() + " habitaciones");
        System.out.println("REGLAS:");
        System.out.println("  - Recoge las piezas (P) para abrir la puerta de salida (S)");
        System.out.println("  - En la ultima habitacion necesitas 3 piezas");
        System.out.println("  - Evita los hadrones (O) o perderas vida");
        System.out.println("  - Los objetos (O) son curas o escudos");
        System.out.println("  - Las curas se usan automaticamente al recibir dano\n");
    }
    
    public static void mostrarEstado() {
        Jugador jugador = gm.getJugadorGlobal();
        System.out.println("VIDA: " + jugador.getVida());
        System.out.println("HABITACION: " + (gm.getHabitacionActualNum() + 1) + " de " + gm.getTotalHabitaciones());
        System.out.println("PIEZAS: " + jugador.getPiezasRecogidas() + " de " + jugador.getPiezasNecesarias());
    }
    
    public static int lanzarDado(int numCaras) {
        int dado = ThreadLocalRandom.current().nextInt(1, numCaras + 1);
        System.out.println("[DADO] Has sacado un " + dado);
        return dado;
    }
    
    public static void habitacionSuperada() {
        System.out.println("========== HABITACION SUPERADA ==========");
        System.out.println("Has encontrado la salida!");
        System.out.println("=========================================");
    }
    
    public static void juegoCompletado() {
        System.out.println("\n===============================================");
        System.out.println("     FELICITACIONES! HAS COMPLETADO EL JUEGO");
        System.out.println("===============================================\n");
    }
    
    public static void gameOver() {
        System.out.println("\n===============================================");
        System.out.println("               GAME OVER - HAS MUERTO");
        System.out.println("===============================================\n");
    }
    
    public static void mostrarAyuda() {
        System.out.println("AYUDA:");
        System.out.println("  J = Tu personaje");
        System.out.println("  O = Hadron (enemigo) o Objeto (cura/escudo)");
        System.out.println("  P = Pieza");
        System.out.println("  E = Puerta de entrada");
        System.out.println("  S = Puerta de salida (solo visible con todas las piezas)");
        System.out.println("  Para salir del juego, escribe 999 en X o Y\n");
    }
}