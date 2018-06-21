package Dao;

import Models.Tareas;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TareasDao extends DAO {

    public List<Tareas> listar() throws SQLException, Exception {
        this.Conectar();
        List<Tareas> lista;
        ResultSet rs;
        try {
            String sql = "Select CODIGO,TO_CHAR(FECHA,'DD/MM/YYYY') AS FECHA,DESCRIPCION,ESTADO from Tareas WHERE ESTADO LIKE 'A'";
            PreparedStatement ps = this.getCn().prepareCall(sql);
            rs = ps.executeQuery();
            lista = new ArrayList();
            Tareas model;
            while (rs.next()) {
                model = new Tareas(
                        rs.getString("CODIGO"),
                        rs.getString("FECHA"),
                        rs.getString("DESCRIPCION"),
                        rs.getString("ESTADO")
                );
                lista.add(model);
            }
            return lista;
        } catch (SQLException e) {
            throw e;
        }
    }

    public void actualizarFecha(String Codigo, String Fecha) throws Exception {
        this.Conectar();
        try {
            String sql = "UPDATE TAREAS SET FECHA = TO_DATE(?,'DD/MM/YYYY') WHERE CODIGO LIKE ?";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, Fecha);
            ps.setString(2, Codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void actualizar(String Codigo, String Descripcion, String Fecha) throws Exception {
        this.Conectar();
        try {
            String sql = "UPDATE TAREAS SET FECHA = TO_DATE(?,'DD/MM/YYYY'),DESCRIPCION=? WHERE CODIGO LIKE ?";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, Fecha);
            ps.setString(2, Descripcion);
            ps.setString(3, Codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    
    public void eliminar(String Codigo) throws Exception {
        this.Conectar();
        try {
            String sql = "UPDATE TAREAS SET ESTADO = 'I' WHERE CODIGO = ?";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, Codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void ingresar(String Fecha, String Descripcion) throws Exception {
        this.Conectar();
        try {
            String sql = "INSERT INTO TAREAS (FECHA,DESCRIPCION) VALUES (TO_DATE(?,'DD/MM/YYYY'),?)";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, Fecha);
            ps.setString(2, Descripcion);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

}
