public class Sobre extends Envios {
    public Sobre(String codigo, String remitente,String destinatario, double pesoReal,String destino) {

        super(codigo, remitente, destinatario, pesoReal, destino);

        if (pesoReal <= 0 || pesoReal > 0.5) {
            throw new IllegalArgumentException(
                "El peso del sobre debe ser mayor que 0 y máximo 0.5 kg."
            );
        }

        if (!destino.equalsIgnoreCase("Urbano") && !destino.equalsIgnoreCase("Departamental")) {
            throw new IllegalArgumentException("El destino debe ser Urbano o Departamental.");
        }
    }

    @Override 
    public  double calcularCosto(){
        if (destino.equalsIgnoreCase("Urbano")){
            return 20; 
        }
        return 35;
    };

    @Override
    public int estimarTiempoEntrega(){
        if (destino.equalsIgnoreCase("Urbano")){
            return 1; 
        }
        return 2;
    };

    @Override
    public int obtenerMaximoIntentos(){
        return 3;
    };
}
