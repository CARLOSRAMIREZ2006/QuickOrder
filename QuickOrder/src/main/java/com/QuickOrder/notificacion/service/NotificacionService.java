package com.QuickOrder.notificacion.service;

import com.QuickOrder.notificacion.model.Notificacion;
import com.QuickOrder.notificacion.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);
    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Transactional(readOnly = true)
    public List<Notificacion> obtenerTodas() {
        return notificacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Notificacion> obtenerPorPedidoId(Long pedidoId) {
        return notificacionRepository.findByPedidoId(pedidoId);
    }

    @Transactional
    public Notificacion enviarNotificacion(Notificacion notificacion) {
        log.info("=================================================");
        log.info("SIMULANDO ENVÍO DE CORREO ELECTRÓNICO...");
        log.info("Para: {}", notificacion.getCorreoDestino());
        log.info("Asunto: Actualización de su Pedido #{}", notificacion.getPedidoId());
        log.info("Mensaje: {}", notificacion.getMensaje());
        log.info("=================================================");

        return notificacionRepository.save(notificacion);
    }
}