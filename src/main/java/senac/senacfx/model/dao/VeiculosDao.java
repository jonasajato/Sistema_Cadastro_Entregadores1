package senac.senacfx.model.dao;

import senac.senacfx.model.entities.Entregador;
import senac.senacfx.model.entities.Veiculos;

import java.util.List;

public interface VeiculosDao {

//    void insert(Veiculos obj);
//    void update(Veiculos obj);
//    void deleteById(Integer id);
//    Veiculos findById(Integer id);
//    List<Veiculos> findAll();

    void insert(Veiculos obj);
    void update(Veiculos obj);


    void deleteByid_veiculo(Integer id_veiculo);

    void deleteById_veiculo(Integer id_veiculo);

    Veiculos findByid_veiculo(Integer id_veiculo);

    Veiculos findByid_veiculos(Integer id_veiculos);

    Veiculos findById(Integer id);

    List<Veiculos> findAll();
    List<Veiculos> findByveiculos(Veiculos veiculos);

    List<Veiculos> findByVeiculos(Veiculos veiculos);

}
