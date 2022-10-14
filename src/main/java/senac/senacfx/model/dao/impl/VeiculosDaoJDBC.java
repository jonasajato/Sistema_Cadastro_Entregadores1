package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DB;
import senac.senacfx.db.DbException;
import senac.senacfx.model.dao.VeiculosDao;
import senac.senacfx.model.entities.Veiculos;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VeiculosDaoJDBC implements VeiculosDao {
    private Connection conn;

    public VeiculosDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Veiculos obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("insert into veiculo " +
                    "(placa) " +
                            "(modelo) " +
                            "(ano) " +
                            "(ano) " +
                            "(fabricante ) " +
                            "(km) " +
                            "(valor_fipe) " +
                    "values (?) ",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getPlaca());
            st.setString(1, obj.getModelo());
            st.setString(1, obj.getCor());
            st.setString(1, obj.getFabricante());
            st.setString(1, obj.getKm());
            st.setDouble(1, obj.getValor_fipe());


            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    obj.setId_veiculo(obj.getId_veiculo());
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
    public void update(Veiculos obj) {

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("update veiculo " +
                            "set Placa = ? " +
                            "where Id_veiculo = ?");

            st.setString(1, obj.getPlaca());
            st.setInt(2, obj.getId_veiculo());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0){
                throw new DbException("Error! No rows affected!");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id_veiculo) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("delete from Veiculos where id_veiculos = ?");

            st.setInt(1, id_veiculo);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0){
                throw new DbException("Veiculo inexistente!");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Veiculos findById(Integer id_veiculo) {

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select * from Veiculos " +
                    "where id_veiculo = ?");

            st.setInt(1, id_veiculo);
            rs = st.executeQuery();
            if (rs.next()){
                Veiculos dep = instantiateVeiculos(rs);
                return dep;

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
        dep.setId_veiculo(rs.getInt("Id_veiculo"));
        dep.setPlaca(rs.getString("placa"));
        return dep;
    }

    @Override
    public List<Veiculos> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select * from Veiculos "+
                    "order by Modelo");

            rs = st.executeQuery();

            List<Veiculos> list = new ArrayList<>();
            Map<Integer, Veiculos> map = new HashMap<>();

            while (rs.next()){

                Veiculos dep = map.get(rs.getInt("Id"));

                if (dep == null){
                    dep = instantiateVeiculos(rs);
                    map.put(rs.getInt("Id"), dep);
                }

                list.add(dep);

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
