package holocaustoH;

public class Hadron extends Personaje {
	private boolean visible = true;

	public Hadron() {
		super.setTipoObjeto(ObjetoJuego.HADRON);
	}
	
	public boolean cambioVisibilidad() {
		this.visible = !this.visible;
		return this.visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
}