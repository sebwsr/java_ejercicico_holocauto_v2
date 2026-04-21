package holocaustoH;

import java.util.ArrayList;
import java.util.List;

public class JuegoManager {
    private static JuegoManager instancia;
    private List<Habitacion> habitaciones;
    private int habitacionActual;
    private Jugador jugador;
    private boolean juegoActivo;
    private Mochila mochila;
    
    private JuegoManager() {
        this.habitaciones = new ArrayList<>();
        this.habitacionActual = 0;
        this.juegoActivo = true;
        this.jugador = new Jugador();
        this.mochila = new Mochila();
    }
    
    public static JuegoManager getInstance() {
        if (instancia == null) {
            instancia = new JuegoManager();
        }
        return instancia;
    }
    
    public void inicializarJuego(int numHabitaciones) {
        HabitacionFactory.resetPosiciones();
        habitaciones.clear();
        
        jugador.setVida(Jugador.VIDA_INICIAL);
        jugador.setPiezasRecogidas(0);
        
        Posicion puertaInicial = new Posicion(3, 0);
        Posicion puertaSalidaAnterior = null;
        
        for (int i = 0; i < numHabitaciones; i++) {
            Posicion puertaEntrada;
            if (i == 0) {
                puertaEntrada = puertaInicial;
            } else {
                puertaEntrada = puertaSalidaAnterior;
            }
            
            // PASAR EL TOTAL DE HABITACIONES PARA SABER CUAL ES LA ULTIMA
            Habitacion hab = HabitacionFactory.crearHabitacion(i, puertaEntrada, jugador, numHabitaciones);
            habitaciones.add(hab);
            puertaSalidaAnterior = hab.getPuertaSalida();
        }
        
        habitacionActual = 0;
        juegoActivo = true;
    }
    
    public Habitacion getHabitacionActual() {
        if (habitacionActual < habitaciones.size()) {
            return habitaciones.get(habitacionActual);
        }
        return null;
    }
    
    public void siguienteHabitacion() {
        if (habitacionActual + 1 < habitaciones.size()) {
            Habitacion habActual = habitaciones.get(habitacionActual);
            Habitacion habSiguiente = habitaciones.get(habitacionActual + 1);
            
            Jugador jugadorActual = (Jugador) habActual.getJugador();
            Jugador jugadorSiguiente = (Jugador) habSiguiente.getJugador();
            
            jugadorSiguiente.setVida(jugadorActual.getVida());
            jugadorSiguiente.setPiezasRecogidas(0);
            
            jugador.setVida(jugadorActual.getVida());
            jugador.setPiezasRecogidas(0);
            
            // Actualizar piezas necesarias para la nueva habitacion
            boolean esUltima = (habitacionActual + 1 == habitaciones.size() - 1);
            if (esUltima) {
                jugadorSiguiente.setPiezasNecesarias(3);
                jugador.setPiezasNecesarias(3);
            } else {
                jugadorSiguiente.setPiezasNecesarias(1);
                jugador.setPiezasNecesarias(1);
            }
            
            habActual.setSuperada(true);
            habitacionActual++;
        } else {
            juegoActivo = false;
        }
    }
    
    public boolean isJuegoActivo() {
        return juegoActivo && habitacionActual < habitaciones.size() && jugador.getVida() > 0;
    }
    
    public void terminarJuego() {
        juegoActivo = false;
    }
    
    public int getHabitacionActualNum() {
        return habitacionActual;
    }
    
    public int getTotalHabitaciones() {
        return habitaciones.size();
    }
    
    public Jugador getJugadorGlobal() {
        return jugador;
    }
    
    public Mochila getMochila() {
        return mochila;
    }
}