package cajero.modelo.dao;

import java.util.List;

import cajero.modelo.entity.Cuenta;


public interface CuentaDao {
	Cuenta buscarUno(int idCuenta);
	List<Cuenta> todos();
	int update(Cuenta cuenta);
	int login(int idCuenta);

}
