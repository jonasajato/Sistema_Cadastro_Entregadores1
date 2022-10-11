package senac.senacfx.model.dao;

import senac.senacfx.model.entities.Veiculos;

import java.util.List;

public interface VeiculosDao {

    void insert(Veiculos obj);
    void update(Veiculos obj);
    void deleteById(Integer id);
    Veiculos findById(Integer id);
    List<Veiculos> findAll();

}
