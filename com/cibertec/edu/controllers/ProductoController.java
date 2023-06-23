package com.cibertec.edu.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().contentEquals("ROLE_ADMIN"))) {
			return "producto_registro";
		} else {
			return "redirect:/"; // Redireccionar a la página de inicio u otra página genérica
		}
	}

	@PostMapping({ "/producto/registrar" })
	public ResponseEntity<ByteArrayResource> registrarProducto(
			@Validated @ModelAttribute("producto") Producto producto, boolean generarPdf,
			BindingResult bindingResult) throws IOException, JRException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().contentEquals("ROLE_ADMIN"))) {
			if (bindingResult.hasErrors()) {
				return getRecursoHtmlComoResponseEntity("/templates/producto_registro.html");
			}

			productoService.agregarProducto(producto);

			if (generarPdf) {
				InputStream pdfStream;
				try {
					pdfStream = productoService.getReporteProducto(producto.getNombre(), producto.getDescripcion());
					byte[] data = pdfStream.readAllBytes();
					pdfStream.close();

					HttpHeaders headers = new HttpHeaders();
					headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=constancia_producto.pdf");
					headers.setContentType(MediaType.APPLICATION_PDF);
					headers.setContentLength(data.length);

					// Devolver el contenido del PDF como respuesta
					return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(data));
				} catch (Exception e) {
					// Manejar la excepción y devolver una respuesta de error
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
			}
			return getRecursoHtmlComoResponseEntity("/templates/producto_registro.html");
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	
	private ResponseEntity<ByteArrayResource> getRecursoHtmlComoResponseEntity(String rutaHtml) throws IOException {
		InputStream htmlStream;
		try {
			htmlStream = getClass().getResourceAsStream(rutaHtml);
			byte[] data = htmlStream.readAllBytes();
			htmlStream.close();
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=ejemplo.html");
			headers.setContentType(MediaType.TEXT_HTML);
			headers.setContentLength(data.length);

			// Devolver el contenido HTML como respuesta
			return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(data));
		} catch (IOException e) {
			// Manejar la excepción y devolver una respuesta de error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
