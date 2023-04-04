
package pacote.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import pacote.dominio.Pessoa;

// Classe DAO (Data Access Object) para Pessoa
public class DAOPessoa {
    
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
    
    public boolean inserePessoa(Pessoa pessoa){
        
        conectar();
        String sql = "INSERT INTO PESSOA(nome, data_nascimento, "
                + "profissao, matricula) VALUES(?,?,?,?)";
        
        Date auxDataNascimento = new Date();
        auxDataNascimento = pessoa.getDataNascimento();
        //convertendo para data de banco
        java.sql.Date sqlDataNascimento = new java.sql.Date(auxDataNascimento.getTime());
        
        try{
            comando = con.prepareStatement(sql);
            comando.setString(1, pessoa.getNome());
            comando.setDate(2, sqlDataNascimento);
            comando.setString(3, pessoa.getProfissao());
            comando.setInt(4, pessoa.getMatricula());
            
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
    
    public  ArrayList<Pessoa> selecionarTodosRegistros()
    {
        conectar();
        //interface utilizada pra guardar dados vindos de um banco de dados
        ResultSet rs;
        String sql = "SELECT * FROM PESSOA";
        //lista que conterá todas as informações de pessoas no banco de dados
        ArrayList<Pessoa> listaPessoas = new ArrayList();
        try{
            comando = con.prepareStatement(sql);
            rs = comando.executeQuery();
            while(rs.next())
            {
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("NOME"));
                pessoa.setMatricula(rs.getInt("MATRICULA"));
                pessoa.setDataNascimento(rs.getDate("DATA_NASCIMENTO"));
                pessoa.setProfissao(rs.getString("PROFISSAO"));
                pessoa.setId(rs.getInt("ID"));
                listaPessoas.add(pessoa);
            }
            fechar();
            return listaPessoas;
        }catch(SQLException e){
             JOptionPane.showMessageDialog(null, "Erro ao buscar registro."+e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE, null);
            fechar();
            return null;
        }
            
    }
    
    public boolean removePessoa(Integer id){
        conectar();
        String sql = "DELETE FROM PESSOA WHERE ID=?";
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
    
    public boolean alteraPessoa(Pessoa pessoa){
        conectar();
         String sql = "UPDATE PESSOA SET NOME = ?, DATA_NASCIMENTO = ?, PROFISSAO = ?, MATRICULA = ? "
                 + "WHERE ID=?";
         java.sql.Date sqlDataNascimento = new java.sql.Date(pessoa.getDataNascimento().getTime());
         try{
            comando = con.prepareStatement(sql);
            comando.setString(1, pessoa.getNome());
            comando.setDate(2, sqlDataNascimento);
            comando.setString(3, pessoa.getProfissao());
            comando.setInt(4, pessoa.getMatricula());
            comando.setInt(5, pessoa.getId());
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
