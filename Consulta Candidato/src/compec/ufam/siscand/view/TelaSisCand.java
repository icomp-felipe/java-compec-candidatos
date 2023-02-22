package compec.ufam.siscand.view;

import java.io.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.event.*;

import javax.swing.event.*;
import javax.swing.table.*;

import com.phill.libs.*;
import com.phill.libs.br.*;
import com.phill.libs.ui.*;
import com.phill.libs.sys.*;
import com.phill.libs.i18n.*;
import com.phill.libs.table.*;

import compec.ufam.siscand.model.*;
import compec.ufam.siscand.utils.*;

/** Classe que implementa a interface de busca e visualização de candidatos.
 *  @author Felipe André - felipeandresouza@hotmail.com
 *  @version 2.0, 22/FEV/2023 */
public class TelaSisCand extends JFrame {

	// Serial
	private static final long serialVersionUID = 804215921125761987L;
	
	// Atributos gráficos
	private final JTextField textNome, textRG;
	private final JFormattedTextField textCPF;
	private final JComboBox<String> comboConcurso;
	private final JCheckBox checkPagoIsento;
	private final JButton buttonClear, buttonRefresh, buttonDownload;
	private final JTable tableCandidatos;
	private final DefaultTableModel modelo;
	private final JLabel labelInfos, textQtd;
	private final ImageIcon loadingIcon;
	
	// Atributos dinâmicos
	private boolean loading;
	private Map<String, List<Candidato>> mapaCandidatos;
	private ArrayList<Candidato> listaFiltrados;
	
	// Redirecionamento da stream de erros
	private final PrintStream stderr;
	
	// Carregando bundle de idiomas
	private final static PropertyBundle bundle = new PropertyBundle("i18n/tela-candidato-busca", null);
	
	public static void main(String[] args) {
		new TelaSisCand();
	}
	
	public TelaSisCand() {
		super("SisCand v.2.0 - Lista de Candidatos");
		
		// Inicializando atributos gráficos
		GraphicsHelper instance = GraphicsHelper.getInstance();
		GraphicsHelper.setFrameIcon(this,"icon/siscand-icon.png");
		ESCDispose.register(this);
		getContentPane().setLayout(null);
		
		// Recuperando ícones
		final Icon clearIcon    = ResourceManager.getIcon("icon/brush.png"          , 20, 20);
		final Icon downloadIcon = ResourceManager.getIcon("icon/globe_3.png"        , 20, 20);
		final Icon refreshIcon  = ResourceManager.getIcon("icon/playback_reload.png", 20, 20);
		
		this.loadingIcon = new ImageIcon(ResourceManager.getResource("icon/loading.gif"));
		
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
		checkPagoIsento.addActionListener((event) -> actionBusca());
		checkPagoIsento.setToolTipText(bundle.getString("hint-check-pago-isento"));
		checkPagoIsento.setBounds(645, 55, 210, 25);
		panelBusca.add(checkPagoIsento);
		
		buttonClear = new JButton(clearIcon);
		buttonClear.setToolTipText(bundle.getString("hint-button-clear"));
		buttonClear.addActionListener((event) -> utilClear());
		buttonClear.setBounds(963, 55, 30, 25);
		panelBusca.add(buttonClear);
		
		// Painel 'Candidatos'
		JPanel panelCandidatos = new JPanel();
		panelCandidatos.setBorder(instance.getTitledBorder("Candidatos"));
		panelCandidatos.setBounds(10, 100, 1004, 468);
		panelCandidatos.setLayout(null);
		getContentPane().add(panelCandidatos);
		
		JScrollPane scrollResultado = new JScrollPane();
		scrollResultado.setBounds(10, 25, 984, 413);
		panelCandidatos.add(scrollResultado);
		
		this.modelo = new LockedTableModel(new String [] {"Concurso","Candidato","RG","CPF","Inscrição","Data de Insc.","Pago / Isento"});
		
		tableCandidatos = new JTable(modelo);
		tableCandidatos.setRowHeight(20);
		tableCandidatos.setFont(ubuntu);
		tableCandidatos.getTableHeader().setFont(fonte);
		tableCandidatos.addMouseListener(new JTableMouseListener(tableCandidatos));
		tableCandidatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollResultado.setViewportView(tableCandidatos);
		
		final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumnModel columnModel = tableCandidatos.getColumnModel();

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
		labelQtd.setBounds(10, 443, 90, 15);
		panelCandidatos.add(labelQtd);
		
		textQtd = new JLabel("0");
		textQtd.setFont(fonte);
		textQtd.setBounds(105, 443, 70, 15);
		panelCandidatos.add(textQtd);
		
		// Fundo da janela
		labelInfos = new JLabel();
		labelInfos.setFont(fonte);
		labelInfos.setBounds(10, 575, 925, 25);
		getContentPane().add(labelInfos);
		
		buttonRefresh = new JButton(refreshIcon);
		buttonRefresh.addActionListener((event) -> threadLoadSheets());
		buttonRefresh.setToolTipText(bundle.getString("hint-button-refresh"));
		buttonRefresh.setBounds(943, 575, 30, 25);
		getContentPane().add(buttonRefresh);

		buttonDownload = new JButton(downloadIcon);
		buttonDownload.addActionListener((event) -> threadDownloadSheets());
		buttonDownload.setToolTipText(bundle.getString("hint-button-download"));
		buttonDownload.setBounds(983, 575, 30, 25);
		getContentPane().add(buttonDownload);
		
		// Listeners dos campos de texto
		DocumentListener docListener = (DocumentChangeListener) (event) -> actionBusca();
		
		textNome.getDocument().addDocumentListener(docListener);
		textCPF .getDocument().addDocumentListener(docListener);
		textRG  .getDocument().addDocumentListener(docListener);
		
		this.stderr = new PrintStream(new StderrManager()); System.setErr(stderr);
		
		createPopupMenu();
		threadLoadSheets();
		
		comboConcurso.addActionListener((event) -> actionBusca());
		
		setSize(1024,640);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
	}
	
	/** Adiciona um popup menu às linhas da tabela */
	private void createPopupMenu() {
		
		JPopupMenu popupMenu = new JPopupMenu();
		
		// Definindo aceleradores
		KeyStroke visualizar = KeyStroke.getKeyStroke(KeyEvent.VK_V, 0);
		KeyStroke imprimir   = KeyStroke.getKeyStroke(KeyEvent.VK_I, 0);
		KeyStroke email      = KeyStroke.getKeyStroke(KeyEvent.VK_E, 0);
		KeyStroke whatsapp   = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0);
		
		// Definindo ações dos itens de menu
		Action actionVisualizar = new ShortcutAction("Visualizar (PDF)"          , KeyEvent.VK_V, visualizar, (event) -> actionReport  ());
		Action actionImprimir   = new ShortcutAction("Imprimir diretamente"      , KeyEvent.VK_I, imprimir  , (event) -> actionPrint   ());
		Action actionEmail      = new ShortcutAction("Enviar e-mail"             , KeyEvent.VK_E, email     , (event) -> actionEmail   ());
		Action actionWhatsapp   = new ShortcutAction("Enviar mensagem (Whatsapp)", KeyEvent.VK_W, whatsapp  , (event) -> actionWhatsapp());
		
		// Declarando os itens de menu
		JMenuItem itemFicha = new JMenuItem(actionVisualizar);
		popupMenu.add(itemFicha);
		
		JMenuItem itemImprimir = new JMenuItem(actionImprimir);
		popupMenu.add(itemImprimir);
		
		popupMenu.addSeparator();
		
		JMenuItem itemEmail = new JMenuItem(actionEmail);
		popupMenu.add(itemEmail);
		
		JMenuItem itemWhatsapp = new JMenuItem(actionWhatsapp);
		popupMenu.add(itemWhatsapp);
		
		// Definindo atalhos de teclado
		final InputMap  imap = tableCandidatos.getInputMap (JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		final ActionMap amap = tableCandidatos.getActionMap();
		
		imap.put(visualizar, "actionVisualizar");
		imap.put(imprimir  , "actionImprimir"  );
		imap.put(email     , "actionEmail"     );
		imap.put(whatsapp  , "actionWhatsapp"  );
		
		amap.put("actionVisualizar", actionVisualizar);
		amap.put("actionImprimir"  , actionImprimir  );
		amap.put("actionEmail"     , actionEmail     );
		amap.put("actionWhatsapp"  , actionWhatsapp  );
		
		// Atribuindo menu à tabela
		tableCandidatos.setComponentPopupMenu(popupMenu);
		
	}
	
	/********************** Bloco de Tratamento de Eventos ********************************/
	
	/** Motor de busca de candidato. */
	private synchronized void actionBusca() {
		
		// Previne execução desse método quando a view está sendo atualizada por outros métodos 
		if (!this.loading) {
			
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
			
	}
	
	/** Abre um novo email com o endereço do candidato selecionado na tabela. */
	private void actionEmail() {
		
		// Recupera o candidato selecionado na tabela
		final Candidato candidato = TableUtils.getSelected(tableCandidatos, listaFiltrados);
		
		if (candidato != null)
			HostUtils.sendEmail(candidato.getEmail());
		
	}
	
	/** Imprime diretamente na impressora padrão do sistema os dados do candidato selecionado. */
	private void actionPrint() {
		
		// Recupera o candidato selecionado na tabela
		final Candidato candidato = TableUtils.getSelected(tableCandidatos, listaFiltrados);

		if (candidato != null) {
			
			// Método de alto processamento, logo, vai para uma Thread
			Thread printThread = new Thread(() -> {
			
				try {
					
					utilMessageLabel(bundle.getString("buscand-action-print-loadMsg"), true);
					
					FichaCandidato.print(candidato);
						
				} catch (Exception exception) {
					
					exception.printStackTrace();
					AlertDialog.error(bundle.getString("buscand-action-print-title"), bundle.getString("buscand-action-print-error"));
						
				}
				finally {
					
					utilMessageLabel(null, false);
					
				}
				
			});
			
			printThread.setName(bundle.getString("buscand-action-print-thread"));
			printThread.start();
			
		}
		
	}
	
	/** Exibe a ficha com os dados do candidato selecionado na tabela. */
	private void actionReport() {
		
		// Recupera o candidato selecionado na tabela
		final Candidato candidato = TableUtils.getSelected(tableCandidatos, listaFiltrados);
		
		if (candidato != null) {
			
			// Método de alto processamento, logo, vai para uma Thread
			Thread reportThread = new Thread(() -> {
			
				try {
					
					utilMessageLabel(bundle.getString("buscand-action-report-loadMsg"), true);
					
					FichaCandidato.show(candidato);
						
				} catch (Exception exception) {
					
					exception.printStackTrace();
					AlertDialog.error(bundle.getString("buscand-action-report-title"), bundle.getString("buscand-action-report-error"));
						
				}
				finally {
					
					utilMessageLabel(null, false);
					
				}
				
			});
			
			reportThread.setName(bundle.getString("buscand-action-report-thread"));
			reportThread.start();
			
		}
		
	}
	
	/** Abre um novo chat no WhatsApp (PC) com o número do candidato selecionado na tabela. */
	private void actionWhatsapp() {
		
		// Recupera o candidato selecionado na tabela
		final Candidato candidato = TableUtils.getSelected(tableCandidatos, listaFiltrados);
		
		if (candidato != null)
			HostUtils.sendWhatsApp(candidato.getWhatsApp(), null);
		
	}
	
	@Override
	public void dispose() {
		
		System.err.close(); stderr.close();
		
		super.dispose();
		
	}
	
	/************************** Bloco de Métodos Utilitários ******************************/
	
	/** Limpa os dados de pesquisa. */
	private void utilClear() {
		
		SwingUtilities.invokeLater(() -> {
			
			textNome.setText(null);
			textCPF .setText(null);
			textRG  .setText(null);
			
			comboConcurso  .setSelectedIndex(0);
			checkPagoIsento.setSelected(false);
			
			textNome.requestFocus();
			
		});
		
	}
	
	/** Ativa ou desativa os campos de entrada de dados.
	 *  @param lock - tranca ou destranca os campos de pesquisa */
	private void utilLockFields(final boolean lock) {
		
		final boolean enable = !lock;
		
		SwingUtilities.invokeLater(() -> {
		
			textNome       .setEditable(enable);
			textCPF        .setEditable(enable);
			textRG         .setEditable(enable);
			comboConcurso  .setEnabled (enable);
			checkPagoIsento.setEnabled (enable);
			buttonClear    .setEnabled (enable);
			
			tableCandidatos.setEnabled(enable);
			
			buttonRefresh .setEnabled(enable);
			buttonDownload.setEnabled(enable);
			
		});
		
	}
	
	/** Exibe uma mensagem no label de informações.
	 *  @param message - mensagem a ser exibida. Se 'null', o label é ocultado
	 *  @param showLoadingIcon - exibe ou não o ícone de carregamento */
	private void utilMessageLabel(final String message, final boolean showLoadingIcon) {
		
		SwingUtilities.invokeLater(() -> {
			
			if (message == null)
				labelInfos.setVisible(false);
			
			else {
				
				labelInfos.setVisible(true);
				labelInfos.setText(message);
				labelInfos.setIcon(showLoadingIcon ? loadingIcon : null);
				
			}
			
		});
		
	}
	
	/** Carrega os concursos para o comboBox. */
	private void utilUpdateCombo() {
		
		SwingUtilities.invokeLater(() -> {
		
			this.loading = true;
			
			comboConcurso.removeAllItems();
			
			for (String concurso: mapaCandidatos.keySet())
				comboConcurso.addItem(concurso);
			
			comboConcurso.addItem("Todos");
			
			this.loading = false;
			
		});
		
	}
	
	/****************************** Bloco de Threads **************************************/
	
	/** Faz o download de novas planilhas da rede. */
	private void threadDownloadSheets() {
		
		Thread downloadThread = new Thread(() -> {
			
			try {
				
				DownloadManager.run(labelInfos);
				
			} catch (IOException exception) {
				
				exception.printStackTrace();
				AlertDialog.error(bundle.getString("buscand-thread-download-title"), bundle.getString("buscand-thread-download-error"));
				
			}
		});
		
		downloadThread.setName(bundle.getString("buscand-thread-download-title"));
		downloadThread.start();
		
	}
	
	/** Cria um mapa com os dados provenientes das planilhas de listagens de candidatos. */
	private void threadLoadSheets() {
		
		Thread loadThread = new Thread(() -> {
		
			try {
	
				utilLockFields(true);
				
				mapaCandidatos = CandidatoDAO.load();
				
				utilUpdateCombo();
				
				
			}
			catch (Exception exception) {
				
				exception.printStackTrace();
				AlertDialog.error(bundle.getString("buscand-thread-sheets-title"), bundle.getString("buscand-thread-sheets-error"));
				
			}
			finally {
				
				utilLockFields(false);
				utilClear();
				
			}
			
		});
		
		loadThread.setName(bundle.getString("buscand-thread-sheets-thead"));
		loadThread.start();
		
	}
}