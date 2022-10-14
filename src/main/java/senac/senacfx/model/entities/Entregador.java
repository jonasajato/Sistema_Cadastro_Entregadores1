package senac.senacfx.model.entities;

import java.io.Serializable;
import java.util.Date;

public class Entregador implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id_entregador;
    private String nome;
    private String email;
    private Date data_de_nascimento;
    private Double salario;

    private String endereco;

    private String telefone;

    private Veiculos veiculos;

    public Entregador() {
    }

    public Entregador(Integer id_entregador, String nome, String email, Date data_de_nascimento, Double salario, String endereco, String telefone, Veiculos veiculos) {
        this.id_entregador = id_entregador;
        this.nome = nome;
        this.email = email;
        this.data_de_nascimento = data_de_nascimento;
        this.salario = salario;
        this.endereco = endereco;
        this.telefone = telefone;
        this.veiculos = veiculos;
    }

    public Integer getId_entregador() {
        return id_entregador;
    }

    public void setId_entregador(Integer id_entregador) {
        this.id_entregador = id_entregador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getData_de_nascimento() {
        return data_de_nascimento;
    }

    public void setData_de_nascimento(Date data_de_nascimento) {
        this.data_de_nascimento = data_de_nascimento;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public String getendereco() {return endereco;}

    public String setendereco(String text) {return endereco;}

    public String gettelefone() {return telefone;}

    public String settelefone(String text) {return telefone;}

    public Veiculos getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(Veiculos veiculos) {
        this.veiculos = veiculos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Entregador other = (Entregador) o;
        if (id_entregador == null){
            if (other.id_entregador != null)
                return false;
        } else if (!id_entregador.equals(other.id_entregador))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id_entregador == null) ? 0 : id_entregador.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Entregador{" +
                "id_entregador=" + id_entregador +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", data de nascimento=" + data_de_nascimento +
                ", salario=" + salario +
                ", endereco=" + endereco +
                ", telefone=" + telefone +
                ", veiculos=" + veiculos +
                '}';
    }
}
