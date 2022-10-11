package senac.senacfx.model.dao;

import senac.senacfx.db.DB;
import senac.senacfx.model.dao.impl.VeiculosDaoJDBC;
import senac.senacfx.model.dao.impl.EntregadorDaoJDBC;
import senac.senacfx.model.entities.Entregador;
import senac.senacfx.model.entities.Veiculos;

import java.util.List;

public class DaoFactory {

    public static EntregadorDao createEntregadorDao(){
        return new EntregadorDaoJDBC(DB.getConnection()) {
            @Override
            public List<Entregador> findByVeiculo(Veiculos veiculos) {
                return null;
            }
        };
    }

    public static VeiculosDao createVeiculoDao(){
        return new VeiculosDaoJDBC(DB.getConnection());
    }

}
