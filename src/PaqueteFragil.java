public class PaqueteFragil extends Paquete {
    private double valorDeclarado;

    public PaqueteFragil(String codigo, String remitente, String destinatario,double pesoReal,String destino, double largo, double ancho,double alto,double valorDeclarado) {

        super(codigo, remitente, destinatario, pesoReal, destino,largo, ancho, alto);

        this.valorDeclarado = valorDeclarado;
    }


    @Override
    public double calcularCosto() {

        double costoBase = super.calcularCosto();

        return costoBase +15 +(valorDeclarado * 0.03);
    }


    @Override
    public int estimarTiempoEntrega() {

        return super.estimarTiempoEntrega() + 1;
    }

    @Override
    public int obtenerMaximoIntentos() {
        return 2;
    }
}