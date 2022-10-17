package senac.senacfx.model.entities;

import java.io.Serializable;
import java.sql.Date;
import java.time.Year;

public class Veiculos implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id_veiculo;
    private String placa;
    private String modelo;
    private Year ano;
    private String cor;
    private String fabricante;
    private String km;
    private double valor_fipe;

    public Veiculos() {
    }

    public Veiculos(Integer id_veiculo, String placa, String modelo, Year ano, String cor, String fabricante, String km, Double valor_fipe) {
        this.id_veiculo = id_veiculo;
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.cor= cor;
        this.fabricante=fabricante;
        this.km= km;
        this.valor_fipe= valor_fipe;

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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return String.valueOf(ano);
    }

    public void setAno(Year ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public double getValor_fipe() {
        return valor_fipe;
    }

    public void setValor_fipe(double valor_fipe) {
        this.valor_fipe = valor_fipe;
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
        return "veiculo{" +
                "id_veiculo=" + id_veiculo +
                ", placa='" + placa + '\'' +
                ", modelo='" + modelo + '\'' +
                ", ano='" + ano + '\'' +
                ", cor='" + cor + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", km='" + km + '\'' +
                ", valor_fipe='" + valor_fipe + '\'' +
                '}';
    }


}
