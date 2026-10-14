public class Paquete extends Envios {
    protected double largo; 
    protected double ancho; 
    protected double alto; 

    public Paquete ( String codigo, String remitente,String destinatario, double pesoReal,String destino, double lar, double anch, double alt) {
        super(codigo, remitente, destinatario, pesoReal, destino);
        if (!Double.isFinite(lar) || !Double.isFinite(anch) || !Double.isFinite(alt) || lar <= 0 || anch <= 0 || alt <= 0) {
            throw new IllegalArgumentException("Las medidas del paquete deben ser mayores a cero" );
        }
        if (!Double.isFinite(lar * anch * alt / 5000.0))
            throw new IllegalArgumentException("Las dimensiones son demasiado grandes.");
        this.largo=lar;
        this.ancho=anch;
        this.alto=alt;

    }

    public double pesoVolumetrico() {
        return (largo * ancho * alto) / 5000.0;
    }

    public double pesoFacturable() {
        return Math.max(pesoReal, pesoVolumetrico());
    }

    @Override 
    public double calcularCosto() {
        double tarifaBase;
        if(destino.equalsIgnoreCase("Urbano"))
            tarifaBase = 25;
        else
            tarifaBase = 40;
        return tarifaBase + Math.ceil(pesoFacturable()) * 8;
    }

    @Override
    public int estimarTiempoEntrega() {

        if(destino.equalsIgnoreCase("Urbano"))
            return 2;

        return 4;
    }

    @Override
    public int obtenerMaximoIntentos() {
        return 3;
    }
}
