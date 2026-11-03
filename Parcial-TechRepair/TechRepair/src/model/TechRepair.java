package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TechRepair {
    private String nombre;
    private List<Cliente> clientes = new ArrayList<>();
    private List<Equipo> equipos = new ArrayList<>();
    private List<Tecnico> tecnicos = new ArrayList<>();
    private List<Repuesto> repuestos = new ArrayList<>();
    private List<OrdenServicio> ordenes = new ArrayList<>();
    private List<Servicio> servicios = new ArrayList<>();

    public TechRepair(String nombre) {
        this.nombre = nombre;
        servicios.add(new Diagnostico());
        servicios.add(new MantenimientoPreventivo());
        servicios.add(new ReparacionCorrectiva());
    }

    public String getNombre() { return nombre; }
    public List<Cliente> getClientes() { return clientes; }
    public List<Equipo> getEquipos() { return equipos; }
    public List<Tecnico> getTecnicos() { return tecnicos; }
    public List<Repuesto> getRepuestos() { return repuestos; }
    public List<OrdenServicio> getOrdenes() { return ordenes; }
    public List<Servicio> getServicios() { return servicios; }

    public String registrarCliente(Cliente c) {
        if (buscarCliente(c.getDocumento()).isPresent()) return "Ya existe un cliente con ese documento.";
        clientes.add(c);
        return "Cliente registrado correctamente.";
    }

    public Optional<Cliente> buscarCliente(String documento) {
        return clientes.stream().filter(c -> c.getDocumento().equalsIgnoreCase(documento)).findFirst();
    }

    public String registrarEquipo(Equipo e) {
        if (buscarEquipo(e.getCodigo()).isPresent()) return "Ya existe un equipo con ese código.";
        if (equipos.stream().anyMatch(x -> x.getNumeroSerie().equalsIgnoreCase(e.getNumeroSerie()))) return "Ya existe un equipo con ese número de serie.";
        if (!clientes.contains(e.getCliente())) return "El cliente no está registrado en el sistema.";
        equipos.add(e);
        return "Equipo registrado correctamente.";
    }

    public Optional<Equipo> buscarEquipo(String codigo) {
        return equipos.stream().filter(e -> e.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

    public String registrarTecnico(Tecnico t) {
        if (tecnicos.stream().anyMatch(x -> x.getCodigo().equalsIgnoreCase(t.getCodigo()) || x.getDocumento().equalsIgnoreCase(t.getDocumento()))) {
            return "El código o documento del técnico ya existe.";
        }
        tecnicos.add(t);
        return "Técnico registrado correctamente.";
    }

    public Optional<Tecnico> buscarTecnico(String codigo) {
        return tecnicos.stream().filter(t -> t.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

    public String registrarRepuesto(Repuesto r) {
        if (buscarRepuesto(r.getCodigo()).isPresent()) return "Ya existe un repuesto con ese código.";
        repuestos.add(r);
        return "Repuesto registrado correctamente.";
    }

    public Optional<Repuesto> buscarRepuesto(String codigo) {
        return repuestos.stream().filter(r -> r.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

    public String crearOrden(String codigo, String documento, String observaciones) {
        if (buscarOrden(codigo).isPresent()) return "Ya existe una orden con ese código.";
        Optional<Cliente> c = buscarCliente(documento);
        if (c.isEmpty()) return "No existe ese cliente.";
        ordenes.add(new OrdenServicio(codigo, c.get(), observaciones));
        return "Orden creada correctamente.";
    }

    public Optional<OrdenServicio> buscarOrden(String codigo) {
        return ordenes.stream().filter(o -> o.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

    public Optional<Servicio> buscarServicio(String codigo) {
        return servicios.stream().filter(s -> s.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

    public List<OrdenServicio> consultarOrdenesCliente(String documento) {
        List<OrdenServicio> resultado = new ArrayList<>();
        for (OrdenServicio o : ordenes) {
            if (o.getCliente().getDocumento().equalsIgnoreCase(documento)) resultado.add(o);
        }
        return resultado;
    }

    public List<DetalleServicio> consultarTrabajosTecnico(String codigo) {
        List<DetalleServicio> resultado = new ArrayList<>();
        for (OrdenServicio o : ordenes) {
            for (DetalleServicio d : o.getDetalles()) {
                if (d.getTecnico() != null && d.getTecnico().getCodigo().equalsIgnoreCase(codigo)) {
                    resultado.add(d);
                }
            }
        }
        return resultado;
    }

    public List<DetalleServicio> consultarEquiposPendientes() {
        List<DetalleServicio> resultado = new ArrayList<>();
        for (OrdenServicio o : ordenes) {
            for (DetalleServicio d : o.getDetalles()) {
                if (d.getEstado() != DetalleServicio.EstadoDetalle.FINALIZADO && d.getEstado() != DetalleServicio.EstadoDetalle.CANCELADO) {
                    resultado.add(d);
                }
            }
        }
        return resultado;
    }

    public List<Repuesto> consultarInventarioCritico() {
        List<Repuesto> resultado = new ArrayList<>();
        for (Repuesto r : repuestos) {
            if (r.getCantidadDisponible() < 5) resultado.add(r);
        }
        return resultado;
    }

    public String reporteServicios() {
        StringBuilder sb = new StringBuilder("REPORTE DE SERVICIOS\n");
        for (Servicio s : servicios) {
            int cantidad = 0;
            double total = 0;
            for (OrdenServicio o : ordenes) {
                for (DetalleServicio d : o.getDetalles()) {
                    if (d.getServicio().getCodigo().equals(s.getCodigo())) {
                        cantidad++;
                        total += d.calcularValor();
                    }
                }
            }
            sb.append(s.getNombre()).append(": ").append(cantidad).append(" intervención(es), total $ ")
              .append(String.format("%.0f", total)).append("\n");
        }
        return sb.toString();
    }

    public String reporteTecnicos() {
        StringBuilder sb = new StringBuilder("REPORTE DE TÉCNICOS\n");
        for (Tecnico t : tecnicos) {
            int cantidad = 0;
            double total = 0;
            for (DetalleServicio d : consultarTrabajosTecnico(t.getCodigo())) {
                if (d.getEstado() == DetalleServicio.EstadoDetalle.FINALIZADO) {
                    cantidad++;
                    total += d.calcularValor();
                }
            }
            sb.append(t.getNombre()).append(" (").append(t.getCodigo()).append("): ")
              .append(cantidad).append(" intervención(es) finalizadas, valor $ ")
              .append(String.format("%.0f", total)).append("\n");
        }
        return sb.toString();
    }
}
