package com.nexasupplyai.order.mapper;

import com.nexasupplyai.order.dto.response.HistorialEstadoPedidoResponse;
import com.nexasupplyai.order.entity.HistorialEstadoPedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistorialEstadoPedidoMapper {

    @Mapping(target = "idPedido", source = "pedido.id")
    HistorialEstadoPedidoResponse aResponse(HistorialEstadoPedido historial);
}
