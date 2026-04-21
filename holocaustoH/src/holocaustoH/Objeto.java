package holocaustoH;

public class Objeto extends ObjetoJuego {
    // Tipos de objetos especiales
    public static final int TIPO_CURA = 1;
    public static final int TIPO_ESCUDO = 2;
    
    private int peso;
    private int tipoEspecial; // 1=Cura, 2=Escudo
    private boolean usado; // Para el escudo
    
    public Objeto() {
        super.setTipoObjeto(ObjetoJuego.OBJETO);
        this.usado = false;
    }
    
    public int getPeso() {
        return peso;
    }
    
    public void setPeso(int peso) {
        this.peso = peso;
    }
    
    public int getTipoEspecial() {
        return tipoEspecial;
    }
    
    public void setTipoEspecial(int tipoEspecial) {
        this.tipoEspecial = tipoEspecial;
        if (tipoEspecial == TIPO_CURA) {
            this.setNombre("Poción de Cura");
            this.setLetraMapa('O');
        } else if (tipoEspecial == TIPO_ESCUDO) {
            this.setNombre("Escudo Protector");
            this.setLetraMapa('O');
        }
    }
    
    public boolean isUsado() {
        return usado;
    }
    
    public void setUsado(boolean usado) {
        this.usado = usado;
    }
    
    // Aplicar efecto del objeto al jugador
    public void aplicarEfecto(Jugador jugador) {
        if (usado) {
            System.out.println(" Este objeto ya fue usado.");
            return;
        }
        
        if (tipoEspecial == TIPO_CURA) {
            int vidaMaxima = Jugador.VIDA_INICIAL;
            int curacion = (vidaMaxima * 30) / 100; // 30% de cura
            int nuevaVida = jugador.getVida() + curacion;
            
            if (nuevaVida > vidaMaxima) {
                nuevaVida = vidaMaxima;
            }
            
            System.out.println("   Aplicando cura...");
            System.out.println("   Vida anterior: " + jugador.getVida());
            System.out.println("   +" + curacion + " puntos de vida");
            jugador.setVida(nuevaVida);
            System.out.println("   Vida actual: " + jugador.getVida());
            usado = true;
            
        } else if (tipoEspecial == TIPO_ESCUDO) {
            System.out.println(" Escudo activado! Te protegerá de un golpe mortal.");
            usado = true;
        }
    }
    
    // Verificar si el escudo puede proteger
    public boolean proteger(Jugador jugador, int vidaPerdida) {
        if (tipoEspecial == TIPO_ESCUDO && !usado) {
            System.out.println(" ¡Tu escudo te ha protegido del hadrón!");
            usado = true;
            return true;
        }
        return false;
    }
    
    public String getDescripcion() {
        if (tipoEspecial == TIPO_CURA) {
            return "Cura el 30% de tu vida máxima (Peso: " + peso + ")";
        } else if (tipoEspecial == TIPO_ESCUDO) {
            return "Te protege de un golpe mortal (Peso: " + peso + ")";
        }
        return "Objeto misterioso (Peso: " + peso + ")";
    }
}