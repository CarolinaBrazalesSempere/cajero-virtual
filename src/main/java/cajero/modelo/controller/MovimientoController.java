package cajero.modelo.controller;

import java.util.Date;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cajero.modelo.dao.CuentaDao;
import cajero.modelo.dao.MovimientoDao;
import cajero.modelo.entity.Cuenta;
import cajero.modelo.entity.Movimiento;
import jakarta.servlet.http.HttpSession;

@Controller
public class MovimientoController {
	
	@Autowired
	private CuentaDao cdao;
	
	@Autowired
	private MovimientoDao mdao;
	
	@GetMapping("/ingresar")
	public String showIngresar(HttpSession sesion, Model model) {
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuenta"); //recupero la cuenta de la sesión actual
	    model.addAttribute("cuenta", cuenta);
		return "ingresar";
	}
	
	@PostMapping("/ingresar")
	public String hacerIngreso(HttpSession sesion, @RequestParam double cantidad, RedirectAttributes ratt) {
		
	    Cuenta cuenta = (Cuenta)sesion.getAttribute("cuenta");
	  
	    
	    //creo una instancia del movimiento 'ingresar', 
	    //con los setters lo reflejo en la tabla de la web y 
	    //con el update en la BBDD pasándole por parámetro el movimiento que se ha producido en esta sesión
	    Movimiento mov = new Movimiento();
	    mov.setCuenta(cuenta);
		mov.setFecha(new Date());
	    mov.setCantidad(cantidad);
	    mov.setOperacion("ingresar");
	    mdao.insertOne(mov);
	    
	    //ingresar el saldo en la cuenta de la sesión actual y actualizar la BBDD 
	    cuenta.ingresar(cantidad);
	    cdao.update(cuenta);
	    
	    
	    sesion.setAttribute("cuenta", cuenta);   
	    ratt.addFlashAttribute("mensaje", "Has ingresado: " + cantidad + "€");
	    
	    return "redirect:/ingresar";
		
		
	}
	
	@GetMapping("/extraer")
	public String showExtraer(HttpSession sesion, Model model) {
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuenta");
	    model.addAttribute("cuenta", cuenta);
		return "extraer";
	}
	
	@PostMapping("/extraer")
	public String extraerDinero(HttpSession sesion, @RequestParam double cantidad, RedirectAttributes ratt) {
		
	    Cuenta cuenta = (Cuenta)sesion.getAttribute("cuenta");
	    
	    //comprobamos que la cuenta de la sesión actual tiene saldo
	    if(cuenta.getSaldo() >= cantidad) {
	    //creo una instancia del movimiento  que será para 'extraer', 
		//con los setters lo reflejo en la tabla de la web y 
		//con el update en la BBDD pasándole por parámetro el movimientode extracción
	    Movimiento mov = new Movimiento();
	    mov.setCuenta(cuenta);
		mov.setFecha(new Date());
	    mov.setCantidad(cantidad);
	    mov.setOperacion("extraer");
	    mdao.insertOne(mov);
	    
	    //extraer el saldo en la cuenta de la sesión actual y actualizar la BBDD 
	    cuenta.extraer(cantidad);
	    cdao.update(cuenta);
	    
	    //para reflejar los datos actualizados en la cuenta de la sesión actual
	    sesion.setAttribute("cuenta", cuenta);
	    
	    ratt.addFlashAttribute("mensaje", "Has sacado " +  cantidad + "€ de la cuenta");
	    
	    return "redirect:/extraer";
	    
	    } else {
	    	ratt.addFlashAttribute("mensaje", "No tienes  " +  cantidad + "€ para sacar de la cuenta");
	    	 return "redirect:/extraer";
	    }
		
		
	}
	
	@GetMapping("/transferencia")
	public String showTransferir(HttpSession sesion, Model model){
		Cuenta cuenta = (Cuenta) sesion.getAttribute("cuenta");
		model.addAttribute("cuenta", cuenta);
		return "transferencia";
	}
	
	
	@PostMapping("/transferencia")
	public String transferirDinero(HttpSession sesion, @RequestParam int idCuenta, @RequestParam double cantidad, RedirectAttributes ratt) {
	    // Capturar la cuenta de la sesión actual
	    Cuenta cuenta = (Cuenta) sesion.getAttribute("cuenta");
	    
	    // Buscar la cuenta correspondiente al ID proporcionado
	    Cuenta cuentaParam = cdao.buscarUno(idCuenta);
	    
	    // Verificar si la cuenta destino existe
	    if (cuentaParam != null) {
	        // Comprobar si hay saldo suficiente en la cuenta de origen
	        if (cuenta.getSaldo() > cantidad) {
	        	 //transfiero el dinero a la cuenta de destino, la que me llega por parámetro
	            cuenta.transferir(cuentaParam, cantidad);
	            
	    		//creo una una nueva instancia de movimiento para ingresar y con los setters actualizo la tabla, con insertOne la BBDD
	            Movimiento movIng = new Movimiento();
	            movIng.setCuenta(cuentaParam);
	            movIng.setFecha(new Date());
	            movIng.setCantidad(cantidad);
	            movIng.setOperacion("ingreso por transferencia");
	            mdao.insertOne(movIng);
	            
	            //crear otra instancia de movimiento para la extracción
	            Movimiento movExtr = new Movimiento();
	            movExtr.setCuenta(cuenta);
	            movExtr.setFecha(new Date());
	            movExtr.setCantidad(cantidad);
	            movExtr.setOperacion("extracción por transferencia");
	            mdao.insertOne(movExtr);
	            
	            //actualizar la base de datos de ambas cuentas, la de sesión y la que me llega por parámetro
	            cdao.update(cuentaParam);
	            cdao.update(cuenta);
	            
	            //actualizar los datos de la cuenta en la sesión actual
	            sesion.setAttribute("cuenta", cuenta);
	            
	            //redireccionar con un mensaje de éxito
	            ratt.addFlashAttribute("mensaje", "Has transferido " + cantidad + "€ a la cuenta " + cuentaParam.getIdCuenta());
	            return "redirect:/transferencia";
	        } else {
	            //saldo insuficiente para realizar la transferencia
	            ratt.addFlashAttribute("mensaje", "Saldo insuficiente para realizar la transferencia");
	            return "redirect:/transferencia";
	        }
	    } else {
	        //la cuenta destino no existe
	        ratt.addFlashAttribute("mensaje", "La cuenta destino no existe");
	        return "redirect:/transferencia";
	    }
	}
	
	@GetMapping("/detalle-movs/{idCuenta}")
	public String showMoviientos(@PathVariable int idCuenta, Model model) {
		//busco con el método findBycuenta el id de la cuenta en la lista de movimientos
		//para encontrar los movimientos correspondientes a la cuenta de la sesión
		//lo cual me llega por @PathVariable en la vista
		List<Movimiento> movimiento = mdao.findByCuenta(idCuenta);
		model.addAttribute("movimiento", movimiento);
		return "detalle-movs";
	}
	
	
	@GetMapping("/logout")
	public String logout(HttpSession sesion) {
		//eliminamos el atributo cuenta de la sesión actual
		sesion.removeAttribute("cuenta");
		//invalidamos la sesión del todo para eliminar todo los atributos y datos asociados con la sesión
		sesion.invalidate();
		
		return "/logout";
	}
	
}
