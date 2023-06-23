package com.cibertec.edu.controllers;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cibertec.edu.models.Producto;
import com.cibertec.edu.services.ProductoService;

import net.sf.jasperreports.engine.JRException;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({ "/", "/producto/registrar" })
    public String registrarProducto(@ModelAttribute("producto") Producto producto, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "producto_registro";
        } else {
            return "redirect:/"; // Redireccionar a la página de inicio u otra página genérica
        }
    }

    @PostMapping("/producto/registrar")
    public ResponseEntity<ByteArrayResource> registrarProducto(
            @Validated @ModelAttribute("producto") Producto producto, 
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            boolean generarPdf,
            BindingResult bindingResult) throws IOException, JRException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            if (bindingResult.hasErrors()) {
                // Manejar errores de validación
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            try {
                // Registrar el producto en la base de datos
                producto.setFechaRegistro(fecha);
                productoService.agregarProducto(producto);

                if (generarPdf) {
                    // Generar el archivo PDF de la constancia
                    byte[] pdfBytes = productoService.generarConstanciaPdf(producto);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", "constancia.pdf");
                    ByteArrayResource resource = new ByteArrayResource(pdfBytes);

                    return new ResponseEntity<>(resource, headers, HttpStatus.OK);
                }

                return ResponseEntity.ok().build();

            } catch (DataIntegrityViolationException e) {
                // Manejar errores de integridad de datos
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @GetMapping("/CrearConstancia")
    public ResponseEntity<ByteArrayResource> generarConstanciaUltimo() throws IOException, JRException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication.isAuthenticated()) {
            // Obtener el último producto registrado por el usuario autenticado
            String username = authentication.getName();
            Producto producto = productoService.obtenerUltimoProductoPorUsuario(username);
            
            if (producto != null) {
                // Generar el archivo PDF de la constancia
                byte[] pdfBytes = productoService.generarConstanciaPdf(producto);
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", "constancia.pdf");
                ByteArrayResource resource = new ByteArrayResource(pdfBytes);
                
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
