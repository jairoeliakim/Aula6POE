
package pacote.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import pacote.dominio.Laboratorio;

// Classe DAO (Data Access Object) para laboratorio
public class DAOLaboratorio {
    
    private Connection con;
    //Pre-compila a query para o banco de dados
    private PreparedStatement comando;
    
    // Classe acessada internamente para conectar com o banco
    private void conectar(){
        con = FabricaConexao.conexao();
    }
    
    //Classe que fecha a conexão com o banco
    private void fechar(){
        try{
            comando.close();
            con.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao fechar conexão"+e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }
    
    public boolean insereFlaboratorio(Laboratorio laboratorio){
        
        conectar();
        String sql = "INSERT INTO fornecedor(nome, data_Entrega, "
                + "especialidade, cnpj) VALUES(?,?,?,?)";
        
        Date auxDataEntrega = new Date();
        auxDataEntrega = laboratorio.getDataEntrega();
        //convertendo para data de banco
        java.sql.Date sqlDataEntrega = new java.sql.Date(auxDataEntrega.getTime());
        
        try{
            comando = con.prepareStatement(sql);
            comando.setString(1, laboratorio.getNome());
            comando.setDate(2, sqlDataEntrega);
            comando.setString(3, laboratorio.getEspecialidade());
            comando.setInt(4, laboratorio.getCnpj());
            
            comando.execute();
            return true;
        }catch(SQLException e){
              JOptionPane.showMessageDialog(null, "Erro ao inserir registro."+e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE, null);
        }finally{
            fechar();
        }
        return false;
    }
    
    public  ArrayList<Laboratorio> selecionarTodosRegistros()
    {
        conectar();
        //interface utilizada pra guardar dados vindos de um banco de dados
        ResultSet rs;
        String sql = "SELECT * FROM Laboratorio";
        //lista que conterá todas as informações de Flaboratorio no banco de dados
        ArrayList<Laboratorio> listaLaboratorio = new ArrayList();
        try{
            comando = con.prepareStatement(sql);
            rs = comando.executeQuery();
            while(rs.next())
            {
                Laboratorio laboratorio = new Laboratorio();
                laboratorio.setNome(rs.getString("NOME"));
                laboratorio.setCnpj(rs.getInt("CNPJ"));
                laboratorio.setDataEntrega(rs.getDate("DATA_Entrega"));
                laboratorio.setEspecialidade(rs.getString("ESPECIALIDADE"));
                laboratorio.setId(rs.getInt("ID"));
                listaLaboratorio.add(laboratorio);
            }
            fechar();
            return listaLaboratorio;
        }catch(SQLException e){
             JOptionPane.showMessageDialog(null, "Erro ao buscar registro."+e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE, null);
            fechar();
            return null;
        }
            
    }
    
    public boolean removeFlaboratorio(Integer id){
        conectar();
        String sql = "DELETE FROM Laboratorio WHERE ID=?";
        try{
            comando = con.prepareStatement(sql);
            comando.setInt(1, id);
            comando.executeUpdate();
            return true;
        }catch(SQLException e){
              JOptionPane.showMessageDialog(null, "Erro ao excluir registro."+e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE, null);
        }finally{
            fechar();
        }
        return false;
    }
    
    public boolean alteralaboratorio(Laboratorio laboratorio){
        conectar();
         String sql = "UPDATE laboratorio SET NOME = ?, DATA_Entrega = ?, ESPECIALIDADE = ?, CNPJ = ? "
                 + "WHERE ID=?";
         java.sql.Date sqlDataEntrega = new java.sql.Date(laboratorio.getDataEntrega().getTime());
         try{
            comando = con.prepareStatement(sql);
            comando.setString(1, laboratorio.getNome());
            comando.setDate(2, sqlDataEntrega);
            comando.setString(3, laboratorio.getEspecialidade());
            comando.setInt(4, laboratorio.getCnpj());
            comando.setInt(5, laboratorio.getId());
            comando.executeUpdate();
            return true;
        }catch(SQLException e){
              JOptionPane.showMessageDialog(null, "Erro ao atualizar registro."+e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE, null);
        }finally{
            fechar();
        }
         return false;
    }

    public boolean insereLaboratorio(Laboratorio laboratorio) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
