package senac.senacfx.model.services;

import senac.senacfx.model.dao.DaoFactory;
import senac.senacfx.model.dao.VeiculosDao;
import senac.senacfx.model.entities.Veiculos;

import java.util.List;

public class VeiculoService {

    //dependencia injetada usando padrao factory
    private VeiculosDao dao = DaoFactory.createVeiculoDao();

    public List<Veiculos> findAll() {
        return dao.findAll();

        //Dados MOCK (fake) so para testar, sem puxar do banco por hora
//        List<Veiculos> list = new ArrayList<>();
//        list.add(new Veiculos(1,"Computadores"));
//        list.add(new Veiculos(2,"Alimentação"));
//        list.add(new Veiculos(3,"Financeiro"));
//        return list;

    }
    public void saveOrUpdate(Veiculos obj){
        if (obj.getId_veiculo() == null){
            dao.insert(obj);
        } else {
            dao.update(obj);
            }
        }

        public void remove(Veiculos obj){
            dao.deleteById(obj.getId_veiculo());
        }
    }

