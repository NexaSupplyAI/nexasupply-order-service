package com.nexasupplyai.order.mapper;

import com.nexasupplyai.order.dto.request.PedidoRequest;
import com.nexasupplyai.order.dto.response.PedidoResponse;
import com.nexasupplyai.order.entity.Pedido;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PedidoMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "numeroPedido", source = "request.numeroPedido")
    @Mapping(target = "idClienteExterno", source = "request.idClienteExterno")
    @Mapping(target = "nombreCliente", source = "request.nombreCliente")
    @Mapping(target = "emailCliente", source = "request.emailCliente")
    @Mapping(target = "telefonoCliente", source = "request.telefonoCliente")
    @Mapping(target = "estado", source = "request.estado", defaultValue = "PENDING")
    @Mapping(target = "moneda", source = "request.moneda")
    @Mapping(target = "subtotal", source = "request.subtotal")
    @Mapping(target = "montoImpuesto", source = "request.montoImpuesto")
    @Mapping(target = "montoDescuento", source = "request.montoDescuento")
    @Mapping(target = "montoEnvio", source = "request.montoEnvio")
    @Mapping(target = "montoTotal", source = "request.montoTotal")
    @Mapping(target = "notas", source = "request.notas")
    @Mapping(target = "solicitadoEn", source = "request.solicitadoEn")
    @Mapping(target = "confirmadoEn", source = "request.confirmadoEn")
    @Mapping(target = "completadoEn", source = "request.completadoEn")
    @Mapping(target = "canceladoEn", source = "request.canceladoEn")
    @Mapping(target = "creadoPor", source = "request.creadoPor")
    Pedido aEntidad(PedidoRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "numeroPedido", source = "request.numeroPedido")
    @Mapping(target = "idClienteExterno", source = "request.idClienteExterno")
    @Mapping(target = "nombreCliente", source = "request.nombreCliente")
    @Mapping(target = "emailCliente", source = "request.emailCliente")
    @Mapping(target = "telefonoCliente", source = "request.telefonoCliente")
    @Mapping(target = "estado", source = "request.estado")
    @Mapping(target = "moneda", source = "request.moneda")
    @Mapping(target = "subtotal", source = "request.subtotal")
    @Mapping(target = "montoImpuesto", source = "request.montoImpuesto")
    @Mapping(target = "montoDescuento", source = "request.montoDescuento")
    @Mapping(target = "montoEnvio", source = "request.montoEnvio")
    @Mapping(target = "montoTotal", source = "request.montoTotal")
    @Mapping(target = "notas", source = "request.notas")
    @Mapping(target = "solicitadoEn", source = "request.solicitadoEn")
    @Mapping(target = "confirmadoEn", source = "request.confirmadoEn")
    @Mapping(target = "completadoEn", source = "request.completadoEn")
    @Mapping(target = "canceladoEn", source = "request.canceladoEn")
    @Mapping(target = "creadoPor", source = "request.creadoPor")
    void actualizarEntidad(PedidoRequest request, @MappingTarget Pedido pedido);

    PedidoResponse aResponse(Pedido pedido);
}
