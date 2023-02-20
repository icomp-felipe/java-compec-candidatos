package compec.ufam.consulta.view;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.phill.libs.StringUtils;
import com.phill.libs.br.CPFTextField;
import com.phill.libs.table.JTableMouseListener;
import com.phill.libs.table.LockedTableModel;
import com.phill.libs.table.TableUtils;
import com.phill.libs.ui.AlertDialog;
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
    private final String[] colunas = new String [] {"Concurso","Inscrição","Nome do Candidato","Data de Nasc.","RG","CPF"};
	private final DefaultTableModel modelo;
	
	private final JComboBox<String> comboConcurso;
	private final JTextField textNome, textRG;
	private final JFormattedTextField textCPF;
	
	private final JButton botaoLimpar, botaoBuscar;
	private final JLabel labelInfos, textQtd;
	
	//private final PrintStream stdout, stderr;
	private JRadioButtonMenuItem itemRuntime;
	
	private ArrayList<Candidato> listaFiltrados;

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
		//final Icon clearIcon = ResourceManager.getIcon("icon/brush.png", 20, 20);
		
		// Recuperando fontes e cores
		Font  fonte  = instance.getFont ();
		Font  ubuntu = instance.getUbuntuFont();
		Color color  = instance.getColor();
		
		// Painel 'Busca'
		JPanel painelBusca = new JPanel();
		painelBusca.setBorder(GraphicsHelper.getInstance().getTitledBorder("Busca"));
		painelBusca.setBounds(12, 12, 776, 83);
		painelBusca.setLayout(null);
		getContentPane().add(painelBusca);
		
		JLabel labelNome = new JLabel("Nome:");
		labelNome.setForeground(color);
		labelNome.setFont(fonte);
		labelNome.setBounds(12, 26, 70, 15);
		painelBusca.add(labelNome);
		
		textNome = new JTextField();
		textNome.setFont(fonte);
		textNome.setBounds(63, 24, 701, 20);
		painelBusca.add(textNome);
		
		JLabel labelCPF = new JLabel("CPF:");
		labelCPF.setForeground(color);
		labelCPF.setFont(fonte);
		labelCPF.setBounds(12, 53, 70, 15);
		painelBusca.add(labelCPF);
		
		textCPF = new CPFTextField();
		textCPF.setFocusLostBehavior(JFormattedTextField.PERSIST);
		textCPF.setFont(fonte);
		textCPF.setBounds(63, 51, 121, 20);
		painelBusca.add(textCPF);
		
		JLabel labelRG = new JLabel("RG:");
		labelRG.setForeground(color);
		labelRG.setFont(fonte);
		labelRG.setBounds(196, 53, 70, 15);
		painelBusca.add(labelRG);
		
		textRG = new JTextField();
		textRG.setFont(fonte);
		textRG.setBounds(234, 51, 121, 20);
		painelBusca.add(textRG);
		
		JLabel labelConcurso = new JLabel("Concurso:");
		labelConcurso.setForeground(color);
		labelConcurso.setFont(fonte);
		labelConcurso.setBounds(367, 53, 87, 15);
		painelBusca.add(labelConcurso);
		
		comboConcurso = new JComboBox<String>();
		comboConcurso.setFont(fonte);
		comboConcurso.setBounds(448, 51, 121, 20);
		painelBusca.add(comboConcurso);
		
		botaoBuscar = new JButton("Buscar");
		botaoBuscar.addActionListener((event) -> buscaCandidato());
		botaoBuscar.setBounds(581, 51, 87, 20);
		painelBusca.add(botaoBuscar);
		
		botaoLimpar = new JButton("Limpar");
		botaoLimpar.addActionListener((event) -> limpar());
		botaoLimpar.setBounds(677, 51, 87, 20);
		painelBusca.add(botaoLimpar);
		
		
		
		
		setSize(800,500);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		

		

		

		

		

		

		

		

		
		KeyListener keyListener = (KeyReleasedListener) (event) -> { if (event.getKeyCode() == KeyEvent.VK_ENTER) botaoBuscar.doClick(); };
		
		textNome.addKeyListener(keyListener);
		textCPF .addKeyListener(keyListener);
		textRG  .addKeyListener(keyListener);
		
		JPanel painelResultado = new JPanel();
		painelResultado.setBorder(GraphicsHelper.getInstance().getTitledBorder("Resultados"));
		painelResultado.setBounds(12, 99, 776, 315);
		getContentPane().add(painelResultado);
		painelResultado.setLayout(null);
		
		modelo = new LockedTableModel(colunas);
		
		tableResultado = new JTable(modelo);
		tableResultado.addMouseListener(new JTableMouseListener(tableResultado));
		tableResultado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		tableResultado.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		
		for (int i=3; i<=5; i++)
			tableResultado.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		
		tableResultado.getColumnModel().getColumn(0).setPreferredWidth(10);
		tableResultado.getColumnModel().getColumn(1).setPreferredWidth(10);
		tableResultado.getColumnModel().getColumn(2).setPreferredWidth(200);
		tableResultado.getColumnModel().getColumn(3).setPreferredWidth(50);
		tableResultado.getColumnModel().getColumn(4).setPreferredWidth(50);
		tableResultado.getColumnModel().getColumn(5).setPreferredWidth(50);
		
		JScrollPane scrollResultado = new JScrollPane(tableResultado);
		scrollResultado.setBounds(12, 22, 752, 260);
		painelResultado.add(scrollResultado);
		
		JLabel labelQtd = new JLabel("Candidatos Encontrados:");
		labelQtd.setForeground(color);
		labelQtd.setFont(fonte);
		labelQtd.setBounds(15, 290, 204, 15);
		painelResultado.add(labelQtd);
		
		textQtd = new JLabel("0");
		textQtd.setFont(fonte);
		textQtd.setForeground(color);
		textQtd.setBounds(199, 290, 70, 15);
		painelResultado.add(textQtd);
		
		labelInfos = new JLabel();
		labelInfos.setFont(fonte);
		labelInfos.setForeground(color);
		labelInfos.setBounds(12, 415, 776, 27);
		getContentPane().add(labelInfos);
		
		/*stdout = new PrintStream(new StdoutManager(labelInfos));
		stderr = new PrintStream(new StderrManager());
		
		System.setOut(stdout);
		System.setErr(stderr);*/
		
		onCreateOptionsPopupMenu();
		onCreateOptionsMenu();

		setVisible(true);
		
		loadSheets();
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
		
		JMenu menuAjuda = new JMenu("Ajuda");
		menuBar.add(menuAjuda);
		
		JMenuItem itemSobre = new JMenuItem("Sobre");
		itemSobre.addActionListener((event) -> sobre());
		menuAjuda.add(itemSobre);
		
		setJMenuBar(menuBar);
	}

	/** Adiciona um popup menu às linhas da tabela */
	private void onCreateOptionsPopupMenu() {
		
		JPopupMenu popupMenu = new JPopupMenu();
		
		JMenuItem itemExpandir  = new JMenuItem("Expandir Candidato");
		itemExpandir.addActionListener((event) -> expandir());
		popupMenu.add(itemExpandir);
		
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
	
	/** Busca um candidato no banco de dados em memória */
	private synchronized void buscaCandidato() {
		
		String aux = comboConcurso.getSelectedItem().toString();
		String conc = aux.equals("Todos") ? "" : aux;
		
		/** Preparing String for match */
		String nome = textNome.getText().toUpperCase().replace("%",".*");
		String iden = textRG  .getText().replace("%",".*");
		String cpfs = StringUtils.extractNumbers(textCPF.getText());
		
		buscaCandidatos(conc, nome, iden, cpfs);
		
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
		
		botaoBuscar.setEnabled(isEditable);
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
			botaoBuscar.setVisible(false);
		}
		else {
			textNome.getDocument().removeDocumentListener(this);
			textRG  .getDocument().removeDocumentListener(this);
			textCPF .getDocument().removeDocumentListener(this);
			//comboConcurso.removeActionListener(this);
			botaoBuscar.setVisible(true);
		}
		
	}

	/** Exibe informações completas de um candidato */
	private void expandir() {
		
		Candidato candidato = TableUtils.getSelected(tableResultado, listaFiltrados);
		
	}
	
	/** Exibe informações legais do programa */
	private void sobre() {
		final String message = "Sistema de Consulta de Candidatos v.1.5\n"
							 + "Criado por: Felipe André\n"
							 + "2016(c) Todos os direitos reservados";
		AlertDialog.info(message);
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
		
		textNome.requestFocus();
		
	}
	
	/** Exibe os candidatos que contém os mesmos dados informados nos campos de texto */
	private void buscaCandidatos(String concurso, String nome, String rg, String cpf) {
		
		/*listaFiltrados = candidatoDAO.buscaCandidato(concurso, nome, rg, cpf);
		SwingUtilities.invokeLater(() -> TableUtils.load(modelo, listaFiltrados, textQtd));*/
		
	}
	
	Map<String, List<Candidato>> mapaCandidatos;
	
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
