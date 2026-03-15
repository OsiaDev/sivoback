package com.coljuegos.sivo.data.dto.acta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadImagenActaDTO {

    private Integer numActa;
    private ImagenDTO imagen;

}
