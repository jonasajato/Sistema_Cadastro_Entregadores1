package senac.senacfx.model.entities;

import java.io.Serializable;

public class Veiculos implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id_veiculo;
    private String placa;

    public Veiculos() {
    }

    public Veiculos(Integer id_veiculo, String placa) {
        this.id_veiculo = id_veiculo;
        this.placa = placa;
    }

    public Integer getId_veiculo() {
        return id_veiculo;
    }

    public void setId_veiculo(Integer id_veiculo) {
        this.id_veiculo = id_veiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Veiculos other = (Veiculos) o;
        if (id_veiculo == null){
            if (other.id_veiculo != null)
                return false;
        } else if (!id_veiculo.equals(other.id_veiculo))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id_veiculo == null) ? 0 : id_veiculo.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Veiculo{" +
                "id_veiculo=" + id_veiculo +
                ", placa='" + placa + '\'' +
                '}';
    }
}
