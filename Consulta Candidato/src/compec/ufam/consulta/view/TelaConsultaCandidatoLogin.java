package compec.ufam.consulta.view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import compec.ufam.consulta.model.*;
import compec.ufam.consulta.utils.*;
import com.phill.libs.KeyboardAdapter;
import com.phill.libs.ResourceManager;

/** Classe TelaLogin - cria um ambiente gráfico para o usuário fazer login no sistema
 *  @author Felipe André Souza da Silva 
 *  @version 2.00, 12/09/2014 */
public class TelaConsultaCandidatoLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final JLabel labelSenha;
	private final JTextField textLogin;
	private final JPasswordField textSenha;
	
	/** Construtor da classe TelaLogin - Cria a janela */
	public static void main(String[] args) {
		
		try {
			if (args[0].equals("--no-login"))
				new TelaBuscaCandidato();
			return;
		}
		catch (ArrayIndexOutOfBoundsException exception) { }
		
		new TelaConsultaCandidatoLogin();
	}
	
	/** Construtor da classe TelaLogin - constrói a janela gráfica */
	public TelaConsultaCandidatoLogin() {
		super("Tela de Login");
		
		Font  dialg = GraphicsHelper.getFont ();
		Color color = GraphicsHelper.getColor();
		Font fonte = GraphicsHelper.getFont(20);
		
		Container container = getContentPane();
		container.setLayout(null);		
	
		ImageIcon icon = new ImageIcon(ResourceManager.getResource("img/login.png"));
		JLabel labelImagem = new JLabel(icon);
		labelImagem.setBounds(12, 12, 232, 235);
		
		JLabel labelLogin = new JLabel("Login:");
		labelLogin.setFont(fonte);
		labelLogin.setBounds(256, 75, 85, 24);
		
		textLogin = new JTextField();
		textLogin.setFont(dialg);
		textLogin.setForeground(color);
		textLogin.setToolTipText("Digite aqui seu login");
		textLogin.setBounds(341, 75, 193, 24);
		textLogin.setColumns(10);
		
		labelSenha = new JLabel("Senha:");
		labelSenha.setFont(fonte);
		labelSenha.setBounds(256, 134, 85, 24);
		
		final JButton botaoEntrar = new JButton("Entrar");
		botaoEntrar.addActionListener((event) -> tryLogin());
		botaoEntrar.setBounds(449, 203, 85, 25);
		container.add(botaoEntrar);
		
		textSenha = new JPasswordField();
		textSenha.setFont(dialg);
		textSenha.setForeground(color);
		textSenha.setToolTipText("Digite aqui sua senha");
		textSenha.setBounds(341, 134, 193, 24);
		
		KeyListener listener = (KeyboardAdapter) (event) -> { if (event.getKeyCode() == KeyEvent.VK_ENTER) botaoEntrar.doClick(); };
		textSenha.addKeyListener(listener);
				
		container.add(labelImagem);
		container.add(labelLogin);
		container.add(textLogin);
		container.add(labelSenha);
		container.add(textSenha);
		
		JButton botaoLimpar = new JButton("Limpar");
		botaoLimpar.addActionListener((event) -> limpaCampos());
		botaoLimpar.setBounds(352, 203, 85, 25);
		container.add(botaoLimpar);
		
		JButton botaoSair = new JButton("Sair");
		botaoSair.addActionListener((event) -> dispose());
		botaoSair.setBounds(256, 203, 85, 25);
		getContentPane().add(botaoSair);

	    setSize(550,300);
		setResizable(false);
	    setLocationRelativeTo(null); 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		firstAccess(this);
		
	}
	
	private void firstAccess(JFrame frame) {
		
		if (UsuarioDAO.firstAccess()) {
			AlertDialog.informativo("Bem vindo ao sistema de busca de candidatos!\nA seguir configure seu login e senha!");
			new TelaCriaUsuario(frame);
		}
		else
			setVisible(true);
	}

	private void tryLogin() {
		
		String login = textLogin.getText();
		String senha = new String(textSenha.getPassword());
		
		if (!UsuarioDAO.tryLogin(login, senha))
			AlertDialog.erro("Usuário e/ou senha inválidos!");
		else {
			new TelaBuscaCandidato();
			dispose();
		}
	}
	
	/** Método para limpar os campos de texto da janela */
	private void limpaCampos() {
		textLogin.setText(null);
		textSenha.setText(null);
	}
	
}
