package com.cibertec.edu.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.edu.models.Producto;
import com.cibertec.edu.repositories.ProductoRepository;
import com.cibertec.edu.services.ProductoService;

import net.sf.jasperreports.engine.JRException;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ProductoRepository productoRepository;

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
            return "redirect:/"; // Redirect to the homepage or another generic page
        }
    }

    /*
    @PostMapping("/producto/registrar")
    public ResponseEntity<ByteArrayResource> registrarProducto(
            @Validated @ModelAttribute("producto") Producto producto, 
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            boolean generarPdf,
            BindingResult bindingResult) throws IOException, JRException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            if (bindingResult.hasErrors()) {
                // Handle validation errors
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            try {
                // Register the product in the database
                producto.setFechaRegistro(fecha);
                productoService.agregarProducto(producto);

                if (generarPdf) {
                    // Generate the PDF file for the certificate
                    byte[] pdfBytes = productoService.generarConstanciaPdf(producto);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", "constancia.pdf");
                    ByteArrayResource resource = new ByteArrayResource(pdfBytes);

                    return new ResponseEntity<>(resource, headers, HttpStatus.OK);
                }

                return ResponseEntity.ok().build();

            } catch (DataIntegrityViolationException e) {
                // Handle data integrity errors
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
            // Get the last product registered
            Producto producto = productoRepository.findTopByOrderByFechaRegistroDesc();
            
            if (producto != null) {
                // Generate the PDF file for the certificate
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
    }*/
    
    @PostMapping("/producto/registrar")
    public ResponseEntity<ByteArrayResource> registrarProducto(
            @Validated @ModelAttribute("producto") Producto producto,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            @RequestParam(defaultValue = "false") boolean generarPdf,
            BindingResult bindingResult) throws IOException, JRException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            if (bindingResult.hasErrors()) {
                // Handle validation errors
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            try {
                // Register the product in the database
                producto.setFechaRegistro(fecha);
                productoService.agregarProducto(producto);

                if (generarPdf) {
                    // Generate the PDF file for the certificate
                    byte[] pdfBytes = productoService.generarConstanciaPdf(producto.getNombre(), producto.getDescripcion());

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", "constancia.pdf");
                    ByteArrayResource resource = new ByteArrayResource(pdfBytes);

                    return new ResponseEntity<>(resource, headers, HttpStatus.OK);
                }

                return ResponseEntity.ok().build();

            } catch (DataIntegrityViolationException e) {
                // Handle data integrity errors
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    /*
    @GetMapping("/CrearConstancia")
    public ResponseEntity<ByteArrayResource> generarConstanciaUltimo() throws IOException, JRException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication.isAuthenticated()) {
            // Get the last product registered
            Producto producto = productoRepository.findTopByOrderByFechaRegistroDesc();
            
            if (producto != null) {
                // Generate the PDF file for the certificate
                byte[] pdfBytes = productoService.generarConstanciaPdf(producto.getNombre(), producto.getDescripcion());
                
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
    */
    @GetMapping("/CrearConstancia")
    public ResponseEntity<ByteArrayResource> generarConstanciaUltimo() throws IOException, JRException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication.isAuthenticated()) {
            // Get the last product registered
            Optional<Producto> productoOptional = productoRepository.findFirstByOrderByFechaRegistroDesc();
            
            if (productoOptional.isPresent()) {
                Producto producto = productoOptional.get();
                
                // Generate the PDF file for the certificate
                byte[] pdfBytes = productoService.generarConstanciaPdf(producto.getNombre(), producto.getDescripcion());
                
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