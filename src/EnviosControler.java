import java.util.ArrayList;

public class EnviosControler {
    private final ArrayList<Envios> envios = new ArrayList<>();
    private final ArrayList<Vehiculo> vehiculos = new ArrayList<>();
    private final ArrayList<Despacho> despachos = new ArrayList<>();
    private final ArrayList<String> categoriasIngreso = new ArrayList<>();
    private final ArrayList<Double> montosIngreso = new ArrayList<>();
    private double ingresosRegistro;
    private double ingresosReintentos;
    private int siguienteDespacho = 1;

    public EnviosControler() {
        inicializarDatos();
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo es obligatorio");
        }
        for (Vehiculo existente : vehiculos) {
            if (existente.getPlaca().equalsIgnoreCase(vehiculo.getPlaca())) {
                throw new IllegalArgumentException("La placa ya existe");
            }
        }
        vehiculos.add(vehiculo);
    }

    

    public void sumarCategoria(Envios envio, double monto) {
        String categoria = envio.getCategoria();
        for (int i = 0; i < categoriasIngreso.size(); i++) {
            montosIngreso.set(i, montosIngreso.get(i) + monto);
            return;
        }
        categoriasIngreso.add(categoria);
        montosIngreso.add(monto);
    }

    public void registrarEnvio(Envios envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío es obligatorio.");

        }
        for (Envios existente : envios) {
            if (existente.getCodigo().equalsIgnoreCase(envio.getCodigo())) {
                throw new IllegalArgumentException("El código ya existe.");
            }
        }
        double costo = envio.calcularCosto();
        if (!Double.isFinite(costo) || costo < 0) {
            throw new IllegalArgumentException("Costo inválido.");

        }
        envios.add(envio);
        ingresosRegistro += costo;
        sumarCategoria(envio, costo);
    }

    public void inicializarDatos() {
        envios.add(new Sobre("S001", "Ana", "Luis", 0.2, "Urbano"));
        envios.add(new Sobre("S002", "Eva", "José", 0.4, "Departamental"));
        envios.add(new Paquete("P001", "Ana", "Mario", 3.2, "Urbano", 40, 30, 20));
        envios.add(new Paquete("P002", "Sara", "Juan", 2.1, "Departamental", 20, 20, 20));
        envios.add(new PaqueteFragil("F001", "Leo", "Inés", 3.2, "Urbano", 40, 30, 20, 1200));
        envios.add(new PaqueteFragil("F002", "Marta", "Luz", 1, "Departamental", 10, 10, 10, 500));
        envios.add(new CargaRefrigerada("R001", "Paz", "Omar", 2, "Urbano", -5));
        envios.add(new CargaRefrigerada("R002", "Paz", "Noé", 3, "Urbano", 4));
        registrarVehiculo(new Vehiculo("GT-101", 10, false));
        registrarVehiculo(new Vehiculo("GT-202", 25, true));
        registrarVehiculo(new Vehiculo("GT-303", 5, false));
    }

    public Envios cotizar(int categoria, String codigo, String remitente, String destinatario,
        double peso, String destino, double largo, double ancho, double alto,
        double valorDeclarado, double temperatura) {
        switch (categoria) {
            case 1:
                return new Sobre(codigo, remitente, destinatario, peso, destino);
            case 2:
                return new Paquete(codigo, remitente, destinatario, peso, destino, largo, ancho, alto);
            case 3:
                return new PaqueteFragil(codigo, remitente, destinatario, peso, destino,
                        largo, ancho, alto, valorDeclarado);
            case 4:
                return new CargaRefrigerada(codigo, remitente, destinatario, peso, destino, temperatura);
            default:
                throw new IllegalArgumentException("Categoría desconocida.");
        }
    }

    public Vehiculo buscarVehiculo(String placa){
        for (Vehiculo vehiculo: vehiculos){
            if (placa!= null && vehiculo.getPlaca().equalsIgnoreCase(placa.trim())){
                return  vehiculo;
            }
        }
        throw new IllegalArgumentException("No existe ese vehiculo");

    }

    public Despacho crearDespacho(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        for (Despacho despacho : despachos){
            if (despacho.getVehiculo() == vehiculo && !despacho.getEstado().equals("Cerrado")){
                throw new IllegalStateException("El vehículo ya tiene un despacho sin cerrar.");

            }
            
        }
        Despacho despacho = new Despacho(siguienteDespacho++, vehiculo);
        despachos.add(despacho);
        return despacho;
    }


    public Despacho buscarDespacho(int numero) {
        for (Despacho despacho : despachos){
            if (despacho.getNumero() == numero){
                return despacho;

            }

        }
        throw new IllegalArgumentException("No existe ese despacho.");
    }

    

    public void asignar(int numero, String codigo){
        buscarDespacho(numero).asignar(buscarEnvio(codigo));
    }

    public void retirar (int numero, String codigo){
        buscarDespacho(numero).retirar(buscarEnvio(codigo));
    }

    public void salir(int numero) {
        Despacho despacho = buscarDespacho(numero);
        int reintentos = despacho.salir();
        ingresosReintentos += reintentos * 15.0;
        for (Envios envio : despacho.getEnvios()){

            if (envio.getIntentosEntrega() > 0){
                sumarCategoria(envio, 15.0);

            }
        }
    }

    public void registrarResultado(int numero, String codigo, boolean entregado) {
        buscarDespacho(numero).registrarResultado(buscarEnvio(codigo), entregado);
    }

    public Envios buscarEnvio(String codigo) {
        for (Envios envio : envios){
            if (codigo != null && envio.getCodigo().equalsIgnoreCase(codigo.trim())){
                return envio;

            }
        }
            
        throw new IllegalArgumentException("No existe ese envío.");
    }

    public ArrayList<Envios> getEnvios() {
        return new ArrayList<>(envios);
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    public ArrayList<Despacho> getDespachos() {
        return new ArrayList<>(despachos);
    }

    public double getIngresosRegistro() {
        return ingresosRegistro;
    }

    public double getIngresosReintentos() {
        return ingresosReintentos;
    }

    public ArrayList<String> getCategorias() {
        ArrayList<String> categorias = new ArrayList<>();
        for (Envios envio : envios)
            if (!categorias.contains(envio.getCategoria()))
                categorias.add(envio.getCategoria());
        return categorias;
    }

    public int contar(String categoria, String estado) {
        int cantidad = 0;
        for (Envios envio : envios){
            if (envio.getCategoria().equals(categoria) && envio.getEstado().equals(estado)){
                cantidad++;
            }
        }
        return cantidad;
    }

    public double getIngresoCategoria(String categoria){
        for(int i = 0; i<categoriasIngreso.size();i++){
            if (categoriasIngreso.get(i).equals(categoria)) {
                return montosIngreso.get(i);
            }
        }
        return 0;
    }
}
