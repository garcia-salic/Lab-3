public class CargaRefrigerada extends Envios{
    private double temperatura; 
// luego de todo no era doble herencia aqui :v lo acepto me confundí

    public  CargaRefrigerada(String codigo, String remitente, String destinatario,double pesoReal,String destino, double temperatura){
        super(codigo, remitente,destinatario,pesoReal,destino);

        if(!destino.equalsIgnoreCase("Urbano")) {
            throw new IllegalArgumentException("Solo se permiten destinos urbanos");
        }

        if(temperatura < -25 || temperatura > 8) {
            throw new IllegalArgumentException("Temperatura fuera de rango");
        }

        this.temperatura = temperatura;
    }

    @Override
    public double calcularCosto() {
        return 25 + (pesoReal * 12) + 60;
    }

    @Override
    public int estimarTiempoEntrega() {
        return 1;
    }

    @Override
    public int obtenerMaximoIntentos() {
        return 1;
    }

    @Override
    public boolean requiereRefrigeracion() {
        return true; 
    }

}
