package model;

public class TelefonoInteligente extends Equipo {
    private String sistemaOperativo;
    private int almacenamientoGB;
    private int bateriaMah;
    private boolean compatible5G;

    public TelefonoInteligente(String codigo, String serie, String marca, String modelo, int anio, String estado, Cliente cliente, String sistema, int almacenamiento, int bateria, boolean cincoG) {
        super(codigo, serie, marca, modelo, anio, estado, cliente);
        if (sistema == null || sistema.trim().isEmpty() || almacenamiento <= 0 || bateria <= 0) {
            throw new IllegalArgumentException("Sistema operativo, almacenamiento y batería deben ser válidos.");
        }
        this.sistemaOperativo = sistema.trim();
        this.almacenamientoGB = almacenamiento;
        this.bateriaMah = bateria;
        this.compatible5G = cincoG;
    }

    @Override
    public TipoEquipo getTipoEquipo() {
        return TipoEquipo.TELEFONO_INTELIGENTE;
    }

    public String getSistemaOperativo() { return sistemaOperativo; }
    public int getAlmacenamientoGB() { return almacenamientoGB; }
    public int getBateriaMah() { return bateriaMah; }
    public boolean isCompatible5G() { return compatible5G; }

    @Override
    public String toString() {
        return super.toString() + " | Sistema: " + sistemaOperativo + " | Almacenamiento: " + almacenamientoGB + " GB | Batería: " + bateriaMah + " mAh | 5G: " + compatible5G;
    }
}
