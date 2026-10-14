import java.util.ArrayList;

public class Despacho {
    private final int numero;
    private final Vehiculo vehiculo;
    private final ArrayList<Envios> envios = new ArrayList<>();
    private final ArrayList<Envios> pendientes = new ArrayList<>();
    private String estado = "Preparación";

    public Despacho(int numero, Vehiculo vehivulo) {
        this.numero = numero;
        this.vehiculo = vehivulo;
    }

    private void exigirPreparacion() {
        if (!estado.equals("Preparacion")) {
            throw new IllegalArgumentException("El despacho y asalio o esta cerrado");

        }
    }

    public double getPesoCargado() {
        double total = 0;
        for (Envios envio : envios)
            total += envio.getPesoReal();
        return total;
    }

    public double getCapacidadDisponible() {
        return vehiculo.getCapacidad() - getPesoCargado();

    }

    public void asignar(Envios envio) {
        exigirPreparacion();
        if (envio.requiereRefrigeracion() && !vehiculo.isRefrigerado()) {
            throw new IllegalArgumentException("El vehiculo debe ser refrigerado para el tipo de envio");

        }
        if (envio.getPesoReal() > getCapacidadDisponible()) {
            throw new IllegalArgumentException("El envio supera la capacidad disp9onible");
        }
        envio.asignar();
        envios.add(envio);
    }

    public void retirar(Envios envio) {
        exigirPreparacion();
        if (!envios.contains(envio)) {
            throw new IllegalArgumentException("El envio no pertenece al despachof");
        }

        envio.retirar();
        envios.remove(envio);
    }

    public int salir() {
        exigirPreparacion();
        if (envios.isEmpty()) {
            throw new IllegalArgumentException("No puede salir un despacho vacio ");
        }
        int reintentos = 0;
        for (Envios envio : envios) {
            envio.salir();

        }
        pendientes.addAll(envios);
        estado = "En ruta";
        return reintentos;

    }

    public void registrarResultado(Envios envio, boolean entregado) {
        if (!estado.equals("En ruta")) {
            throw new IllegalStateException("El despacho no está en ruta.");

        }
        if (!pendientes.contains(envio)) {
            throw new IllegalArgumentException("El envío no está pendiente en este despacho.");
        }
        envio.registrarResultado(entregado);
        pendientes.remove(envio);
        if (pendientes.isEmpty()) {
            estado = "Cerrado";
        }
    }

    public int getNumero() {
        return numero;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public String getEstado() {
        return estado;
    }

    public ArrayList<Envios> getEnvios() {
        return new ArrayList<>(envios);
    }

    public ArrayList<Envios> getPendientes() {
        return new ArrayList<>(pendientes);
    }

}
