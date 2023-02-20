package compec.ufam.consulta.view;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.phill.libs.ResourceManager;
import com.phill.libs.StringUtils;
import com.phill.libs.br.CPFTextField;
import com.phill.libs.i18n.PropertyBundle;
import com.phill.libs.table.JTableMouseListener;
import com.phill.libs.table.LockedTableModel;
import com.phill.libs.table.TableUtils;
import com.phill.libs.ui.AlertDialog;
import com.phill.libs.ui.DocumentChangeListener;
import com.phill.libs.ui.ESCDispose;
import com.phill.libs.ui.GraphicsHelper;
import com.phill.libs.ui.KeyReleasedListener;

import compec.ufam.consulta.model.*;
import compec.ufam.consulta.utils.*;

/** Classe que implementa a interface de busca e visualização de candidatos.
 *  @author Felipe André
 *  @version 2.0, 20/FEV/2023 */
public class TelaBuscaCandidato extends JFrame implements DocumentListener {

	// Serial
	private static final long serialVersionUID = 804215921125761987L;
	
	// Declaração de atributos gráficos
	
	private final JTable tableResultado;
	private final DefaultTableModel modelo;
	
	private final JComboBox<String> comboConcurso;
	private final JTextField textNome, textRG;
	private final JFormattedTextField textCPF;
	
	private final JButton botaoLimpar;
	private final JLabel labelInfos, textQtd;
	
	//private final PrintStream stdout, stderr;
	private JRadioButtonMenuItem itemRuntime;
	
	private Map<String, List<Candidato>> mapaCandidatos;
	private ArrayList<Candidato> listaFiltrados;
	
	// Carregando bundle de idiomas
	private final static PropertyBundle bundle = new PropertyBundle("i18n/tela-candidato-busca", null);
	private JCheckBox checkPagoIsento;

	public static void main(String[] args) {
		new TelaBuscaCandidato();
	}
	
	public TelaBuscaCandidato() {
		super("Busca de Candidato");
		
		// Inicializando atributos gráficos
		GraphicsHelper instance = GraphicsHelper.getInstance();
		//GraphicsHelper.setFrameIcon(this,"icon/windows-icon.png");
		ESCDispose.register(this);
		getContentPane().setLayout(null);
		
		// Recuperando ícones
		final Icon clearIcon = ResourceManager.getIcon("icon/brush.png", 20, 20);
		
		// Recuperando fontes e cores
		Font  fonte  = instance.getFont ();
		Font  ubuntu = instance.getUbuntuFont();
		Color color  = instance.getColor();
		
		// Painel 'Busca'
		JPanel panelBusca = new JPanel();
		panelBusca.setBorder(GraphicsHelper.getInstance().getTitledBorder("Busca"));
		panelBusca.setBounds(10, 10, 1004, 90);
		panelBusca.setLayout(null);
		getContentPane().add(panelBusca);
		
		JLabel labelNome = new JLabel("Nome:");
		labelNome.setHorizontalAlignment(JLabel.RIGHT);
		labelNome.setForeground(color);
		labelNome.setFont(fonte);
		labelNome.setBounds(10, 28, 50, 15);
		panelBusca.add(labelNome);
		
		textNome = new JTextField();
		textNome.setFont(fonte);
		textNome.setToolTipText(bundle.getString("hint-text-nome"));
		textNome.setBounds(65, 25, 929, 25);
		panelBusca.add(textNome);
		
		JLabel labelCPF = new JLabel("CPF:");
		labelCPF.setHorizontalAlignment(JLabel.RIGHT);
		labelCPF.setForeground(color);
		labelCPF.setFont(fonte);
		labelCPF.setBounds(10, 58, 50, 15);
		panelBusca.add(labelCPF);
		
		textCPF = new CPFTextField();
		textCPF.setHorizontalAlignment(CPFTextField.CENTER);
		textCPF.setFont(fonte);
		textCPF.setToolTipText(bundle.getString("hint-text-cpf"));
		textCPF.setBounds(65, 55, 125, 25);
		panelBusca.add(textCPF);
		
		JLabel labelRG = new JLabel("RG:");
		labelRG.setHorizontalAlignment(JLabel.RIGHT);
		labelRG.setForeground(color);
		labelRG.setFont(fonte);
		labelRG.setBounds(210, 58, 30, 15);
		panelBusca.add(labelRG);
		
		textRG = new JTextField();
		textRG.setFont(fonte);
		textRG.setToolTipText(bundle.getString("hint-text-rg"));
		textRG.setBounds(245, 55, 125, 25);
		panelBusca.add(textRG);
		
		JLabel labelConcurso = new JLabel("Concurso:");
		labelConcurso.setHorizontalAlignment(JLabel.RIGHT);
		labelConcurso.setForeground(color);
		labelConcurso.setFont(fonte);
		labelConcurso.setBounds(390, 58, 80, 15);
		panelBusca.add(labelConcurso);
		
		comboConcurso = new JComboBox<String>();
		comboConcurso.setFont(fonte);
		comboConcurso.setToolTipText(bundle.getString("hint-combo-concurso"));
		comboConcurso.setBounds(475, 55, 150, 25);
		panelBusca.add(comboConcurso);
		
		checkPagoIsento = new JCheckBox("Somente pagos e isentos");
		checkPagoIsento.setFont(fonte);
		checkPagoIsento.addActionListener((event) -> buscaCandidato());
		checkPagoIsento.setToolTipText(bundle.getString("hint-check-pago-isento"));
		checkPagoIsento.setBounds(645, 55, 210, 25);
		panelBusca.add(checkPagoIsento);
		
		botaoLimpar = new JButton(clearIcon);
		botaoLimpar.setToolTipText(bundle.getString("hint-button-clear"));
		botaoLimpar.addActionListener((event) -> limpar());
		botaoLimpar.setBounds(963, 55, 30, 25);
		panelBusca.add(botaoLimpar);
		
		// Painel 'Candidatos'
		JPanel panelCandidatos = new JPanel();
		panelCandidatos.setBorder(instance.getTitledBorder("Candidatos"));
		panelCandidatos.setBounds(10, 100, 1004, 450);
		panelCandidatos.setLayout(null);
		getContentPane().add(panelCandidatos);
		
		JScrollPane scrollResultado = new JScrollPane();
		scrollResultado.setBounds(10, 25, 984, 395);
		panelCandidatos.add(scrollResultado);
		
		this.modelo = new LockedTableModel(new String [] {"Concurso","Candidato","RG","CPF","Inscrição","Data de Insc.","Pago / Isento"});
		
		tableResultado = new JTable(modelo);
		tableResultado.setRowHeight(20);
		tableResultado.setFont(ubuntu);
		tableResultado.getTableHeader().setFont(fonte);
		tableResultado.addMouseListener(new JTableMouseListener(tableResultado));
		tableResultado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollResultado.setViewportView(tableResultado);
		
		final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumnModel columnModel = tableResultado.getColumnModel();

		columnModel.getColumn(0).setCellRenderer(centerRenderer);
		columnModel.getColumn(2).setCellRenderer(centerRenderer);
		columnModel.getColumn(3).setCellRenderer(centerRenderer);
		columnModel.getColumn(4).setCellRenderer(centerRenderer);
		columnModel.getColumn(5).setCellRenderer(centerRenderer);
		columnModel.getColumn(6).setCellRenderer(centerRenderer);
		
		columnModel.getColumn(0).setPreferredWidth( 45);
		columnModel.getColumn(1).setPreferredWidth(255);
		columnModel.getColumn(2).setPreferredWidth( 35);
		columnModel.getColumn(3).setPreferredWidth( 60);
		columnModel.getColumn(4).setPreferredWidth( 31);
		columnModel.getColumn(5).setPreferredWidth(103);
		columnModel.getColumn(6).setPreferredWidth( 53);
		
		JLabel labelQtd = new JLabel("Candidatos:");
		labelQtd.setHorizontalAlignment(JLabel.RIGHT);
		labelQtd.setForeground(color);
		labelQtd.setFont(fonte);
		labelQtd.setBounds(10, 425, 90, 15);
		panelCandidatos.add(labelQtd);
		
		textQtd = new JLabel("0");
		textQtd.setFont(fonte);
		textQtd.setBounds(105, 425, 70, 15);
		panelCandidatos.add(textQtd);
		
		// Fundo da janela
		labelInfos = new JLabel();
		labelInfos.setFont(fonte);
		labelInfos.setForeground(color);
		labelInfos.setBounds(10, 555, 1004, 27);
		getContentPane().add(labelInfos);

		// Listeners dos campos de texto
		DocumentListener docListener = (DocumentChangeListener) (event) -> buscaCandidato();
		
		textNome.getDocument().addDocumentListener(docListener);
		textCPF .getDocument().addDocumentListener(docListener);
		textRG  .getDocument().addDocumentListener(docListener);
		
		/*stdout = new PrintStream(new StdoutManager(labelInfos));
		stderr = new PrintStream(new StderrManager());
		
		System.setOut(stdout);
		System.setErr(stderr);*/
		
		onCreateOptionsPopupMenu();
		onCreateOptionsMenu();
		loadSheets();
		
		setSize(1024,640);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
	}
	
	/** Busca um candidato no banco de dados em memória. */
	private synchronized void buscaCandidato() {
		
		// Criando uma nova lista interna
		this.listaFiltrados = new ArrayList<Candidato>();
		
		// Recuperando dados da view
		String nomeCand  = textNome.getText().trim();
		String cpfCand   = textCPF .getText().trim();
		String rgCand    = textRG  .getText().trim();
		String concurso  = comboConcurso.getSelectedItem().toString();
		boolean inscrito = checkPagoIsento.isSelected();
		
		// Percorrendo todos os concursos
		for (List<Candidato> candidatos: mapaCandidatos.values()) {
			
			// Percorrendo todos os candidatos
			for (Candidato candidato: candidatos) {
				
				if (candidato.matches(nomeCand, cpfCand, rgCand, concurso, inscrito))
					this.listaFiltrados.add(candidato);
				
			}
			
		}
		
		// Ordenando a lista por nome de candidato
		Collections.sort(this.listaFiltrados, (cand1,cand2) -> cand1.getNome().compareToIgnoreCase(cand2.getNome()));
		
		// Atualizando a view
		TableUtils.load(modelo, this.listaFiltrados, textQtd);
		
	}
	
	
	
	
	
	
	
	
	@Override
	public void dispose() {
		
		//System.err.close();
		//stderr.close();
		
		super.dispose();
		
	}
	
	/** Inicializa a barra de menu e seus subitens */
	private void onCreateOptionsMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuArquivo = new JMenu("Arquivo");
		menuBar.add(menuArquivo);
		
		JMenuItem itemReload = new JMenuItem("Recarregar Planilhas");
		itemReload.addActionListener((event) -> loadSheets());
		menuArquivo.add(itemReload);
		
		JMenuItem itemDownload = new JMenuItem("Baixar Planilhas");
		itemDownload.addActionListener((event) -> downloadSheets());
		menuArquivo.add(itemDownload);
		
		JMenuItem itemSair = new JMenuItem("Sair");
		itemSair.addActionListener((event) -> dispose());
		
		menuArquivo.addSeparator();
		menuArquivo.add(itemSair);
		
		JMenu menuBusca = new JMenu("Busca");
		menuBar.add(menuBusca);
		
		itemRuntime = new JRadioButtonMenuItem("Busca em Tempo Real");
		itemRuntime.setSelected(false);
		itemRuntime.addActionListener((event) -> toggleRuntimeSearch(itemRuntime.isSelected()));
		menuBusca.add(itemRuntime);
		
		setJMenuBar(menuBar);
	}

	/** Adiciona um popup menu às linhas da tabela */
	private void onCreateOptionsPopupMenu() {
		
		JPopupMenu popupMenu = new JPopupMenu();
		
		JMenuItem itemFicha = new JMenuItem("Exibir Ficha");
		itemFicha.addActionListener((event) -> mostrarFicha());
		popupMenu.add(itemFicha);
		
		tableResultado.setComponentPopupMenu(popupMenu);
		
	}
	
	
	
	
	
	/** Exibe a ficha com os dados do candidato selecionado */
	private void mostrarFicha() {
		
		Candidato candidato = TableUtils.getSelected(tableResultado, listaFiltrados);
		
		if (candidato != null) {
			
			try {
				FichaCandidato.show(candidato);
			} catch (Exception exception) {
				AlertDialog.error("Falha ao gerar visualização!");
			}
			
		}
		
	}
	
	
	/** Carrega os candidatos para a memória */
	private void loadSheets() {
		
		Runnable job  = () -> carregaPlanilhas();
		Thread thread = new Thread(job);
		
		thread.setName("Thread de Carregamento de Planilhas");
		thread.start();
		
	}
	
	/** Faz o download de novas planilhas da rede */
	private void downloadSheets() {
		
		Runnable job  = () -> DownloadManager.updateSheets();
		Thread thread = new Thread(job);
		
		thread.setName("Thread de Download de Planilhas");
		thread.start();
		
	}
	
	/** Ativa ou desativa os campos de entrada de dados */
	private void turnFieldsEditable(boolean isEditable) {
		
		textNome.setEditable(isEditable);
		textCPF .setEditable(isEditable);
		textRG  .setEditable(isEditable);
		comboConcurso.setEnabled(isEditable);
		
		botaoLimpar.setEnabled(isEditable);
		
		if (isEditable && itemRuntime.isSelected())
			toggleRuntimeSearch(true);
		else
			toggleRuntimeSearch(false);
		
	}
	
	/** Ativa ou desativa a busca em tempo de execução */
	private void toggleRuntimeSearch(boolean enableRuntime) {
		
		if (enableRuntime) {
			textNome.getDocument().addDocumentListener(this);
			textRG  .getDocument().addDocumentListener(this);
			textCPF .getDocument().addDocumentListener(this);
			//comboConcurso.addActionListener(this);
		}
		else {
			textNome.getDocument().removeDocumentListener(this);
			textRG  .getDocument().removeDocumentListener(this);
			textCPF .getDocument().removeDocumentListener(this);
			//comboConcurso.removeActionListener(this);
		}
		
	}

	/** Limpa os campos de texto */
	private void limpar() {
		textNome.setText(null);
		textCPF.setText(null);
		textRG.setText(null);
		
		comboConcurso.setSelectedIndex(0);
		textNome.requestFocus();
	}
	
	@Override
	public void changedUpdate(DocumentEvent event) {
		buscaCandidato();
	}

	@Override
	public void insertUpdate(DocumentEvent event) {
		changedUpdate(event);
	}

	@Override
	public void removeUpdate(DocumentEvent event) {
		changedUpdate(event);
	}
	
	/** Método que atualiza as informações do comboBox */
	private void updateCombo(Set<String> keys) {
		
		comboConcurso.removeAllItems();
		
		for (String concurso: keys)
			comboConcurso.addItem(concurso);
		comboConcurso.addItem("Todos");

		comboConcurso.addActionListener((event) -> buscaCandidato());
		
	}
	
	/** Método que realiza o carregamento da lista de candidatos para a memória */
	private void carregaPlanilhas() {
		
		try {
			
			SwingUtilities.invokeLater(() -> turnFieldsEditable(false));
			
			mapaCandidatos = CandidatoDAO.load();
			
			/*candidatoDAO = new CandidatoDAO();
			candidatoDAO.load();
			candidatoDAO.sort();*/
			
			SwingUtilities.invokeLater(() -> updateCombo(mapaCandidatos.keySet()));
			
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		finally {
			SwingUtilities.invokeLater(() -> turnFieldsEditable(true));
		}
		
	}
}
