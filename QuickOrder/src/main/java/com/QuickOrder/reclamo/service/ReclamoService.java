package com.QuickOrder.reclamo.service;

import com.QuickOrder.reclamo.model.Reclamo;
import com.QuickOrder.reclamo.repository.ReclamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final RestTemplate restTemplate;

    public ReclamoService(ReclamoRepository reclamoRepository, RestTemplate restTemplate) {
        this.reclamoRepository = reclamoRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<Reclamo> obtenerTodos() {
        return reclamoRepository.findAll();
    }

    @Transactional
    public Reclamo crearReclamo(Reclamo reclamo) {
        String urlPedido = "http://localhost:8080/api/v1/pedidos/" + reclamo.getPedidoId();

        try {
            restTemplate.getForObject(urlPedido, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("No se puede registrar el reclamo: El pedido ID " + reclamo.getPedidoId() + " no existe.");
        }

        reclamo.setEstado("INGRESADO");
        return reclamoRepository.save(reclamo);
    }

    @Transactional
    public Reclamo resolverReclamo(Long id, String respuesta, String nuevoEstado) {
        Reclamo reclamo = reclamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));

        reclamo.setRespuesta(respuesta);
        reclamo.setEstado(nuevoEstado);

        return reclamoRepository.save(reclamo);
    }
}