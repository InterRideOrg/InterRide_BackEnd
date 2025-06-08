package com.interride.dto.response;

public record NotificacionSimpleResponse (
        String mensaje,
        String fechaHora,
        Boolean leido
){

}
