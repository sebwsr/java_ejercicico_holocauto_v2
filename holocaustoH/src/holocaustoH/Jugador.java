package holocaustoH;

public class Jugador extends Personaje {
	public static final int VIDA_INICIAL = 5;
	
	private int vida = VIDA_INICIAL;
	private boolean piezaHabitacion = false;

	public Jugador() {
		super.setTipoObjeto(ObjetoJuego.JUGADOR);
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public boolean getPiezaHabitacion() {
		return piezaHabitacion;
	}

	public void setPiezaHabitacion(boolean piezaHabitacion) {
		this.piezaHabitacion = piezaHabitacion;
	}
}