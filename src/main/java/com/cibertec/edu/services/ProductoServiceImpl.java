package com.cibertec.edu.services;

import java.io.ByteArrayInputStream;
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
public class ProductoServiceImpl implements ProductoService{

	@Autowired
	private ProductoRepository productoRepo;
	
	@Override
	public void agregarProducto(Producto p) {
		productoRepo.save(p);
		
	}

	@Override
	public InputStream getReporteProducto(String nombre, String descripcion) throws JRException {
		try {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("NOMBRE_PRODUCTO", nombre);
			parametros.put("DESCRIPCION_PRODUCTO", descripcion);
			JasperPrint jPrint = JasperFillManager.fillReport(getClass().getResourceAsStream("/jasper/reporte_producto.jasper"), parametros, new JREmptyDataSource());
			return new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jPrint));
			
		} catch (JRException e) {
			throw e;
		}
	}

}