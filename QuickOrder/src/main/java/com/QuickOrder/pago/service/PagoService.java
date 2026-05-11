package com.QuickOrder.pago.service;

import com.QuickOrder.pago.model.Pago;
import com.QuickOrder.pago.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final RestTemplate restTemplate;

    public PagoService(PagoRepository pagoRepository, RestTemplate restTemplate) {
        this.pagoRepository = pagoRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pago obtenerPorPedidoId(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado para el pedido: " + pedidoId));
    }

    @Transactional
    public Pago procesarPago(Pago pago) {
        String urlPedido = "http://localhost:8080/api/v1/pedidos/" + pago.getPedidoId();

        try {
            Map<String, Object> pedido = restTemplate.getForObject(urlPedido, Map.class);

            if (pedido != null && pedido.get("precioTotal") != null) {
                BigDecimal totalPedido = new BigDecimal(pedido.get("precioTotal").toString());

                if (pago.getMonto().compareTo(totalPedido) < 0) {
                    throw new RuntimeException("Error: El monto del pago es inferior al total del pedido (" + totalPedido + ")");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo validar el pedido. Verifique que el servicio de Pedidos esté activo.");
        }

        pago.setEstado("COMPLETADO");
        Pago pagoGuardado = pagoRepository.save(pago);

        try {
            String urlUpdateEstado = "http://localhost:8080/api/v1/pedidos/" + pago.getPedidoId() + "/estado?nuevoEstado=PAGADO";
            restTemplate.put(urlUpdateEstado, null);
        } catch (Exception e) {
            System.err.println("No se pudo actualizar el estado del pedido: " + e.getMessage());
        }

        return pagoGuardado;
    }
}