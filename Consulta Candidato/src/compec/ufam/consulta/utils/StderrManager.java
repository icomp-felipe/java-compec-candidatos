package compec.ufam.consulta.utils;

import java.io.*;
import com.phill.libs.*;
import com.phill.libs.time.*;

public class StderrManager  extends OutputStream {
	
	private FileOutputStream outputStream;
	private File traceFile;
	
	public StderrManager() {
		
		try {
			traceFile    = getStackTraceFile  ();
			outputStream = getStackTraceStream();
		}
		catch (IOException exception) {
			AlertDialog.erro("Falha ao criar o arquivo de log!");
		}
		
	}
	
	@Override
	/** Fecha o arquivo de log e o deleta, se ele estiver vazio */
	public void close() throws IOException {
		
		super.flush();
		outputStream.flush();
		outputStream.close();
		
		removeBlank();
	}
	
	@Override
	/** Método básico de escrita de dados */
	public void write(int args) throws IOException {
		write(new byte[] {(byte) args}, 0, 1);
	}
	
	@Override
	/** Escreve um charSequence no arquivo de log */
	public synchronized void write(byte[] charSequence, int offset, int length) throws IOException {
		registerStackTrace(charSequence,offset,length);
	}
	
	/** Remove um arquivo, se este for vazio */
	private void removeBlank() {
		
		if (traceFile.length() == 0L)
			traceFile.delete();
			
	}
	
	/** Escreve o stack trace em arquivo */
	private void registerStackTrace(byte[] charSequence, int offset, int length) {
		try {
			outputStream.write(charSequence,offset,length);
			outputStream.flush();
		}
		catch (IOException exception) {
		}
	}
	

	/** Cria o arquivo de debug */
	private File getStackTraceFile() {
		
		String curdate  = DateUtils.getSystemDate("dd.MM.yyyy_HH.mm.ss");
		String filename = String.format("stackTrace_%s.txt",curdate);
		String stackDir = ResourceManager.getResource("tracing/");
		
		return new File(stackDir+filename);
	}
	
	/** Cria uma stream com o arquivo de debug */
	private FileOutputStream getStackTraceStream() throws IOException {
		return new FileOutputStream(traceFile);
	}

}
