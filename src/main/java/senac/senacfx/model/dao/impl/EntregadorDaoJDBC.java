package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DB;
import senac.senacfx.db.DbException;
import senac.senacfx.model.dao.EntregadorDao;
import senac.senacfx.model.entities.Veiculos;
import senac.senacfx.model.entities.Entregador;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntregadorDaoJDBC implements EntregadorDao {
    private Connection conn;

    public EntregadorDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Entregador obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "insert into Entregador " +
                            "(nome, Email, BirthDate, BaseSalary, id_veiculo) " +
                            "values (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getNome());
            st.setString(2, obj.getEmail());
            st.setDate(3, new Date(obj.getData_de_nascimento().getTime()));
            st.setDouble(4, obj.getSalario());
            st.setInt(5, obj.getVeiculos().getId_veiculo());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id_entregador = rs.getInt(1);
                    obj.setId_entregador(id_entregador);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Error! No rows affected!");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Entregador obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "update Entregador " +
                            "set nome = ?, Email = ?, BirthDate = ?, BaseSalary = ?, id_veiculo = ? " +
                            "where id = ?");

            st.setString(1, obj.getNome());
            st.setString(2, obj.getEmail());
            st.setDate(3, new Date(obj.getData_de_nascimento().getTime()));
            st.setDouble(4, obj.getSalario());
            st.setInt(5, obj.getVeiculos().getId_veiculo());
            st.setInt(6, obj.getId_entregador());

            st.executeUpdate();

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById_entregador(Integer id_entregador) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("delete from Entregador where Id = ?");

            st.setInt(1, id_entregador);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0){
                throw new DbException("Vendedor inexistente!");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Entregador findByid_veiculo(Integer id_entregador) {
        return null;
    }

    @Override
    public Entregador findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select Entregador.*, Veiculos.nome as Depnome " +
                    "from Entregador inner join Veiculos " +
                    "on Entregador.DepartmentId = id_veiculo " +
                    "where Entregador.Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()){
                Veiculos dep = instantiateVeiculos(rs);
                Entregador obj = instantiateEntregador(rs, dep);
                return obj;

            }
            return null;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Veiculos instantiateVeiculos(ResultSet rs) throws SQLException {
        Veiculos dep = new Veiculos();
        dep.setId_veiculo(rs.getInt("VeiculoId"));
        dep.setPlaca(rs.getString("VeiculoPlaca"));
        return dep;
    }

    private Entregador instantiateEntregador(ResultSet rs, Veiculos dep) throws SQLException{
        Entregador obj = new Entregador();
        obj.setId_entregador(rs.getInt("Id"));
        obj.setNome(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setSalario(rs.getDouble("BaseSalary"));
        obj.setData_de_nascimento(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
        obj.setVeiculos(dep);
        return obj;
    }
    @Override
    public List<Entregador> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select * from entregador");

//            select Entregador.*, Veiculos.Name as DepName " +
//            "from Entregador inner join Veiculos " +
//                    "on Entregador.id_veiculo = id_veiculo " +
//                    "order by Name

            rs = st.executeQuery();

            List<Entregador> list = new ArrayList<>();
            Map<Integer, Veiculos> map = new HashMap<>();

            while (rs.next()){

                Veiculos dep = map.get(rs.getInt("VeiculosId"));

                if (dep == null){
                    dep = instantiateVeiculos(rs);
                    map.put(rs.getInt("id_veiculo"), dep);
                }

                Entregador obj = instantiateEntregador(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Entregador> findByEntregador(Entregador entregador) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select * from entregador");

//            "select Entregador.*, Veiculos.Name as DepName " +
//                    "from Entregador inner join Veiculos " +
//                    "on Entregador.id_veiculo = id_veiculo " +
//                    "where id_veiculo = ? " +
//                    "order by Name"

            st.setInt(1, entregador.getId_entregador());

            rs = st.executeQuery();

            List<Entregador> list = new ArrayList<>();
            Map<Integer, Veiculos> map = new HashMap<>();

            while (rs.next()){

                Veiculos dep = map.get(rs.getInt("id_veiculo"));

                if (dep == null){
                    dep = instantiateVeiculos(rs);
                    map.put(rs.getInt("id_veiculo"), dep);
                }

                Entregador obj = instantiateEntregador(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
