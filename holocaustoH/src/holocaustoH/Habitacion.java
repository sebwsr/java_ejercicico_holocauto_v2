package holocaustoH;

import java.util.ArrayList;
import java.util.List;

public class Habitacion {
	public static final int ANCHO = 10;
	public static final int ALTO = 10;
	
	private Posicion puertaEntrada;
	private Posicion puertaSalida;
	private List<ObjetoJuego> objetosJuego;
	private Personaje jugador;
	private int id;
	private boolean superada;
	
	public Habitacion(int id) {
		this.id = id;
		this.objetosJuego = new ArrayList<>();
		this.superada = false;
	}
	
	public void agregarObjeto(ObjetoJuego obj) {
		objetosJuego.add(obj);
		if (obj instanceof Personaje && obj.getTipoObjeto() == ObjetoJuego.JUGADOR) {
			this.jugador = (Personaje) obj;
		}
	}
	
	public void eliminarObjeto(ObjetoJuego obj) {
		objetosJuego.remove(obj);
	}
	
	public void eliminarObjetoPorIndice(int index) {
		if (index >= 0 && index < objetosJuego.size()) {
			objetosJuego.remove(index);
		}
	}
	
	public int buscarObjeto(Posicion p) {
		for (int i = 0; i < objetosJuego.size(); i++) {
			if (objetosJuego.get(i).getPos().esIgual(p)) {
				return i;
			}
		}
		return -1;
	}
	
	public int buscarObjetoSinJugador(Posicion p) {
	    for (int i = 0; i < objetosJuego.size(); i++) {
	        ObjetoJuego obj = objetosJuego.get(i);
	        // Saltar al jugador
	        if (obj.getTipoObjeto() == ObjetoJuego.JUGADOR) {
	            continue;
	        }
	        // Saltar puertas (se manejan por separado)
	        if (obj.getTipoObjeto() == ObjetoJuego.PUERTA_IN || 
	            obj.getTipoObjeto() == ObjetoJuego.PUERTA_OUT) {
	            continue;
	        }
	        if (obj.getPos().esIgual(p)) {
	            return i;
	        }
	    }
	    return -1;
	}
	public ObjetoJuego getObjeto(int index) {
		if (index >= 0 && index < objetosJuego.size()) {
			return objetosJuego.get(index);
		}
		return null;
	}
	
	// Agregar este método a Habitacion.java
	public Jugador getJugadorComoJugador() {
	    for (ObjetoJuego obj : objetosJuego) {
	        if (obj.getTipoObjeto() == ObjetoJuego.JUGADOR) {
	            return (Jugador) obj;
	        }
	    }
	    return null;
	}
	
	
	public boolean esPuerta(Posicion p) {
		return p.esIgual(puertaEntrada) || p.esIgual(puertaSalida);
	}
	
	public boolean esJugador(Posicion p) {
		return jugador != null && p.esIgual(jugador.getPos());
	}
	
	// Getters y Setters
	public Posicion getPuertaEntrada() { 
		return puertaEntrada; 
	}
	
	public void setPuertaEntrada(Posicion puertaEntrada) { 
		this.puertaEntrada = puertaEntrada; 
	}
	
	public Posicion getPuertaSalida() { 
		return puertaSalida; 
	}
	
	public void setPuertaSalida(Posicion puertaSalida) { 
		this.puertaSalida = puertaSalida; 
	}
	
	public Personaje getJugador() { 
		return jugador; 
	}
	
	public int getId() { 
		return id; 
	}
	
	public boolean isSuperada() { 
		return superada; 
	}
	
	public void setSuperada(boolean superada) { 
		this.superada = superada; 
	}
	
	public List<ObjetoJuego> getObjetosJuego() { 
		return objetosJuego; 
	}
}