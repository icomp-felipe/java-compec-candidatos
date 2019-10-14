package compec.ufam.consulta.model;

import jxl.*;
import java.io.*;
import java.util.*;

import com.phill.libs.PhillFileUtils;
import com.phill.libs.ResourceManager;

public class CandidatoDAO {
	
	private final File sheetsDirectory;
	private final Vector<String> listaConcursos;
	private final ArrayList<Candidato> listaCandidatos;
	
	private static final Comparator<Candidato> comparadorCandidato = (c1,c2) -> c1.getNome(false).compareToIgnoreCase(c2.getNome(false));
	
	public CandidatoDAO() throws Exception {
		
		this.listaConcursos  = new Vector<String>();
		this.listaCandidatos = new ArrayList<Candidato>();
		this.sheetsDirectory = new File(ResourceManager.getResource("sheets"));
		
	}
	
	/************************* Bloco de Getters **********************************************/
	
	/** Retorna a lista de concursos */
	public Vector<String> getConcursos() {
		return listaConcursos;
	}
	
	/** Retorna a lista de candidatos */
	public ArrayList<Candidato> getListaCandidatos() {
		return listaCandidatos;
	}
	
	/************ Bloco de Métodos Públicos de Manipulação da Lista de Candidatos ************/
	
	/** Carrega todas as planilhas de candidatos para a lista em memória */
	public void load() {
		
		File[] listaArquivos = PhillFileUtils.orderedListFiles(sheetsDirectory);
		
		for (File planilha: listaArquivos) {
			
			Sistema versao = getVersaoSistema(planilha);
			loadCandidatosFromFile(planilha, versao);
			
		}
		
	}
	
	/** Ordena a lista de candidatos */
	public void sort() {
		
		System.out.print("Ordenando lista de candidatos...");
		
		Collections.sort(listaCandidatos,comparadorCandidato);
		
		System.out.println();
		
	}
	
	/** Retorna uma lista de candidatos que possuem os mesmos dados informados via parâmetro */
	public synchronized ArrayList<Candidato> buscaCandidato(String concurso, String nome, String rg, String cpf) {
		
		ArrayList<Candidato> listaFiltrados = new ArrayList<Candidato>();
		
		String regex = String.format("%s.*-%s.*-%s.*-%s.*",concurso,nome,rg,cpf);
		
		for (Candidato candidato: listaCandidatos)
			if (candidato.matches(regex))
				listaFiltrados.add(candidato);
		
		return listaFiltrados;
		
	}
	
	/************** Bloco de Métodos de Manipulação das Planilhas ****************************/
	
	/** Carrega a lista de candidatos a partir de um arquivo */
	private void loadCandidatosFromFile(File planilha, Sistema versao) {
		
		if (versao == null) return;
		
		try {
			
			System.out.print("Carregando planilha \"" + planilha.getName() + "\"...");
			Workbook workbook = Workbook.getWorkbook(planilha);
			
			final Sheet sheet = workbook.getSheet(0);
			final int rows = sheet.getRows();
			
			/** Lê o concurso */
			listaConcursos.add(sheet.getCell(0,1).getContents());
			
			for(int i = 1; i < rows; i++) {
				Candidato candidato = getCandidatoFromRow(sheet,i,versao);
				listaCandidatos.add(candidato);
			}
			
			System.out.println();
			workbook.close();
			
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		
	}
	
	/** Carrega um candidato a partir de uma linha da planilha */
	private Candidato getCandidatoFromRow(Sheet sheet, int currentRow, Sistema versao) {
		
		final int[] rowSet = versao.getRowSet();
		
		String concurso   = sheet.getCell(rowSet[0], currentRow).getContents();
		int codigo		  = Integer.parseInt(sheet.getCell(rowSet[1], currentRow).getContents());
		String nome       = sheet.getCell(rowSet[2], currentRow).getContents();
		String sexo	 	  = sheet.getCell(rowSet[3], currentRow).getContents();
		String nascimento = sheet.getCell(rowSet[4], currentRow).getContents();
		
		String rg			 = sheet.getCell(rowSet[5], currentRow).getContents();
		String orgaoEmissor  = sheet.getCell(rowSet[6], currentRow).getContents();
		String estadoEmissor = sheet.getCell(rowSet[7], currentRow).getContents();
		String cpf 			 = sheet.getCell(rowSet[8], currentRow).getContents();
		String dataInscricao = sheet.getCell(rowSet[9], currentRow).getContents();
		
		Situacao situacao    = SituacaoDAO.getSituacao(sheet.getCell(rowSet[10], currentRow).getContents());
		
		String rua 	   = sheet.getCell(rowSet[11], currentRow).getContents();
		String numCasa = sheet.getCell(rowSet[12], currentRow).getContents();
		String bairro  = sheet.getCell(rowSet[13], currentRow).getContents();
		String cep 	   = sheet.getCell(rowSet[14], currentRow).getContents();
		
		String cidade = sheet.getCell(rowSet[15], currentRow).getContents();
		String estado = sheet.getCell(rowSet[16], currentRow).getContents();
		String fone   = sheet.getCell(rowSet[17], currentRow).getContents();
		String email  = sheet.getCell(rowSet[18], currentRow).getContents();
		
		Candidato candidato = new Candidato(concurso, codigo, nome, sexo, nascimento, rg, orgaoEmissor, estadoEmissor, cpf, dataInscricao, situacao, rua, numCasa, bairro, cep, cidade, estado, fone, email, versao);
		
		return candidato;
	}
	
	/** Identifica a versão do sistema de origem da planilha */
	private Sistema getVersaoSistema(File arquivo) {
		
		String filename = arquivo.getName().toLowerCase();
		
		if (!filename.endsWith(".xls"))
			return null;

		if (filename.contains("newsys"))
			return Sistema.NOVO;
		
		if (filename.contains("oldsys"))
			return Sistema.ANTIGO;
		
		return null;
		
	}
	
}
