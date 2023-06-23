package com.cibertec.edu.services;

import java.io.InputStream;

import com.cibertec.edu.models.Producto;

public interface ProductoService {
	
	public void agregarProducto(Producto p);
	
	public InputStream getReporteProducto(String nombre, String descripcion) throws Exception;
}