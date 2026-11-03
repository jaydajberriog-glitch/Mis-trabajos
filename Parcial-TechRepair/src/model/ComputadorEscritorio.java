package model;

public class ComputadorEscritorio extends Equipo {
    private String tipoProcesador;
    private int memoriaRamGB;
    private int almacenamientoGB;
    private boolean tarjetaGraficaDedicada;

    public ComputadorEscritorio(String codigo, String serie, String marca, String modelo, int anio, String estado, Cliente cliente, String procesador, int ram, int almacenamiento, boolean grafica) {
        super(codigo, serie, marca, modelo, anio, estado, cliente);
        if (procesador == null || procesador.trim().isEmpty() || ram <= 0 || almacenamiento <= 0) {
            throw new IllegalArgumentException("Procesador, RAM y almacenamiento deben ser válidos.");
        }
        this.tipoProcesador = procesador.trim();
        this.memoriaRamGB = ram;
        this.almacenamientoGB = almacenamiento;
        this.tarjetaGraficaDedicada = grafica;
    }

    @Override
    public TipoEquipo getTipoEquipo() {
        return TipoEquipo.ESCRITORIO;
    }

    public String getTipoProcesador() { return tipoProcesador; }
    public int getMemoriaRamGB() { return memoriaRamGB; }
    public int getAlmacenamientoGB() { return almacenamientoGB; }
    public boolean isTarjetaGraficaDedicada() { return tarjetaGraficaDedicada; }

    @Override
    public String toString() {
        return super.toString() + " | Procesador: " + tipoProcesador + " | RAM: " + memoriaRamGB + " GB | Almacenamiento: " + almacenamientoGB + " GB | Gráfica dedicada: " + tarjetaGraficaDedicada;
    }
}
