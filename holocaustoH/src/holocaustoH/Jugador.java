package holocaustoH;

public class Jugador extends Personaje {
    public static final int VIDA_INICIAL = 5;
    
    private int vida = VIDA_INICIAL;
    private int piezasRecogidas = 0;
    private int piezasNecesarias = 1;

    public Jugador() {
        super.setTipoObjeto(ObjetoJuego.JUGADOR);
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getPiezasRecogidas() {
        return piezasRecogidas;
    }

    public void setPiezasRecogidas(int piezasRecogidas) {
        this.piezasRecogidas = piezasRecogidas;
    }
    
    public void incrementarPiezas() {
        this.piezasRecogidas++;
        System.out.println("Piezas recogidas: " + piezasRecogidas + " de " + piezasNecesarias);
    }
    
    public int getPiezasNecesarias() {
        return piezasNecesarias;
    }
    
    public void setPiezasNecesarias(int piezasNecesarias) {
        this.piezasNecesarias = piezasNecesarias;
    }
    
    public boolean tieneTodasLasPiezas() {
        return piezasRecogidas >= piezasNecesarias;
    }
    
    public void resetPiezasHabitacion() {
        this.piezasRecogidas = 0;
    }
}