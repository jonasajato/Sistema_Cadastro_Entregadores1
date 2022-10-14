package senac.senacfx.model.services;

import senac.senacfx.model.dao.DaoFactory;
import senac.senacfx.model.dao.EntregadorDao;
import senac.senacfx.model.entities.Entregador;

import java.util.List;

public class EntregadorService {

    //dependencia injetada usando padrao factory
    private EntregadorDao dao = DaoFactory.createEntregadorDao();

    public List<Entregador> findAll() {
        return dao.findAll();

        //Dados MOCK (fake) so para testar, sem puxar do banco por hora
//        List<Entregador> list = new ArrayList<>();
//        list.add(new Entregador(1,"Computadores"));
//        list.add(new Entregador(2,"Alimentação"));
//        list.add(new Entregador(3,"Financeiro"));
//        return list;

    }
    public void saveOrUpdate(Entregador obj){
        if (obj.getId_entregador() == null){
            dao.insert(obj);
        } else {
            dao.update(obj);
            }
        }

        public void remove(Entregador obj){
            dao.deleteById_entregador(obj.getId_entregador());
        }
    }

