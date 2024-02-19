package cajero.modelo.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cajero.modelo.entity.Movimiento;
import cajero.repository.MovimientoRepository;

@Repository
public class MovimientoDaoImplMy8Jpa implements MovimientoDao {
	
	@Autowired
	private MovimientoRepository movimientoRepo;

	@Override
	public Movimiento buscarUno(int idMovimiento) {
		
		return movimientoRepo.findById(idMovimiento).orElse(null);
	}

	@Override
	public List<Movimiento> todos() {
		
		return movimientoRepo.findAll();
	}

	@Override
	public Movimiento insertOne(Movimiento movimiento) {
		Movimiento mov = movimientoRepo.save(movimiento);
		return mov;
	}


	@Override
	public List<Movimiento> findByCuenta(int idCuenta) {
		
		return movimientoRepo.findByCuenta(idCuenta);
	}

	

	
	

}
