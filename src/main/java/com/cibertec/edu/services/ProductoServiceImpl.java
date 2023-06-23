package com.cibertec.edu.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.edu.models.Producto;
import com.cibertec.edu.repositories.ProductoRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void agregarProducto(Producto p) {
        productoRepository.save(p);
    }

    
    /*
    @Override
    public byte[] generarConstanciaPdf(Producto producto) throws IOException, JRException {
        // Load the JasperReport template
        InputStream templateStream = getClass().getResourceAsStream("/jasper/producto_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
        templateStream.close();

        // Prepare data source
        List<Producto> productos = new ArrayList<>();
        productos.add(producto); // Agrega solo el producto actual, en lugar de buscar todos los productos
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productos);

        // Prepare parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("productoDataSource", dataSource);

        // Fill the report with data
        InputStream reportStream = getClass().getResourceAsStream("/jasper/producto_report.jasper");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        reportStream.close();

        // Export the report to PDF
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return pdfBytes;
       
    }

	@Override
	public InputStream getProductoReporte(String nombre, String descripcion) throws Exception {
		try {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("PRODUCT_NAME", nombre);
			parameters.put("PRODUCT_DESCRIPTION", descripcion);
			JasperPrint jPrint = JasperFillManager.fillReport(getClass().getResourceAsStream("/jasper/producto_report.jasper"), parameters, new JREmptyDataSource());
			return new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jPrint));
			
		} catch (JRException e) {
			throw e;
		}
	}*/
    
    @Override
    public byte[] generarConstanciaPdf(String nombre, String descripcion) throws IOException, JRException {
        // Load the JasperReport template
        InputStream templateStream = getClass().getResourceAsStream("/jasper/producto_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
        templateStream.close();

        // Prepare data source
        List<Map<String, Object>> dataSource = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("PRODUCT_NAME", nombre);
        data.put("PRODUCT_DESCRIPTION", descripcion);
        dataSource.add(data);
        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dataSource);

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, beanDataSource);

        // Export the report to PDF
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return pdfBytes;
    }
}
