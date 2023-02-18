package compec.ufam.consulta.view;

import java.awt.*;
import javax.swing.*;
import com.phill.libs.br.CPFParser;
import com.phill.libs.ResourceManager;
import com.phill.libs.time.PhillsDateUtils;
import com.phill.libs.ui.AlertDialog;
import com.phill.libs.ui.GraphicsHelper;
import com.phill.libs.ui.JPaintedPanel;

import compec.ufam.consulta.model.*;

/** Classe que contém a implementação do visualizador de candidato.
 *  @author Felipe André
 *  @version 1.0, 23/08/2015 */
public class TelaVisualizaCandidato extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JTextField textNome, textSexo, textNascimento, textRG, textCPF;
	private final JTextField textEndereco, textFone, textEmail, textNomeConcurso;
	private final JTextField textCodigo, textInscricao, textSituacao;
	private final Color red = new Color(232,145,172);
	
	public TelaVisualizaCandidato(Candidato candidato) {
		super("Candidato: " + candidato.getNome(true));
		
		Dimension d = new Dimension(720,259);
		
		JPanel painelMaster = new JPaintedPanel("img/background-visualiza.jpg",d);
		setContentPane(painelMaster);
		
		final Font  fonte = GraphicsHelper.getInstance().getFont();
		final Color color = GraphicsHelper.getInstance().getColor();
		final Color label = new Color(32,43,194);
		
		setSize(d);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		painelMaster.setLayout(null);
		
		JPanel painelCandidato = new JPanel();
		painelCandidato.setOpaque(false);
		painelCandidato.setBorder(GraphicsHelper.getInstance().getTitledBorder("Candidato"));
		painelCandidato.setBounds(12, 12, 696, 147);
		painelMaster.add(painelCandidato);
		painelCandidato.setLayout(null);
		
		JLabel labelNome = new JLabel("Nome:");
		labelNome.setForeground(label);
		labelNome.setFont(fonte);
		labelNome.setBounds(12, 28, 70, 15);
		painelCandidato.add(labelNome);
		
		textNome = new JTextField();
		textNome.setFont(fonte);
		textNome.setEditable(false);
		textNome.setForeground(color);
		textNome.setBounds(92, 26, 492, 20);
		painelCandidato.add(textNome);
		textNome.setColumns(10);
		
		JLabel labelSexo = new JLabel("Sexo:");
		labelSexo.setForeground(label);
		labelSexo.setFont(fonte);
		labelSexo.setBounds(599, 28, 70, 15);
		painelCandidato.add(labelSexo);
		
		textSexo = new JTextField();
		textSexo.setFont(fonte);
		textSexo.setEditable(false);
		textSexo.setForeground(color);
		textSexo.setBounds(649, 26, 35, 20);
		textSexo.setHorizontalAlignment(SwingConstants.CENTER);
		painelCandidato.add(textSexo);
		textSexo.setColumns(10);
		
		JLabel labelNascimento = new JLabel("Nascim:");
		labelNascimento.setForeground(label);
		labelNascimento.setFont(fonte);
		labelNascimento.setBounds(12, 55, 70, 15);
		painelCandidato.add(labelNascimento);
		
		textNascimento = new JTextField();
		textNascimento.setHorizontalAlignment(SwingConstants.CENTER);
		textNascimento.setEditable(false);
		textNascimento.setFont(fonte);
		textNascimento.setForeground(color);
		textNascimento.setBounds(92, 53, 89, 20);
		painelCandidato.add(textNascimento);
		textNascimento.setColumns(10);
		
		JLabel labelRG = new JLabel("RG:");
		labelRG.setForeground(label);
		labelRG.setFont(fonte);
		labelRG.setBounds(226, 55, 53, 15);
		painelCandidato.add(labelRG);
		
		textRG = new JTextField();
		textRG.setEditable(false);
		textRG.setForeground(color);
		textRG.setFont(fonte);
		textRG.setColumns(10);
		textRG.setBounds(266, 53, 231, 20);
		painelCandidato.add(textRG);
		
		JLabel labelCPF = new JLabel("CPF:");
		labelCPF.setForeground(label);
		labelCPF.setFont(fonte);
		labelCPF.setBounds(519, 55, 53, 15);
		painelCandidato.add(labelCPF);
		
		textCPF = new JTextField();
		textCPF.setHorizontalAlignment(SwingConstants.CENTER);
		textCPF.setEditable(false);
		textCPF.setForeground(color);
		textCPF.setFont(fonte);
		textCPF.setColumns(10);
		textCPF.setBounds(565, 53, 119, 20);
		painelCandidato.add(textCPF);
		
		JLabel labelEndereco = new JLabel("Endereço:");
		labelEndereco.setForeground(label);
		labelEndereco.setFont(fonte);
		labelEndereco.setBounds(12, 84, 81, 15);
		painelCandidato.add(labelEndereco);
		
		textEndereco = new JTextField();
		textEndereco.setEditable(false);
		textEndereco.setColumns(10);
		textEndereco.setFont(fonte);
		textEndereco.setForeground(color);
		textEndereco.setBounds(92, 82, 592, 20);
		painelCandidato.add(textEndereco);
		
		JLabel labelFone = new JLabel("Telefone:");
		labelFone.setForeground(label);
		labelFone.setFont(fonte);
		labelFone.setBounds(12, 111, 70, 15);
		painelCandidato.add(labelFone);
		
		textFone = new JTextField();
		textFone.setEditable(false);
		textFone.setForeground(color);
		textFone.setFont(fonte);
		textFone.setColumns(10);
		textFone.setBounds(92, 109, 164, 20);
		painelCandidato.add(textFone);
		
		JLabel labelEmail = new JLabel("e-mail:");
		labelEmail.setForeground(label);
		labelEmail.setFont(fonte);
		labelEmail.setBounds(274, 111, 70, 15);
		painelCandidato.add(labelEmail);
		
		textEmail = new JTextField();
		textEmail.setEditable(false);
		textEmail.setForeground(color);
		textEmail.setFont(fonte);
		textEmail.setColumns(10);
		textEmail.setBounds(332, 109, 315, 23);
		painelCandidato.add(textEmail);
		
		Icon reportIcon  = ResourceManager.getIcon("icon/report.png",16,16);
		
		JButton botaoFicha = new JButton(reportIcon);
		botaoFicha.addActionListener((event) -> fichaCandidato(candidato));
		botaoFicha.setBounds(658, 109, 26, 23);
		botaoFicha.setToolTipText("Mostra a ficha com todos os dados deste candidato");
		painelCandidato.add(botaoFicha);
		
		JPanel painelConcurso = new JPanel();
		painelConcurso.setOpaque(false);
		painelConcurso.setBorder(GraphicsHelper.getInstance().getTitledBorder("Concurso"));
		painelConcurso.setBounds(12, 158, 696, 59);
		painelMaster.add(painelConcurso);
		painelConcurso.setLayout(null);
		
		JLabel labelNomeConcurso = new JLabel("Nome:");
		labelNomeConcurso.setForeground(label);
		labelNomeConcurso.setFont(fonte);
		labelNomeConcurso.setBounds(12, 28, 70, 15);
		painelConcurso.add(labelNomeConcurso);
		
		textNomeConcurso = new JTextField();
		textNomeConcurso.setHorizontalAlignment(SwingConstants.CENTER);
		textNomeConcurso.setEditable(false);
		textNomeConcurso.setForeground(color);
		textNomeConcurso.setFont(fonte);
		textNomeConcurso.setColumns(10);
		textNomeConcurso.setBounds(71, 26, 89, 20);
		painelConcurso.add(textNomeConcurso);
		
		JLabel labelCodigo = new JLabel("Código:");
		labelCodigo.setForeground(label);
		labelCodigo.setFont(fonte);
		labelCodigo.setBounds(178, 25, 70, 20);
		painelConcurso.add(labelCodigo);
		
		textCodigo = new JTextField();
		textCodigo.setHorizontalAlignment(SwingConstants.CENTER);
		textCodigo.setEditable(false);
		textCodigo.setForeground(color);
		textCodigo.setFont(fonte);
		textCodigo.setColumns(10);
		textCodigo.setBounds(242, 26, 63, 20);
		painelConcurso.add(textCodigo);
		
		JLabel labelInscricao = new JLabel("Última Inscrição:");
		labelInscricao.setForeground(label);
		labelInscricao.setFont(fonte);
		labelInscricao.setBounds(328, 25, 147, 20);
		painelConcurso.add(labelInscricao);
		
		textInscricao = new JTextField();
		textInscricao.setHorizontalAlignment(SwingConstants.CENTER);
		textInscricao.setEditable(false);
		textInscricao.setForeground(color);
		textInscricao.setFont(fonte);
		textInscricao.setColumns(10);
		textInscricao.setBounds(462, 26, 89, 20);
		painelConcurso.add(textInscricao);
		
		JLabel labelSituacao = new JLabel("Situação:");
		labelSituacao.setForeground(label);
		labelSituacao.setFont(fonte);
		labelSituacao.setBounds(569, 25, 89, 20);
		painelConcurso.add(labelSituacao);
		
		textSituacao = new JTextField();
		textSituacao.setEditable(false);
		textSituacao.setHorizontalAlignment(SwingConstants.CENTER);
		textSituacao.setForeground(color);
		textSituacao.setFont(fonte);
		textSituacao.setColumns(10);
		textSituacao.setBounds(649, 26, 35, 20);
		painelConcurso.add(textSituacao);
		
		carregaCandidato(candidato);
		
		setVisible(true);
	}
	
	/** Mostra a ficha do candidato atual */
	private void fichaCandidato(Candidato candidato) {
		
		try {
			FichaCandidato.show(candidato);
		} catch (Exception exception) {
			AlertDialog.error("Falha ao gerar visualização!");
		}
		
	}

	/** Carrega os dados do candidato atualmente selecionado para a janela */
	private void carregaCandidato(Candidato candidato) {
		
		if (candidato == null) return;
		
		textNome.setText(candidato.getNome(true));
		textSexo.setText(candidato.getSexo());
		textNascimento.setText(candidato.getDataNascimento());
		textRG.setText(candidato.getRGCompleto());
		textCPF.setText(candidato.getCPF(true));
		textEndereco.setText(candidato.getEnderecoCompleto());
		textFone.setText(candidato.getFone());
		textEmail.setText(candidato.getEmail());

		textNomeConcurso.setText(candidato.getConcurso());
		textCodigo.setText(candidato.getCodigoAsString());
		textInscricao.setText(candidato.getDataInscricao());
		textSituacao.setText("0");
		
		textSituacao.setToolTipText(candidato.getSituacao());
		
		//validaInfos(candidato);
		
	}
	
	/** Faz a validação das informações do candidato */
	private void validaInfos(Candidato candidato) {
		
		String cpf  = candidato.getCPF(false);
		String nome = candidato.getNome(false);
		String nasc = candidato.getDataNascimento();
		
		String message = detectSpaceBetweenWorsds(nome);
		
		int idadeCandidato = PhillsDateUtils.yearsSince(nasc,"yyyy-MM-dd");
		
		// PUT SOME CODE HERE!
		
		if (!CPFParser.parse(cpf))
			setError(textCPF, "CPF inválido!");
		
		if (idadeCandidato < 0)
			setError(textNascimento, "O candidato ainda nem nasceu!");
		
		else if (idadeCandidato < 10)
			setError(textNascimento, "O candidato possui só " + idadeCandidato + " anos?");
		
		if (message != null)
			setError(textNome, message);
		
		verificaRG(candidato);
	}
	
	public String detectSpaceBetweenWorsds(String phrase) {
		
		String retorno = null;
		int index = phrase.indexOf("  ");
		
		if (index != -1) {
			
			String first  = phrase.substring(0,index).trim();
			String second = phrase.substring(index).trim();
			
			retorno = String.format("Há múltiplos espaços entre \"%s\" e \"%s\"", first,second);
		}
		
		if (phrase.matches(".*\\d+.*"))
			retorno += "Há números cadastrados no nome do candidato!";
		
		return retorno;
	}
	
	private void verificaRG(Candidato candidato) {
		try {
			Long.parseLong(candidato.getRG().replace("-",""));
		}
		catch (Exception exception) {
			setError(textRG, "O RG possui letras!");
		}
	}
	
	private void setError(JComponent component, String hint) {
		component.setToolTipText(hint);
		component.setBackground (red );
	}
}
