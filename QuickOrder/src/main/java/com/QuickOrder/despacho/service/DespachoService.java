package com.QuickOrder.despacho.service;

import com.QuickOrder.despacho.model.Despacho;
import com.QuickOrder.despacho.repository.DespachoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class DespachoService {

    private final DespachoRepository despachoRepository;
    private final RestTemplate restTemplate;

    public DespachoService(DespachoRepository despachoRepository, RestTemplate restTemplate) {
        this.despachoRepository = despachoRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<Despacho> obtenerTodos() {
        return despachoRepository.findAll();
    }

    @Transactional
    public Despacho crearDespacho(Despacho despacho) {
        String urlPago = "http://localhost:8080/api/v1/pagos/pedido/" + despacho.getPedidoId();

        try {
            Map<String, Object> pago = restTemplate.getForObject(urlPago, Map.class);

            if (pago == null || !"COMPLETADO".equals(pago.get("estado"))) {
                throw new RuntimeException("No se puede generar despacho: El pago no ha sido aprobado o no existe.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al validar el pago: " + e.getMessage());
        }

        despacho.setEstado("PREPARANDO");
        return despachoRepository.save(despacho);
    }
}