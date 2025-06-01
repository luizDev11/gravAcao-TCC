package com.recorder.controller.entity;

import com.recorder.controller.entity.enuns.TipoMidia;
import java.time.LocalDateTime;

public class Galeria {

        private Integer id;
        private String midiaUrl; // URL da foto/vídeo
        private TipoMidia tipo;  // Enum para Foto ou Vídeo
        private Integer profissionalId; // ID do profissional
        private LocalDateTime dataPostagem;
}
