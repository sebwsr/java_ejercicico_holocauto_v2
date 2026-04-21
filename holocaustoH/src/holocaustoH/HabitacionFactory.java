package holocaustoH;

import java.util.concurrent.ThreadLocalRandom;

public class HabitacionFactory {
    private static int contadorPosiciones = 0;
    private static Posicion[] posicionesUsadas = new Posicion[100];
    
    public static Habitacion crearHabitacion(int id, Posicion puertaEntrada, Jugador jugadorReferencia, int totalHabitaciones) {
        Habitacion habitacion = new Habitacion(id);
        
        // Configurar puertas
        Posicion puertaSalida = generarPosicionAleatoriaEnOrilla();
        habitacion.setPuertaEntrada(puertaEntrada);
        habitacion.setPuertaSalida(puertaSalida);
        
        // Crear objetos de puerta
        ObjetoJuego puertaInObj = crearObjetoPuerta(puertaEntrada, ObjetoJuego.PUERTA_IN, 'E');
        ObjetoJuego puertaOutObj = crearObjetoPuerta(puertaSalida, ObjetoJuego.PUERTA_OUT, 'S');
        
        // Clonar jugador para esta habitacion
        Jugador jugadorHabitacion = new Jugador();
        jugadorHabitacion.setPos(new Posicion(puertaEntrada.getPosX(), puertaEntrada.getPosY()));
        jugadorHabitacion.setLetraMapa('J');
        jugadorHabitacion.setTipoObjeto(ObjetoJuego.JUGADOR);
        jugadorHabitacion.setVida(jugadorReferencia.getVida());
        jugadorHabitacion.setPiezasRecogidas(0);
        
        // DETERMINAR DIFICULTAD SEGUN LA HABITACION
        boolean esUltimaHabitacion = (id == totalHabitaciones - 1);
        
        int numHadrones;
        int numPiezas;
        int numObjetos;
        
        if (esUltimaHabitacion) {
            // ULTIMA HABITACION - MAS DIFICIL
            numHadrones = 5;
            numPiezas = 3;
            numObjetos = 1;
            jugadorHabitacion.setPiezasNecesarias(numPiezas);
            System.out.println("\n=== ATENCION: ULTIMA HABITACION ===");
            System.out.println("Necesitas recolectar " + numPiezas + " piezas para abrir la salida!");
            System.out.println("Hay " + numHadrones + " hadrones acechando...");
            System.out.println("===================================\n");
        } else {
            // HABITACIONES NORMALES
            numHadrones = 1;
            numPiezas = 1;
            numObjetos = 2;
            jugadorHabitacion.setPiezasNecesarias(numPiezas);
        }
        
        // Agregar objetos a la habitacion
        habitacion.agregarObjeto(jugadorHabitacion);
        habitacion.agregarObjeto(puertaInObj);
        habitacion.agregarObjeto(puertaOutObj);
        
        // Agregar elementos segun dificultad
        agregarHadrones(habitacion, numHadrones);
        agregarPiezas(habitacion, numPiezas);
        agregarObjetosOcultos(habitacion, numObjetos);
        
        return habitacion;
    }
    
    private static Posicion generarPosicionAleatoriaEnOrilla() {
        int intentos = 0;
        while (intentos < 100) {
            int pared = ThreadLocalRandom.current().nextInt(0, 4);
            int posX, posY;
            
            switch (pared) {
                case 0:
                    posX = ThreadLocalRandom.current().nextInt(1, Habitacion.ANCHO - 1);
                    posY = 0;
                    break;
                case 1:
                    posX = ThreadLocalRandom.current().nextInt(1, Habitacion.ANCHO - 1);
                    posY = Habitacion.ALTO - 1;
                    break;
                case 2:
                    posX = 0;
                    posY = ThreadLocalRandom.current().nextInt(1, Habitacion.ALTO - 1);
                    break;
                default:
                    posX = Habitacion.ANCHO - 1;
                    posY = ThreadLocalRandom.current().nextInt(1, Habitacion.ALTO - 1);
                    break;
            }
            
            Posicion nuevaPos = new Posicion(posX, posY);
            
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
        return new Posicion(Habitacion.ANCHO - 1, 5);
    }
    
    private static Posicion generarPosicionAleatoriaInterna() {
        int intentos = 0;
        while (intentos < 100) {
            int posX = ThreadLocalRandom.current().nextInt(2, Habitacion.ANCHO - 2);
            int posY = ThreadLocalRandom.current().nextInt(2, Habitacion.ALTO - 2);
            Posicion nuevaPos = new Posicion(posX, posY);
            
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
            
            objeto.setTipoEspecial(ThreadLocalRandom.current().nextInt(1, 3));
            objeto.setPeso(ThreadLocalRandom.current().nextInt(1, 4));
            
            habitacion.agregarObjeto(objeto);
        }
    }
    
    public static void resetPosiciones() {
        contadorPosiciones = 0;
        posicionesUsadas = new Posicion[100];
    }
}