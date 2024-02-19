package cajero.modelo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cajero.modelo.entity.Cuenta;
import cajero.repository.CuentaRepository;

@Repository
public class CuentaDaoImplMy8Jpa implements CuentaDao {

	@Autowired
	private CuentaRepository crepo;

	@Override
	public Cuenta buscarUno(int idCuenta) {
		return crepo.findById(idCuenta).orElse(null);
	}

	@Override
	public List<Cuenta> todos() {
		return crepo.findAll();
	}

	@Override
	public int update(Cuenta cuenta) {
		try {
			Cuenta cuent = crepo.save(cuenta);
			if (cuent != null) {
				return 1;
			}
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	@Override
	public int login(int idCuenta) {

		Cuenta cuenta = crepo.login(idCuenta);
		if (cuenta != null) {
			return 1;
		} else {
			return 0;
		}

	}

}
