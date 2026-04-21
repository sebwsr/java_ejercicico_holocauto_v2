package holocaustoH;

public class JuegoManager {
    private static JuegoManager instancia;
    private Mapa mapa; 
    private int habitacionActual;
    private Jugador jugador;
    private boolean juegoActivo;
    private Mochila mochila;
    
    private JuegoManager() {
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
        jugador.setVida(Jugador.VIDA_INICIAL);
        jugador.setPiezasRecogidas(0);
        
        // Aquí inicializamos la nueva clase Mapa
        this.mapa = new Mapa(numHabitaciones, jugador);
        
        habitacionActual = 0;
        juegoActivo = true;
    }
    
    public Habitacion getHabitacionActual() {
        if (mapa != null && habitacionActual < mapa.getCantidadHabitaciones()) {
            return mapa.getHabitacion(habitacionActual);
        }
        return null;
    }
    
    public void siguienteHabitacion() {
        if (habitacionActual + 1 < mapa.getCantidadHabitaciones()) {
            Habitacion habActual = mapa.getHabitacion(habitacionActual);
            Habitacion habSiguiente = mapa.getHabitacion(habitacionActual + 1);
            
            Jugador jugadorActual = (Jugador) habActual.getJugador();
            Jugador jugadorSiguiente = (Jugador) habSiguiente.getJugador();
            
            jugadorSiguiente.setVida(jugadorActual.getVida());
            jugadorSiguiente.setPiezasRecogidas(0);
            
            jugador.setVida(jugadorActual.getVida());
            jugador.setPiezasRecogidas(0);
            
            boolean esUltima = (habitacionActual + 1 == mapa.getCantidadHabitaciones() - 1);
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
        return juegoActivo && habitacionActual < mapa.getCantidadHabitaciones() && jugador.getVida() > 0;
    }
    
    public void terminarJuego() {
        juegoActivo = false;
    }
    
    public int getHabitacionActualNum() {
        return habitacionActual;
    }
    
    public int getTotalHabitaciones() {
        if (mapa == null) return 0;
        return mapa.getCantidadHabitaciones();
    }
    
    public Jugador getJugadorGlobal() {
        return jugador;
    }
    
    public Mochila getMochila() {
        return mochila;
    }
}