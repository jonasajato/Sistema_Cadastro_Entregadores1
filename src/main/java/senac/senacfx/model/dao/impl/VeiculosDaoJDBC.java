package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DB;
import senac.senacfx.db.DbException;
import senac.senacfx.model.dao.VeiculosDao;
import senac.senacfx.model.entities.Veiculos;

import java.sql.*;
import java.time.Year;
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
        try{
            st = conn.prepareStatement(
                    "insert into veiculos " +
                            "(placa, modelo, ano, cor, fabricante, km, valor_fipe) " +
                            "values (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

//            st.setInt(1, obj.getId_veiculo());
            st.setString(1, obj.getPlaca());
            st.setString(2, obj.getModelo());
            st.setString(3, obj.getAno());
            st.setString(4, obj.getCor());
            st.setString(5, obj.getFabricante());
            st.setString(6, obj.getKm());
            st.setDouble(7, obj.getValor_fipe());


            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id_veiculo = rs.getInt(1);
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
            st = conn.prepareStatement("update veiculos " +
                    "set id_veiculo = ?, placa = ?, modelo = ?, ano = ?, cor = ?, fabricante = ?, " +
                    "km = ?, valor_fipe = ?");

            st.setInt(1, obj.getId_veiculo());
            st.setString(2, obj.getPlaca());
            st.setString(3, obj.getModelo());
            st.setString(4, obj.getAno());
            st.setString(5, obj.getCor());
            st.setString(6, obj.getFabricante());
            st.setString(7, obj.getKm());
            st.setDouble(8, obj.getValor_fipe());
//            st.setInt(9, obj.getId_veiculo());
            st.executeUpdate();

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteByid_veiculo(Integer id_veiculo) {

    }

    @Override
    public void deleteById_veiculo(Integer id_veiculo) {
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
    public Veiculos findByid_veiculo(Integer id_veiculo) {
        return null;
    }

    @Override
    public Veiculos findByid_veiculos(Integer id_veiculos) {
        return null;
    }

    @Override
    public Veiculos findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select * from veiculos ");
//                    "where id_veiculo = ?" + id_veiculo);

            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()){
                Veiculos obj = instantiateVeiculos(rs);
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
        Veiculos obj = new Veiculos();
        obj.setId_veiculo(rs.getInt("id_veiculo"));
        obj.setPlaca(rs.getString("placa"));
        obj.setModelo(rs.getString("modelo"));
        obj.setAno(Year.of(rs.getInt("ano")));
        obj.setCor(rs.getString("cor"));
        obj.setFabricante(rs.getString("fabricante"));
        obj.setKm(rs.getString("km"));
        obj.setValor_fipe(rs.getDouble("valor_fipe"));
        return obj;
    }

    @Override
    public List<Veiculos> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select * from veiculos");

            rs = st.executeQuery();

            List<Veiculos> list = new ArrayList<>();
            Map<Integer, Veiculos> map = new HashMap<>();

            while (rs.next()){

                Veiculos obj = map.get(rs.getInt("id_veiculo"));

                if (obj == null){
                    obj = instantiateVeiculos(rs);
                    map.put(rs.getInt("id_veiculo"), obj);
                }

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
    public List<Veiculos> findByveiculos(Veiculos veiculos) {
        return null;
    }

    @Override
    public List<Veiculos> findByVeiculos(Veiculos veiculos) {
        return null;
    }
}
