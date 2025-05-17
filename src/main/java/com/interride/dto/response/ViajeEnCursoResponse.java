package com.interride.dto.response;

import com.interride.model.enums.EstadoViaje;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeEnCursoResponse {
    /*
    * Viaje id
    * Nombre del conductor
    * Apellido del conductor
    * Modelo del vehiculo
    * Placa del vehiculo
    * Marca del vehiculo
    * cantidad de asientos
    * asientos ocupados
    * origen longitud
    * origen latitud
    * origen provincia
    * destino longitud
    * destino latitud
    * destino provincia
    * estado del viaje
    * fecha y hora de salida
    * */

    private Integer id;
    private String nombreConductor;
    private String apellidoConductor;
    private String modeloVehiculo;
    private String placaVehiculo;
    private String marcaVehiculo;
    private Integer cantidadAsientos;
    private Integer asientosOcupados;
    private Double origenLongitud;
    private Double origenLatitud;
    private String origenProvincia;
    private Double destinoLongitud;
    private Double destinoLatitud;
    private String destinoProvincia;
    private EstadoViaje estadoViaje;
    private LocalDateTime fecha_hora_partida;
}
