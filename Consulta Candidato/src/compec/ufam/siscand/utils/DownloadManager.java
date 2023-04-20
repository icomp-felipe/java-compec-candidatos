package compec.ufam.siscand.utils;

import java.io.*;
import java.net.*;

import javax.swing.*;

import jcifs.netbios.*;

import org.apache.commons.io.*;
import org.apache.commons.validator.routines.InetAddressValidator;

import com.phill.libs.*;

/** Classe responsável pelo download de planilhas do servidor descrito no arquivo de propriedades do sistema.
 *  @author Felipe André - felipeandresouza@hotmail.com
 *  @version 2.0, 20/ABR/2023 */
public class DownloadManager {

	// Atributo gráfico
	private final JLabel labelInfos;
	
	// Atributos de controle de download
	private long bytesDownloaded;
	private final int bufferSize, updateTime;
	private final URL  serverURL;
	private final File sheetsDir;
	private boolean interrupted;
	
	private static DownloadManager dw;
	
	/** Construtor privado inicializando atributos.
	 *  @param labelInfos - label para mostrar as atualizações 
	 *  @throws IOException quando algum erro de E/S ocorre */
	private DownloadManager(final JLabel labelInfos) throws IOException {
		
		this.labelInfos = labelInfos;
		
		this.bufferSize = 65536;
		this.updateTime = 250;
		this.serverURL  = utilRetrieveServer();
		this.sheetsDir  = ResourceManager.getResourceAsFile("sheets");
		
	}
	
	/** Baixa as planilhas de dados do servidor descrito no arquivo de propriedades do sistema.
	 *  @param labelInfos - label para mostrar as atualizações
	 *  @throws IOException quando algum erro de E/S ocorre */
	public static void run(final JLabel labelInfos) throws IOException {
		
		dw = new DownloadManager(labelInfos);
		dw.actionDownloadSheets();
		
	}
	
	/** Interrompe a operação de download. */
	public static void interrupt() {
		dw.interrupted = true;
	}
	
	/****************************** Bloco de Threads **************************************/
	
	/** Baixa as planilhas do servidor descrito no arquivo de propriedades.
	 *  @throws IOException quando os arquivos não podem ser baixados ou escritos no disco. */
	private void actionDownloadSheets() throws IOException {
		
		if (!interrupted) {
			
			// Recuperando lista de download
			String[] downloadList = utilRetrieveDownloadList();
			
			// Apaga todos os arquivos contidos em 'res/sheets'
			for (File arquivo: sheetsDir.listFiles())
				arquivo.delete();
			
			// Baixa as planilhas do servidor
			for (String remoteFile: downloadList) {
				
				URL  inputFile  = new URL (serverURL, remoteFile);
				File outputFile = new File(sheetsDir, remoteFile);
				
				actionDownloadFile(inputFile, outputFile);
				
			}
			
			// Informando a view
			utilMessageLabel("Planilhas baixadas com sucesso!");
			
		}
		
	}
	
	/** Baixa um <code>arqRemoto</code> para um <code>arqDestino</code>.
	 *  @param arqRemoto - link de um arquivo remoto
	 *  @param arqDestino - arquivo de destino
	 *  @throws IOException quando os arquivos não podem ser baixados ou escritos no disco. */
	private void actionDownloadFile(final URL arqRemoto, final File arqDestino) throws IOException {
		
		// Preparando variáveis de download
		int bytesRead = 0;
		final byte buffer[] = new byte[bufferSize];
		
		// Preparando streams
		final BufferedInputStream inputStream  = new BufferedInputStream(arqRemoto.openStream());
		final FileOutputStream    outputStream = new FileOutputStream   (arqDestino);
		
		// Inicializando o contador de bytes
		this.bytesDownloaded = 0;
		
		// Inicializando o monitor de eventos
		Thread downloadMonitor = new Thread(() -> downloadProgressThread(arqDestino));
		downloadMonitor.setName("Thread do monitor de download");
		downloadMonitor.start();
		
		// Realizando o download propriamente dito
		while ((bytesRead = inputStream.read(buffer, 0, bufferSize)) != -1) {
			
			if (interrupted) break;
			
        	bytesDownloaded += bytesRead;
        	outputStream.write(buffer, 0, bytesRead);
			
        }
		
		// Limpando recursos
		downloadMonitor.interrupt();
		
		inputStream .close();
		outputStream.close();
		
	}
	
	/************************** Bloco de Métodos Utilitários ******************************/
	
	/** @return Tamanho dos dados baixados em um formato amigável. */
	private String utilBytesDownloaded() {
		
		final double size = (double) this.bytesDownloaded;
		
		if (bytesDownloaded >= 1048576L)
			return String.format("%.2f MB", size/1048576F);
		
		if (bytesDownloaded >= 1024L)
			return String.format("%.2f KB", size/1024F);
		
		return String.format("%d bytes", bytesDownloaded);
		
	}
	
	/** Imprime uma mensagem no label.
	 *  @param message - mensagem a ser impressa */
	private void utilMessageLabel(final String message) {
		SwingUtilities.invokeLater(() -> this.labelInfos.setText(message));
	}
	
	/** Recupera a lista de downloads [index.txt] de planilhas do servidor.
	 *  @return Array com os nomes dos arquivos a serem baixados.
	 *  @throws IOException quando o arquivo 'index.txt' não pôde ser baixado do servidor. */
	private String[] utilRetrieveDownloadList() throws IOException {
		
		URL index = new URL(serverURL, "index.txt");
		String fileList = IOUtils.toString(index, "UTF-8");
		
		return fileList.split("\n");
	}
	
	/** Recupera o endereço IP do servidor através no nome netbios contido na variável 'net.home' do arquivo de propriedades.
	 *  @return URL do servidor
	 *  @throws MalformedURLException quando a variável 'net.home' é vazia ou não está no formato correto
	 *  @throws UnknownHostException quando o servidor não pode ser atingido */
	private URL utilRetrieveServer() throws IOException, MalformedURLException, UnknownHostException {
		
		utilMessageLabel("Buscando servidor...");
		
		String hostname = PropertiesManager.getString("net.home", null);
		String serverIP = hostname.toLowerCase().equals("localhost") ? "127.0.0.1" : InetAddressValidator.getInstance().isValid(hostname) ? hostname : NbtAddress.getByName(hostname).getHostAddress();
			
		return new URL("http://" + serverIP);
		
	}
	
	/****************************** Bloco de Threads **************************************/

	/** Thread do monitor de download.
	 *  @param planilha - planilha sendo escrita no disco */
	private void downloadProgressThread(final File planilha) {
		
		final String regex = String.format("Baixando planilha \"%s\"...", planilha.getName());
		
		do {
			
			try {
				
				utilMessageLabel(regex + utilBytesDownloaded());
				Thread.sleep(this.updateTime);
				
			}
			catch (InterruptedException exception) {
				return;
			}
			
		} while (true);
		
	}
	
}