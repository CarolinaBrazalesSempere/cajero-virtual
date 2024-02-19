package cajero.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cajero.modelo.entity.Movimiento;



public interface MovimientoRepository extends JpaRepository<Movimiento,Integer> {
	
	@Query("SELECT m FROM Movimiento m where m.cuenta.idCuenta = ?1")
	 public List<Movimiento> findByCuenta(int idCuenta);
	
}


