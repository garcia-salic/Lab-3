import java.util.Scanner;

public class EnviosView {
    private static class FinDeEntrada extends RuntimeException {
    }

    private final EnviosControler controler;
    private final Scanner entrada;

    public EnviosView(EnviosControler controler) {
        this.controler = controler;
        this.entrada = new Scanner(System.in);
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n=== LAB3 - Envios  ===\n1. Cotizar y registrar envío\n2. Registrar vehículo" + "\n3. Crear despacho\n4. Asignar envío\n5. Retirar envío"
                    + "\n6. Salir despacho\n7. Registrar entrega o fallo"+ "\n8. Consultar envíos\n9. Consultar vehículos\n10. Consultar despachos" + "\n11. Reportes\n0. Salir");
            try {
                int opcion = entero("Opcion: ");
                switch (opcion) {
                    case 0:
                        System.out.println("Fin del programa");
                        return ;
                    case 1: 
                        cotizar();
                        break;
                    case 2: 
                        registrarVehiculo();
                        break;
                    case 3: 
                        crearDespacho();
                        break;
                    case 4: 
                        controler.asignar(entero("Numero de despacho: "), texto("Codigo: "));
                        System.out.println("Envio Asignado");
                        break;
                    case 5: 
                        controler.retirar(entero("Numero de despacho: "), texto("Codigo: "));
                        break;
                    case 6: 
                        controler.salir(entero("Numero de despacho: "));
                        break;
                    case 7: 
                        registrarResultado();
                        break;
                    case 8: 
                        mostrarEnvios();
                        break;
                    case 9: 
                        mostrarVehiculos();
                        break;
                    case 10: 
                        mostrarDespachos();
                        break;
                    case 11: 
                        reportes();
                        break;
                }
            } catch (FinDeEntrada e) {
                System.out.println("No hay mas datos de entrada");
                return ;
            }catch(IllegalArgumentException|IllegalStateException ex){
                System.out.println("Operacionm rechazada: "+ex.getMessage());
            }
        
        }
    }

    private boolean confirmar(String pregunta) {
        String respuesta = texto(pregunta);
        if (respuesta.equalsIgnoreCase("s")) {
            return true;
        }
        if (respuesta.equalsIgnoreCase("n")) {
            return false;
        }
        throw new IllegalArgumentException("Responda s o n.");
    }

    private String texto(String tex) {
        System.out.print(tex);
        if (!entrada.hasNextLine()) {
            throw new FinDeEntrada();
        }
        return entrada.nextLine().trim();
    }

    private int entero(String p) {
        try {
            return Integer.parseInt(texto(p));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Ingrese un número entero válido.");
        }
    }

    private double decimal(String pregunta) {
        try {
            double valor = Double.parseDouble(texto(pregunta).replace(',', '.'));
            if (!Double.isFinite(valor)){
                throw new NumberFormatException();
            }                
            return valor;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Ingrese un número decimal válido.");
        }
    }

    private void cotizar() {
        System.out.println("1. Sobre  2. Paquete  3. Paquete frágil  4. Carga refrigerada");
        int tipo = entero("Categoría: ");
        if (tipo < 1 || tipo > 4){
            throw new IllegalArgumentException("Categoría desconocida.");
        }            
        String codigo = texto("Código: ");
        String remitente = texto("Remitente: ");
        String destinatario = texto("Destinatario: ");
        double peso = decimal("Peso real (kg): ");
        String destino = texto("Destino (Urbano/Departamental): ");
        double largo = 0, ancho = 0, alto = 0, valor = 0, temperatura = 0;
        if(tipo == 2 || tipo == 3){
            largo=decimal("Largo (cm): ");
            ancho=decimal("Ancho (cm): ");
            alto=decimal("Alto (cm): ");
        }
        if(tipo==3){
            valor = decimal("Valor declarado: ");
        }
        if(tipo==4){
            temperatura = decimal("Temperatura: ");

        }
        Envios envio = controler.cotizar(tipo, codigo, remitente, destinatario, peso, destino, largo, ancho, alto, valor, temperatura);
        System.out.printf("Cotización: Q%.2f; entrega en %d día(s) hábil(es).%n",envio.calcularCosto(), envio.estimarTiempoEntrega());
        if(confirmar("Desea registrar y cobrar envios? (s/n): ")){
            controler.registrarEnvio(envio);
            System.out.println("Registrado y en bodega");
        }

    }

    
    
    private void registrarVehiculo(){
        Vehiculo vehiculo = new Vehiculo(texto("Placa: "), decimal("Capacidad (kg): "), confirmar("¿Tiene refrigeración? (s/n): "));
        controler.registrarVehiculo(vehiculo);
        System.out.println("Vehículo registrado.");
    }

    private void crearDespacho(){
        Despacho despacho = controler.crearDespacho(texto("Placa: "));
        System.out.println("Despacho "+ despacho.getNumero()+" Creadp cpm exito");
    }

    private void registrarResultado() {
        int numero = entero("Número de despacho: ");
        String codigo = texto("Código de envío: ");
        boolean entregado = confirmar("¿Se entregó? (s = entregado, n = fallido): ");
        controler.registrarResultado(numero, codigo, entregado);
        System.out.println("Resultado registrado; estado: " + controler.buscarEnvio(codigo).getEstado());
    }


    private void mostrarEnvios() {
        for (Envios envio : controler.getEnvios())
            System.out.printf("%s | %s | %s → %s | %s | %s | %.2f kg | "
                    + "Q%.2f | %d día(s) | intentos %d/%d%n", envio.getCodigo(), envio.getCategoria(),
                    envio.getRemitente(), envio.getDestinatario(), envio.getDestino(), envio.getEstado(),
                    envio.getPesoReal(), envio.calcularCosto(), envio.estimarTiempoEntrega(),
                    envio.getIntentosEntrega(), envio.obtenerMaximoIntentos());
    }

    private void mostrarVehiculos() {
        for (Vehiculo vehiculo : controler.getVehiculos())
            System.out.printf("%s | %.2f kg | refrigerado: %s%n", vehiculo.getPlaca(),
        vehiculo.getCapacidad(), vehiculo.isRefrigerado() ? "sí" : "no");
    }
    
    private void mostrarDespachos() {
        for (Despacho despacho : controler.getDespachos()) {
            System.out.printf("#%d | %s | %s | carga %.2f kg | disponible %.2f kg%n",
                    despacho.getNumero(), despacho.getVehiculo().getPlaca(), despacho.getEstado(),
                    despacho.getPesoCargado(), despacho.getCapacidadDisponible());
            for (Envios envio : despacho.getEnvios())
                System.out.println("  " + envio.getCodigo() + " | " + envio.getEstado());
        }
    }
    private void reportes() {
        System.out.println("Envíos por categoría y estado:");
        String[] estados = { "Bodega", "Asignado", "En ruta", "Entregado", "Devuelto al remitente" };
        for (String categoria : controler.getCategorias()) {
            System.out.println("  " + categoria + ":");
            for (String estado : estados) {
                int cantidad = controler.contar(categoria, estado);
                if (cantidad > 0)
                    System.out.println("    " + estado + ": " + cantidad);
            }
        }
        System.out.printf("Registro: Q%.2f | Reintentos: Q%.2f | Total: Q%.2f%n",controler.getIngresosRegistro(), controler.getIngresosReintentos(),controler.getIngresosRegistro() + controler.getIngresosReintentos());
        System.out.println("Ingreso por categoría:");
        for (String categoria : controler.getCategorias()){
            System.out.printf("  %s: Q%.2f%n", categoria, controler.getIngresoCategoria(categoria));

        }
        System.out.println("Despachos sin cerrar:");
        for (Despacho despacho : controler.getDespachos()){
            if (!despacho.getEstado().equals("Cerrado")){
                System.out.printf("  #%d | %s | %s | %.2f kg cargados | %.2f kg disponibles%n", despacho.getNumero(), despacho.getVehiculo().getPlaca(), despacho.getEstado(), despacho.getPesoCargado(), despacho.getCapacidadDisponible());

            }

        }
    }
}