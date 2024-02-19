package cajero.modelo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cuentas")
public class Cuenta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cuenta")
	private int idCuenta;
	private double saldo;
	@Column(name = "tipo_cuenta")
	private String tipoCuenta;

	public void ingresar(double cantidad) {
		this.saldo += cantidad;

	}

	public void extraer(double cantidad) {
		if (this.saldo >= cantidad) {
			this.saldo -= cantidad;
		}
	}

	public void transferir(Cuenta cuentaDestino, double cantidad) {
		if (this.saldo >= cantidad) {
			extraer(cantidad);
			cuentaDestino.ingresar(cantidad);
		}
	}

}
