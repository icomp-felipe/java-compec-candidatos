package compec.ufam.consulta.view;

import java.awt.*;
import javax.swing.*;

import com.phill.libs.KeyboardAdapter;

import java.awt.event.*;
import compec.ufam.consulta.model.*;
import compec.ufam.consulta.utils.*;

public class TelaCriaUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final JPasswordField textSenhaOnce, textSenhaTwice;
	private final JTextField textLogin;
	private final JFrame frame;
	
	public TelaCriaUsuario(JFrame frame) {
		super("Criação de Usuário");
		
		this.frame = frame;
		
		Font  fonte = GraphicsHelper.getFont();
		Color color = GraphicsHelper.getColor();
		
		JPanel painelLogin = new JPanel();
		painelLogin.setLayout(null);
		painelLogin.setBorder(GraphicsHelper.getTitledBorder("Digite seu login"));
		painelLogin.setBounds(12, 25, 272, 61);
		getContentPane().add(painelLogin);
		
		textLogin = new JTextField();
		textLogin.setForeground(color);
		textLogin.setFont(fonte);
		textLogin.setBounds(12, 30, 248, 20);
		painelLogin.add(textLogin);
		
		JPanel painelSenha = new JPanel();
		painelSenha.setBorder(GraphicsHelper.getTitledBorder("Digite sua senha"));
		painelSenha.setBounds(12, 95, 272, 61);
		getContentPane().add(painelSenha);
		painelSenha.setLayout(null);
		
		textSenhaOnce = new JPasswordField();
		textSenhaOnce.setFont(fonte);
		textSenhaOnce.setForeground(color);
		textSenhaOnce.setBounds(12, 30, 248, 20);
		painelSenha.add(textSenhaOnce);
		
		JPanel painelConfirmaSenha = new JPanel();
		painelConfirmaSenha.setLayout(null);
		painelConfirmaSenha.setBorder(GraphicsHelper.getTitledBorder("Confirme sua senha"));
		painelConfirmaSenha.setBounds(12, 165, 272, 61);
		getContentPane().add(painelConfirmaSenha);
		
		textSenhaTwice = new JPasswordField();
		textSenhaTwice.setFont(fonte);
		textSenhaTwice.setForeground(color);
		textSenhaTwice.setBounds(12, 30, 248, 20);
		painelConfirmaSenha.add(textSenhaTwice);
		
		JButton botaoSalvar = new JButton("Salvar");
		botaoSalvar.addActionListener((event) -> salvar());
		botaoSalvar.setBounds(148, 233, 90, 25);
		getContentPane().add(botaoSalvar);
		
		KeyListener listener = (KeyboardAdapter) (event) -> { if (event.getKeyCode() == KeyEvent.VK_ENTER) botaoSalvar.doClick(); };
		textSenhaTwice.addKeyListener(listener);
		
		JButton botaoSair = new JButton("Sair");
		botaoSair.addActionListener((event) -> dispose());
		botaoSair.setBounds(52, 233, 90, 25);
		
		getContentPane().add(botaoSair);
		
		setSize(300,307);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setResizable(false);
		setVisible(true);
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		frame.setVisible(true);
	}
	
	private void salvar() {
		
		String login	= textLogin.getText();
		String keyOnce  = new String(textSenhaOnce.getPassword());
		String keyTwice = new String(textSenhaTwice.getPassword());
		
		if (keyOnce.equals(keyTwice)) {
			UsuarioDAO.createUser(login,keyTwice);
			dispose();
		}
		else
			AlertDialog.erro("Senhas não conferem!");
		
	}

}
