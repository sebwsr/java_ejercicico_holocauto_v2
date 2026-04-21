package holocaustoH;

import java.util.ArrayList;
import java.util.List;

public class Mapa {
    private List<Habitacion> habitaciones;

    public Mapa(int numHabitaciones, Jugador jugador) {
        this.habitaciones = new ArrayList<>();
        generarHabitaciones(numHabitaciones, jugador);
    }

    private void generarHabitaciones(int numHabitaciones, Jugador jugador) {
        HabitacionFactory.resetPosiciones();
        Posicion puertaEntrada = new Posicion(3, 0); 
        Posicion puertaSalidaAnterior = null;

        for (int i = 0; i < numHabitaciones; i++) {
            if (i > 0) {
                puertaEntrada = puertaSalidaAnterior;
            }
            // Usa tu fábrica original sin modificaciones
            Habitacion hab = HabitacionFactory.crearHabitacion(i, puertaEntrada, jugador, numHabitaciones);
            habitaciones.add(hab);
            puertaSalidaAnterior = hab.getPuertaSalida();
        }
    }

    public Habitacion getHabitacion(int indice) {
        if (indice >= 0 && indice < habitaciones.size()) {
            return habitaciones.get(indice);
        }
        return null;
    }

    public int getCantidadHabitaciones() {
        return habitaciones.size();
    }
}