package cajero.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cajero.modelo.dao.CuentaDao;

import cajero.modelo.entity.Cuenta;

/*test para las conexiones*/
@RestController
public class CuentaRestController {

	@Autowired
	private CuentaDao cdao;
	
	@GetMapping("/todos")
	public List<Cuenta> todos(){
		return cdao.todos();
		
	}
	
	@GetMapping("/una/{idProducto}")
	public Cuenta findOne(@PathVariable int idCuenta) {
		Cuenta cuenta = cdao.buscarUno(idCuenta);
		return cuenta;
	}

	
	
	

}
