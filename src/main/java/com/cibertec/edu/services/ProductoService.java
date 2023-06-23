package com.cibertec.edu.services;

import java.io.IOException;
import java.io.InputStream;

import com.cibertec.edu.models.Producto;

import net.sf.jasperreports.engine.JRException;

public interface ProductoService {
    
    public void agregarProducto(Producto p);
    
    public byte[] generarConstanciaPdf(Producto producto) throws IOException, JRException;
    
    public Producto obtenerUltimoProductoPorUsuario(String username);
}