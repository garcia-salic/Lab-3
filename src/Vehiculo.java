public class Vehiculo {
    private final String placa;
    private  final double capacidad; 
    private final boolean refrigerado; 

    public Vehiculo (String placa, double capacidad, boolean refrigerado){
        if(placa == null || placa.trim().isEmpty()){
            throw new IllegalArgumentException ("La placa debe determinarse");
        }
        if(!Double.isFinite(capacidad) || capacidad <= 0){
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        this.placa=placa;
        this.capacidad=capacidad;
        this.refrigerado = refrigerado;
    }

    public String getPlaca (){
        return  placa;
    }

    public double getCapacidad(){
        return capacidad;
    }

    public boolean isRefrigerado(){
        return refrigerado;
    }
}

