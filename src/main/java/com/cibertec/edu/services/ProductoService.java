package com.cibertec.edu.services;

import java.io.IOException;
import java.io.InputStream;

import com.cibertec.edu.models.Producto;

import net.sf.jasperreports.engine.JRException;

public interface ProductoService {
    
    void agregarProducto(Producto p);

	/*byte[] generarConstanciaPdf(Producto producto) throws IOException, JRException;
	
	public InputStream getProductoReporte(String nombre, String descripcion) throws Exception;*/

	byte[] generarConstanciaPdf(String nombre, String descripcion) throws IOException, JRException;
}