package holocaustoH;

import java.util.ArrayList;
import java.util.List;
//la mochila no es hija de objetojuego porque no va a tener posisiscion se considera que ya esta con el jugador 
public class Mochila {
    private static final int PESO_MAXIMO = 5;
    private int pesoActual;
    private List<Objeto> objetos;
    
    public Mochila() {
        this.pesoActual = 0;
        this.objetos = new ArrayList<>();
    }
    
    public boolean agregarObjeto(Objeto objeto) {
        if (pesoActual + objeto.getPeso() <= PESO_MAXIMO) {
            objetos.add(objeto);
            pesoActual += objeto.getPeso();
            System.out.println(" Objeto guardado en la mochila. Peso actual: " + pesoActual + "/" + PESO_MAXIMO);
            return true;
        } else {
            System.out.println(" Mochila llena! Peso actual: " + pesoActual + "/" + PESO_MAXIMO);
            return false;
        }
    }
    
    public Objeto usarObjeto(int index) {
        if (index >= 0 && index < objetos.size()) {
            Objeto objeto = objetos.remove(index);
            pesoActual -= objeto.getPeso();
            System.out.println(" Usaste: " + objeto.getNombre() + " - Peso actual: " + pesoActual + "/" + PESO_MAXIMO);
            return objeto;
        }
        return null;
    }
    
    public void mostrarMochila() {
        if (objetos.isEmpty()) {
            System.out.println(" Mochila vacía. (Peso: 0/" + PESO_MAXIMO + ")");
        } else {
            System.out.println("==========  MOCHILA ==========");
            System.out.println("Peso: " + pesoActual + "/" + PESO_MAXIMO);
            System.out.println("Objetos guardados:");
            for (int i = 0; i < objetos.size(); i++) {
                Objeto obj = objetos.get(i);
                String icono = (obj.getTipoEspecial() == Objeto.TIPO_CURA) ? "C" : "D";
                System.out.println("  [" + i + "] " + icono + " " + obj.getNombre() + " (Peso: " + obj.getPeso() + ")");
            }
            System.out.println("=================================");
        }
    }
    
    public Objeto buscarEscudoActivo() {
        for (Objeto obj : objetos) {
            if (obj.getTipoEspecial() == Objeto.TIPO_ESCUDO && !obj.isUsado()) {
                return obj;
            }
        }
        return null;
    }
    
    public int getPesoActual() {
        return pesoActual;
    }
    
    public int getPesoMaximo() {
        return PESO_MAXIMO;
    }
    
    public List<Objeto> getObjetos() {
        return objetos;
    }
    
    public boolean estaVacia() {
        return objetos.isEmpty();
    }
    
    public int getNumeroObjetos() {
        return objetos.size();
    }
}