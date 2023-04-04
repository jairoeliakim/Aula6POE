
package pacote.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import pacote.dominio.Funcionario;

// Classe DAO (Data Access Object) para Funcionario
public class DAOFuncionario {
    
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
    
    public boolean insereFuncionario(Funcionario funcionario){
        
        conectar();
        String sql = "INSERT INTO funcionario(nome, data_nascimento, "
                + "cargo, matricula) VALUES(?,?,?,?)";
        
        Date auxDataNascimento = new Date();
        auxDataNascimento = funcionario.getDataNascimento();
        //convertendo para data de banco
        java.sql.Date sqlDataNascimento = new java.sql.Date(auxDataNascimento.getTime());
        
        try{
            comando = con.prepareStatement(sql);
            comando.setString(1, funcionario.getNome());
            comando.setDate(2, sqlDataNascimento);
            comando.setString(3, funcionario.getCargo());
            comando.setInt(4, funcionario.getMatricula());
            
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
    
    public  ArrayList<Funcionario> selecionarTodosRegistros()
    {
        conectar();
        //interface utilizada pra guardar dados vindos de um banco de dados
        ResultSet rs;
        String sql = "SELECT * FROM FUNCINARIO";
        //lista que conterá todas as informações de Funcionarios no banco de dados
        ArrayList<Funcionario> listaFuncionarios = new ArrayList();
        try{
            comando = con.prepareStatement(sql);
            rs = comando.executeQuery();
            while(rs.next())
            {
                Funcionario funcionario = new Funcionario();
                funcionario.setNome(rs.getString("NOME"));
                funcionario.setMatricula(rs.getInt("MATRICULA"));
                funcionario.setDataNascimento(rs.getDate("DATA_NASCIMENTO"));
                funcionario.setCargo(rs.getString("CARGO"));
                funcionario.setId(rs.getInt("ID"));
                listaFuncionarios.add(funcionario);
            }
            fechar();
            return listaFuncionarios;
        }catch(SQLException e){
             JOptionPane.showMessageDialog(null, "Erro ao buscar registro."+e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE, null);
            fechar();
            return null;
        }
            
    }
    
    public boolean removeFuncionario(Integer id){
        conectar();
        String sql = "DELETE FROM FUNCIONARIO WHERE ID=?";
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
    
    public boolean alteraFuncionario(Funcionario funcionario){
        conectar();
         String sql = "UPDATE FUNCIONARIO SET NOME = ?, DATA_NASCIMENTO = ?, CARGO = ?, MATRICULA = ? "
                 + "WHERE ID=?";
         java.sql.Date sqlDataNascimento = new java.sql.Date(funcionario.getDataNascimento().getTime());
         try{
            comando = con.prepareStatement(sql);
            comando.setString(1, funcionario.getNome());
            comando.setDate(2, sqlDataNascimento);
            comando.setString(3, funcionario.getCargo());
            comando.setInt(4, funcionario.getMatricula());
            comando.setInt(5, funcionario.getId());
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
