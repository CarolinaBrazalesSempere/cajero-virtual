package cajero.modelo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cajero.modelo.dao.CuentaDao;
import cajero.modelo.entity.Cuenta;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private CuentaDao cdao;

	@GetMapping({ "", "/", "/home" })
	public String home() {
		
		return "home";
	}

	@PostMapping({ "", "/", "/home" })

	public String showMenu(RedirectAttributes ratt, HttpSession sesion, @RequestParam int idCuenta) {
		
		// Buscar la cuenta en base al ID proporcionado
		Cuenta cuenta = cdao.buscarUno(idCuenta);
		
		if (cuenta != null) {
			sesion.setAttribute("cuenta", cuenta);
			return "redirect:/menu";
		} else {
			ratt.addFlashAttribute("mensaje", "Cuenta incorrecta");
			return "redirect:/";
		}
	}

	@GetMapping("/menu")

	public String menu() {
		return "menu";
	}
}
