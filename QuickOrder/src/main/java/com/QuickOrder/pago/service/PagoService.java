package com.QuickOrder.pago.service;

import com.QuickOrder.pago.model.Pago;
import com.QuickOrder.pago.repository.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);
    private final PagoRepository pagoRepository;
    private final RestTemplate restTemplate;

    private final String PEDIDOS_API_URL = "http://localhost:8080/api/v1/pedidos/";

    public PagoService(PagoRepository pagoRepository, RestTemplate restTemplate) {
        this.pagoRepository = pagoRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    @Transactional
    public Pago procesarPago(Pago pago) {
        log.info("Procesando pago para el pedido ID: {}", pago.getPedidoId());

        try {
            java.util.Map pedidoData = restTemplate.getForObject(PEDIDOS_API_URL + pago.getPedidoId(), java.util.Map.class);
            if (pedidoData != null && pedidoData.get("total") != null) {
                java.math.BigDecimal totalPedido = new java.math.BigDecimal(pedidoData.get("total").toString());

                if (pago.getMonto().compareTo(totalPedido) < 0) {
                    throw new RuntimeException("El monto del pago (" + pago.getMonto() + ") no puede ser inferior al total del pedido (" + totalPedido + ")");
                }
            }
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo validar el total del pedido. Verifique si el pedido ID existe.");
        }

        Pago pagoGuardado = pagoRepository.save(pago);

        try {
            String url = PEDIDOS_API_URL + pago.getPedidoId() + "/estado?nuevoEstado=PAGADO";
            restTemplate.put(url, null);
            log.info("Pedido ID: {} actualizado a estado PAGADO", pago.getPedidoId());
        } catch (Exception e) {
            log.error("No se pudo actualizar el estado del pedido: {}", e.getMessage());
        }

        return pagoGuardado;
    }
}
