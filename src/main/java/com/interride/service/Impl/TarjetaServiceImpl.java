package com.interride.service.Impl;

import com.interride.dto.request.TarjetaRequest;
import com.interride.dto.response.TarjetaConductorResponse;
import com.interride.dto.response.TarjetaPasajeroResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.TarjetaMapper;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Tarjeta;
import com.interride.repository.ConductorRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.TarjetaRepository;
import com.interride.service.TarjetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Console;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarjetaServiceImpl implements TarjetaService {
    private final TarjetaRepository tarjetaRepository;
    private final PasajeroRepository pasajeroRepository;
    private final ConductorRepository conductorRepository;
    private final TarjetaMapper tarjetaMapper;

    @Override
    @Transactional
    public TarjetaPasajeroResponse createTarjetaPasajero(Integer idPasajero, TarjetaRequest request) {
        Pasajero pasajero = pasajeroRepository.findById(idPasajero)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado."));

        List<Tarjeta> tarjetasExistentes = tarjetaRepository.findByPasajeroId(pasajero.getId());

        if (tarjetasExistentes.stream()
                .map(Tarjeta::getNumeroTarjeta)
                .toList()
                .contains(request.numeroTarjeta())) {
            throw new BusinessRuleException("Esta tarjeta ya esta registrada.");
        }

        Tarjeta tarjeta = tarjetaMapper.toEntityPasajero(request);
        tarjeta.setSaldo(70.0); // Asignar un saldo inicial de 70.0

        tarjeta.setPasajero(pasajero);

        Tarjeta savedTarjeta = tarjetaRepository.save(tarjeta);

        return  tarjetaMapper.toPasajeroResponse(savedTarjeta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarjetaPasajeroResponse> getTarjetasPasajero(Integer idPasajero) {
        Pasajero pasajero = pasajeroRepository.findById(idPasajero)
                .orElseThrow(() -> new IllegalArgumentException("Pasajero no encontrado."));

        List<Tarjeta> tarjetas = tarjetaRepository.findByPasajeroId(pasajero.getId());

        return tarjetas.stream()
                .map(tarjetaMapper::toPasajeroResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Integer idTarjeta) {
        Tarjeta tarjeta = tarjetaRepository.findById(idTarjeta)
                .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada."));

        tarjetaRepository.delete(tarjeta);
    }

    @Override
    @Transactional
    public TarjetaConductorResponse createTarjetaConductor(Integer idConductor, TarjetaRequest request) {
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado."));

        Tarjeta tarjetasExistente = tarjetaRepository.findByConductorId(conductor.getId());

        if(tarjetasExistente != null) {
            throw new BusinessRuleException("El conductor ya tiene una tarjeta registrada.");
        }

        Tarjeta tarjeta = tarjetaMapper.toEntityConductor(request);
        tarjeta.setConductor(conductor);

        Tarjeta savedTarjeta = tarjetaRepository.save(tarjeta);

        return tarjetaMapper.toConductorResponse(savedTarjeta);
    }

    @Override
    @Transactional
    public TarjetaConductorResponse getTarjetaConductorById(Integer idConductor) {
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado."));

        Tarjeta tarjeta = tarjetaRepository.findByConductorId(conductor.getId());

        if (tarjeta == null) {
            throw new ResourceNotFoundException("No tiene tarjeta registrada.");
        }

        return tarjetaMapper.toConductorResponse(tarjeta);
    }
}

