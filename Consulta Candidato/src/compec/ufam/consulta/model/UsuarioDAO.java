package compec.ufam.consulta.model;

import com.phill.libs.PropertiesManager;

import compec.ufam.consulta.utils.*;

public class UsuarioDAO {
	
	public static boolean tryLogin(String login, String senha) {
		
		String bdUser = PropertiesManager.getProperty("user.login");
		String bdPass = PropertiesManager.getProperty("user.pass");
		
		return (login.equals(bdUser) && senha.equals(bdPass));
		
	}

	public static void createUser(String login, String pass) {
		
		PropertiesManager.setProperty("user.login",login);
		PropertiesManager.setProperty("user.pass",pass);
		
		AlertDialog.informativo("Usuário criado com sucesso!");
		
	}
	
	public static boolean firstAccess() {
		
		String bdUser = PropertiesManager.getProperty("user.login");
		String bdPass = PropertiesManager.getProperty("user.pass");
		
		return ((bdUser == null) || (bdPass == null));
		
	}
	
}
