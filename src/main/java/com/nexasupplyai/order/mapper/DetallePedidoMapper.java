package com.nexasupplyai.order.mapper;

import com.nexasupplyai.order.dto.request.DetallePedidoRequest;
import com.nexasupplyai.order.dto.response.DetallePedidoResponse;
import com.nexasupplyai.order.entity.DetallePedido;
import com.nexasupplyai.order.entity.Pedido;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DetallePedidoMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pedido", source = "pedido")
    @Mapping(target = "idProducto", source = "request.idProducto")
    @Mapping(target = "idAlmacen", source = "request.idAlmacen")
    @Mapping(target = "skuInstantanea", source = "request.skuInstantanea")
    @Mapping(target = "nombreProductoInstantanea", source = "request.nombreProductoInstantanea")
    @Mapping(target = "cantidad", source = "request.cantidad")
    @Mapping(target = "precioUnitario", source = "request.precioUnitario")
    @Mapping(target = "montoDescuento", source = "request.montoDescuento")
    DetallePedido aEntidad(DetallePedidoRequest request, Pedido pedido);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pedido", source = "pedido")
    @Mapping(target = "idProducto", source = "request.idProducto")
    @Mapping(target = "idAlmacen", source = "request.idAlmacen")
    @Mapping(target = "skuInstantanea", source = "request.skuInstantanea")
    @Mapping(target = "nombreProductoInstantanea", source = "request.nombreProductoInstantanea")
    @Mapping(target = "cantidad", source = "request.cantidad")
    @Mapping(target = "precioUnitario", source = "request.precioUnitario")
    @Mapping(target = "montoDescuento", source = "request.montoDescuento")
    void actualizarEntidad(DetallePedidoRequest request, Pedido pedido, @MappingTarget DetallePedido detalle);

    @Mapping(target = "idPedido", source = "pedido.id")
    DetallePedidoResponse aResponse(DetallePedido detalle);
}
