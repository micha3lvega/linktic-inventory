package co.com.linktic.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.linktic.services.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
