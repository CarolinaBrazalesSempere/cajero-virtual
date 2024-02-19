package cajero.modelo.dao;


import java.util.List;
import cajero.modelo.entity.Movimiento;



public interface MovimientoDao {
	Movimiento buscarUno(int idMovimiento);
	List<Movimiento> todos();
	List<Movimiento> findByCuenta(int idCuenta);
	Movimiento insertOne(Movimiento movimiento);
	
}
