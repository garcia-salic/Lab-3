public abstract class Envios {

     private final String codigo;
    private final String remitente;
    private final String destinatario;
    protected final double pesoReal;
    protected final String destino;
    private String estado = "Bodega";
    private int intentosEntrega;

    protected Envios(String codigo, String remitente, String destinatario, double pesoReal, String destino) {
        if (codigo == null || codigo.trim().isEmpty() || remitente == null || remitente.trim().isEmpty()
                || destinatario == null || destinatario.trim().isEmpty()) {
            throw new IllegalArgumentException("Código, remitente y destinatario son obligatorios.");
        }
        if (!Double.isFinite(pesoReal) || pesoReal <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        if (destino == null || (!destino.equalsIgnoreCase("Urbano")
                && !destino.equalsIgnoreCase("Departamental"))) {
            throw new IllegalArgumentException("El destino debe ser Urbano o Departamental.");
        }
        this.codigo = codigo.trim();
        this.remitente = remitente.trim();
        this.destinatario = destinatario.trim();
        this.pesoReal = pesoReal;
        this.destino = destino.equalsIgnoreCase("Urbano") ? "Urbano" : "Departamental";
    }

    public abstract double calcularCosto();

    public abstract int estimarTiempoEntrega();

    public abstract int obtenerMaximoIntentos();

    public boolean requiereRefrigeracion() {
        return false;
    }

    public boolean puedeReenviarse() {
        return intentosEntrega < obtenerMaximoIntentos()&& !estado.equals("Devuelto al remitente") && !estado.equals("Entregado");
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEstado() {
        return estado;
    }

    public double getPesoReal() {
        return pesoReal;
    }

    public String getDestino() {
        return destino;
    }

    public int getIntentosEntrega() {
        return intentosEntrega;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getCategoria() {
        return getClass().getSimpleName();
    }

    void asignar() {
        if (!estado.equals("Bodega") || !puedeReenviarse())
            throw new IllegalStateException("El envío no está disponible en bodega.");
        estado = "Asignado";
    }

    void retirar() {
        if (!estado.equals("Asignado")) {

            throw new IllegalStateException("El envío no está asignado.");
        }
        estado = "Bodega";
    }

    void salir() {
        if (!estado.equals("Asignado")) {
            throw new IllegalStateException("El envío no está asignado.");
        }
        estado = "En ruta";

    }

    void registrarResultado(boolean entregado) {
        if (!estado.equals("En ruta")) {
            throw new IllegalStateException("El envío no está en ruta.");

        }
        intentosEntrega++;
        estado = entregado ? "Entregado" : intentosEntrega >= obtenerMaximoIntentos() ? "Devuelto al remitente" : "Bodega";
    }
}
