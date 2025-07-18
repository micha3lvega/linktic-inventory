package co.com.linktic.services.rest.client;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.linktic.services.entity.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

}
