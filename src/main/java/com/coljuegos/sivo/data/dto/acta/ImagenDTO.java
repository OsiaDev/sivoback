package com.coljuegos.sivo.data.dto.acta;

import lombok.Data;

@Data
public class ImagenDTO {
    private String nombreImagen;
    private String imagenBase64;
    private String descripcion;
    private String fragmentOrigen;
}