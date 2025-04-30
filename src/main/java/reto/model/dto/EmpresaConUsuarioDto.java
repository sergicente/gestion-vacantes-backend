package reto.model.dto;

public class EmpresaConUsuarioDto {

    private int idEmpresa;
    private String cif;
    private String nombreEmpresa;
    private String direccionFiscal;
    private String pais;
    private String email;

    // Getters y Setters
    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccionFiscal() {
        return direccionFiscal;
    }

    public void setDireccionFiscal(String direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Constructor vacío
    public EmpresaConUsuarioDto() {}

    // Constructor completo (opcional)
    public EmpresaConUsuarioDto(int idEmpresa, String cif, String nombreEmpresa, String direccionFiscal, String pais, String email) {
        this.idEmpresa = idEmpresa;
        this.cif = cif;
        this.nombreEmpresa = nombreEmpresa;
        this.direccionFiscal = direccionFiscal;
        this.pais = pais;
        this.email = email;
    }

    @Override
    public String toString() {
        return "EmpresaConUsuarioDto{" +
                "idEmpresa=" + idEmpresa +
                ", cif='" + cif + '\'' +
                ", nombreEmpresa='" + nombreEmpresa + '\'' +
                ", direccionFiscal='" + direccionFiscal + '\'' +
                ", pais='" + pais + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
