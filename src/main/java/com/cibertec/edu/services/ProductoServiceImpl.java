package com.cibertec.edu.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.edu.models.Producto;
import com.cibertec.edu.repositories.ProductoRepository;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepo;

    @Override
    public void agregarProducto(Producto p) {
        productoRepo.save(p);
    }

    @Override
    public byte[] generarConstanciaPdf(Producto producto) throws IOException, JRException {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("NOMBRE_PRODUCTO", producto.getNombre());
        parametros.put("DESCRIPCION_PRODUCTO", producto.getDescripcion());
        JasperPrint jPrint = JasperFillManager.fillReport(getClass().getResourceAsStream("/jasper/producto_report.jasper"), parametros, new JREmptyDataSource());
        return JasperExportManager.exportReportToPdf(jPrint);
    }

    @Override
    public Producto obtenerUltimoProductoPorUsuario(String username) {
        return productoRepo.findTopByUsuario_UsernameOrderByFechaRegistroDesc(username);
    }
}