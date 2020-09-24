package compec.ufam.consulta.model;

import com.phill.libs.PropertiesManager;
import com.phill.libs.ui.AlertDialog;

public class UsuarioDAO {
	
	public static boolean tryLogin(String login, String senha) {
		
		String bdUser = PropertiesManager.getString("user.login",null);
		String bdPass = PropertiesManager.getString("user.pass",null);
		
		return (login.equals(bdUser) && senha.equals(bdPass));
		
	}

	public static void createUser(String login, String pass) {
		
		PropertiesManager.setString("user.login",login,null);
		PropertiesManager.setString("user.pass" ,pass ,null);
		
		AlertDialog.info("Usuário criado com sucesso!");
		
	}
	
	public static boolean firstAccess() {
		
		String bdUser = PropertiesManager.getString("user.login",null);
		String bdPass = PropertiesManager.getString("user.pass" ,null);
		
		return ((bdUser == null) || (bdPass == null));
		
	}
	
}
