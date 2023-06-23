package com.cibertec.edu.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.edu.models.Producto;

@Repository
public interface ProductoRepository extends CrudRepository<Producto, Long> {

	Producto findTopByOrderByFechaRegistroDesc();

	Optional<Producto> findFirstByOrderByFechaRegistroDesc();
}