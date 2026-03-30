package com.coljuegos.sivo.service.notificacion;

public interface ReenvioCorreoService {
    void reenviarCorreoPost(Integer numActa, boolean fromEndpoint);
}
