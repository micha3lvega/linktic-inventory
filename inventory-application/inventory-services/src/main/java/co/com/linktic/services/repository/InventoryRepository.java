package co.com.linktic.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.linktic.services.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
