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
            st.setString(2, obj.getModelo());
            st.setString(3, obj.getCor());
            st.setString(4, obj.getFabricante());
            st.setString(5, obj.getKm());
            st.setDouble(6, obj.getValor_fipe());


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
                            "set placa = ? " +
                            "set modelo = ? " +
                    "set ano = ? " +
                    "set cor = ? " +
                    "set fabricante = ? " +
                    "set km = ? " +
                    "set valor_fipe = ? " +
                            "where id_veiculo = ?");

            st.setString(1, obj.getPlaca());
            st.setInt(2, obj.getId_veiculo());
            st.setString(3, obj.getModelo());
            st.setString(4, obj.getAno());
            st.setString(5, obj.getCor());
            st.setString(6, obj.getFabricante());
            st.setString(7, obj.getKm());
            st.setDouble(8, obj.getValor_fipe());

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
            st = conn.prepareStatement("delete from veiculos where id_veiculo = ?");

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
                    "select * from veiculos ");
//                    "where id_veiculo = ?" + id_veiculo);

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
        dep.setId_veiculo(rs.getInt("id_veiculo"));
        dep.setPlaca(rs.getString("placa"));
        dep.setModelo(rs.getString("modelo"));
        dep.setAno(rs.getDate("ano"));
        dep.setCor(rs.getString("cor"));
        dep.setFabricante(rs.getString("fabricante"));
        dep.setKm(rs.getString("km"));
        dep.setValor_fipe(rs.getDouble("valor_fipe"));
        return dep;
    }

    @Override
    public List<Veiculos> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select * from veiculos ");

            rs = st.executeQuery();

            List<Veiculos> list = new ArrayList<>();
            Map<Integer, Veiculos> map = new HashMap<>();

            while (rs.next()){

                Veiculos dep = map.get(rs.getInt("id_veiculo"));

                if (dep == null){
                    dep = instantiateVeiculos(rs);
                    map.put(rs.getInt("id_veiculo"), dep);
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
