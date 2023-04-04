
package pacote.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import pacote.dominio.Fornecedores;

// Classe DAO (Data Access Object) para fornecedores
public class DAOFornecedores {
    
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
    
    public boolean insereFornecedores(Fornecedores fornecedores){
        
        conectar();
        String sql = "INSERT INTO fornecedor(nome, data_UltimaCompra, "
                + "ramo, cnpj) VALUES(?,?,?,?)";
        
        Date auxDataUltimaCompra = new Date();
        auxDataUltimaCompra = fornecedores.getDataUltimaCompra();
        //convertendo para data de banco
        java.sql.Date sqlDataUltimaCompra = new java.sql.Date(auxDataUltimaCompra.getTime());
        
        try{
            comando = con.prepareStatement(sql);
            comando.setString(1, fornecedores.getNome());
            comando.setDate(2, sqlDataUltimaCompra);
            comando.setString(3, fornecedores.getRamo());
            comando.setInt(4, fornecedores.getCnpj());
            
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
    
    public  ArrayList<Fornecedores> selecionarTodosRegistros()
    {
        conectar();
        //interface utilizada pra guardar dados vindos de um banco de dados
        ResultSet rs;
        String sql = "SELECT * FROM FORNECEDORES";
        //lista que conterá todas as informações de FORNECEDORES no banco de dados
        ArrayList<Fornecedores> listaFornecedores = new ArrayList();
        try{
            comando = con.prepareStatement(sql);
            rs = comando.executeQuery();
            while(rs.next())
            {
                Fornecedores fornecedores = new Fornecedores();
                fornecedores.setNome(rs.getString("NOME"));
                fornecedores.setCnpj(rs.getInt("CNPJ"));
                fornecedores.setDataUltimaCompra(rs.getDate("DATA_UltimaCompra"));
                fornecedores.setRamo(rs.getString("RAMO"));
                fornecedores.setId(rs.getInt("ID"));
                listaFornecedores.add(fornecedores);
            }
            fechar();
            return listaFornecedores;
        }catch(SQLException e){
             JOptionPane.showMessageDialog(null, "Erro ao buscar registro."+e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE, null);
            fechar();
            return null;
        }
            
    }
    
    public boolean removeFornecedores(Integer id){
        conectar();
        String sql = "DELETE FROM FORNECEDORES WHERE ID=?";
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
    
    public boolean alteraFornecedores(Fornecedores fornecedores){
        conectar();
         String sql = "UPDATE FORNECEDORES SET NOME = ?, DATA_UltimaCompra = ?, RAMO = ?, CNPJ = ? "
                 + "WHERE ID=?";
         java.sql.Date sqlDataUltimaCompra = new java.sql.Date(fornecedores.getDataUltimaCompra().getTime());
         try{
            comando = con.prepareStatement(sql);
            comando.setString(1, fornecedores.getNome());
            comando.setDate(2, sqlDataUltimaCompra);
            comando.setString(3, fornecedores.getRamo());
            comando.setInt(4, fornecedores.getCnpj());
            comando.setInt(5, fornecedores.getId());
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
