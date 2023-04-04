
package pacote.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FabricaConexao {
    
    public static Connection conexao(){
        //Classe que fornece informações de tabelas, SQL's, procedimentos e sintaxe.
        Connection con = null;
        try {
            //fazendo a chamada para carregar o drive do MySql
            Class.forName("com.mysql.cj.jdbc.Driver");
//            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            String url = ("jdbc:mysql://127.0.0.1/poe");
            //estabelecendo conexão com o caminho, usuário e senha
            con = DriverManager.getConnection(url, "root", "");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FabricaConexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex) {
            Logger.getLogger(FabricaConexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
    
    
}
