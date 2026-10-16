package com.nexasupplyai.order.mapper;

import com.nexasupplyai.order.dto.request.HistorialEstadoPedidoRequest;
import com.nexasupplyai.order.dto.response.HistorialEstadoPedidoResponse;
import com.nexasupplyai.order.entity.HistorialEstadoPedido;
import com.nexasupplyai.order.entity.Pedido;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HistorialEstadoPedidoMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pedido", source = "pedido")
    @Mapping(target = "estadoAnterior", source = "request.estadoAnterior")
    @Mapping(target = "nuevoEstado", source = "request.nuevoEstado")
    @Mapping(target = "motivo", source = "request.motivo")
    @Mapping(target = "cambiadoPor", source = "request.cambiadoPor")
    @Mapping(target = "cambiadoEn", source = "request.cambiadoEn")
    HistorialEstadoPedido aEntidad(HistorialEstadoPedidoRequest request, Pedido pedido);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pedido", source = "pedido")
    @Mapping(target = "estadoAnterior", source = "request.estadoAnterior")
    @Mapping(target = "nuevoEstado", source = "request.nuevoEstado")
    @Mapping(target = "motivo", source = "request.motivo")
    @Mapping(target = "cambiadoPor", source = "request.cambiadoPor")
    @Mapping(target = "cambiadoEn", source = "request.cambiadoEn")
    void actualizarEntidad(HistorialEstadoPedidoRequest request, Pedido pedido,
                           @MappingTarget HistorialEstadoPedido historial);

    @Mapping(target = "idPedido", source = "pedido.id")
    HistorialEstadoPedidoResponse aResponse(HistorialEstadoPedido historial);
}
