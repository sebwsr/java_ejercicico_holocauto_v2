package holocaustoH;

public class Objeto extends ObjetoJuego {
	private int peso = 0;

	public Objeto() {
		super.setTipoObjeto(ObjetoJuego.OBJETO);
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
}