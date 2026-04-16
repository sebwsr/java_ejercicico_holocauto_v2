package holocaustoH;

public class ObjetoJuego {
	// Tipos de ObjetoJuego
	public static final int JUGADOR = 0;
	public static final int PUERTA_IN = 1;
	public static final int PUERTA_OUT = 2;
	public static final int HADRON = 3;
	public static final int PIEZA = 4;
	public static final int OBJETO = 5;

	private String nombre;
	private Posicion pos;
	private int tipoObjeto = -1;
	private char letraMapa = ' ';

	public ObjetoJuego() {
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Posicion getPos() {
		return pos;
	}

	public void setPos(Posicion pos) {
		this.pos = pos;
	}

	public char getLetraMapa() {
		return letraMapa;
	}

	public void setLetraMapa(char letraMapa) {
		this.letraMapa = letraMapa;
	}

	public int getTipoObjeto() {
		return tipoObjeto;
	}

	public void setTipoObjeto(int tipoObjeto) {
		this.tipoObjeto = tipoObjeto;
	}
}