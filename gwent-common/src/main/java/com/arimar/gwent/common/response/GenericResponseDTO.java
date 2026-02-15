package com.arimar.gwent.common.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
public class GenericResponseDTO<T> {

    private String serviceOrigin;
    private Integer status;
    private T data;

    public GenericResponseDTO(String serviceProcedencia, Integer status, T data) {
        this.serviceOrigin = serviceProcedencia;
        this.status = status;
        this.data = data;
    }

}
