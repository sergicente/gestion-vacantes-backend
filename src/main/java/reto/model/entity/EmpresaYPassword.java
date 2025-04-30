package reto.model.entity;

public class EmpresaYPassword {

	private Empresa empresa;
    private String password;
    public EmpresaYPassword() {
		super();
	}

	

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


    public EmpresaYPassword(Empresa empresa, String password) {
        this.empresa = empresa;
        this.password = password;
    }
}
	