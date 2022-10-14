package senac.senacfx.model.dao;

import senac.senacfx.model.entities.Veiculos;
import senac.senacfx.model.entities.Entregador;
import java.util.List;

public interface EntregadorDao {

    void insert(Entregador obj);
    void update(Entregador obj);


    void deleteByid_entregador(Integer id_entregador);

    void deleteById_entregador(Integer id_entregador);

    Entregador findById_veiculo(Integer id_entregador);

    Entregador findByid_entregador(Integer id_entregador);

    Entregador findById(Integer id);

    List<Entregador> findAll();
    List<Entregador> findByVeiculo(Veiculos veiculos);

    List<Entregador> findByVeiculos(Veiculos veiculos);
}
