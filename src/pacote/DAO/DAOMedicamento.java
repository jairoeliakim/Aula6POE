
package pacote.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import pacote.dominio.Medicamento;

// Classe DAO (Data Access Object) para medicamento
public class DAOMedicamento {
    
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
    
    public boolean insereMedicamento(Medicamento medicamento){
        
        conectar();
        String sql = "INSERT INTO medicamento(nome, data_Validade, "
                + "receita, lote) VALUES(?,?,?,?)";
        
        Date auxDataValidade = new Date();
        auxDataValidade = medicamento.getDataValidade();
        //convertendo para data de banco
        java.sql.Date sqlDataValidade = new java.sql.Date(auxDataValidade.getTime());
        
        try{
            comando = con.prepareStatement(sql);
            comando.setString(1, medicamento.getNome());
            comando.setDate(2, sqlDataValidade);
            comando.setString(3, medicamento.getReceita());
            comando.setInt(4, medicamento.getLote());
            
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
    
    public  ArrayList<Medicamento> selecionarTodosRegistros()
    {
        conectar();
        //interface utilizada pra guardar dados vindos de um banco de dados
        ResultSet rs;
        String sql = "SELECT * FROM Medicamento";
        //lista que conterá todas as informações de medicamento no banco de dados
        ArrayList<Medicamento> listaMedicamento = new ArrayList();
        try{
            comando = con.prepareStatement(sql);
            rs = comando.executeQuery();
            while(rs.next())
            {
                Medicamento medicamento = new Medicamento();
                medicamento.setNome(rs.getString("NOME"));
                medicamento.setLote(rs.getInt("LOTE"));
                medicamento.setDataValidade(rs.getDate("DATA_Validade"));
                medicamento.setReceita(rs.getString("RECEITA"));
                medicamento.setId(rs.getInt("ID"));
                listaMedicamento.add(medicamento);
            }
            fechar();
            return listaMedicamento;
        }catch(SQLException e){
             JOptionPane.showMessageDialog(null, "Erro ao buscar registro."+e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE, null);
            fechar();
            return null;
        }
            
    }
    
    public boolean removeMedicamento(Integer id){
        conectar();
        String sql = "DELETE FROM Medicamento WHERE ID=?";
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
    
    public boolean alteramedicamento(Medicamento medicamento){
        conectar();
         String sql = "UPDATE medicamento SET NOME = ?, DATA_Validade = ?, RECEITA = ?, LOTE = ? "
                 + "WHERE ID=?";
         java.sql.Date sqlDataValidade = new java.sql.Date(medicamento.getDataValidade().getTime());
         try{
            comando = con.prepareStatement(sql);
            comando.setString(1, medicamento.getNome());
            comando.setDate(2, sqlDataValidade);
            comando.setString(3, medicamento.getReceita());
            comando.setInt(4, medicamento.getLote());
            comando.setInt(5, medicamento.getId());
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
}
