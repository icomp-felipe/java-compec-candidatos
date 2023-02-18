package compec.ufam.consulta.model;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.phill.libs.ResourceManager;
import com.phill.libs.files.PhillFileUtils;

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
	
	/** Carrega todas as planilhas de candidatos para a memória. */
	public void load() {
		
		File[] listaArquivos = PhillFileUtils.listFilesOrdered(sheetsDirectory, PhillFileUtils.ASCENDING);
		
		for (File planilha: listaArquivos) {
			
			if (planilha.getName().endsWith(".xls"))
				loadCandidatosFromFile(planilha);
			
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
	private void loadCandidatosFromFile(final File planilha) {
		
		try {
			
			System.out.print("Carregando planilha \"" + planilha.getName() + "\"...");
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(planilha));
			
			final HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator                 = sheet.iterator();
			
			/** Lê o concurso */
			rowIterator.next();
			listaConcursos.add(sheet.getRow(1).getCell(0).getStringCellValue());
			
			while (rowIterator.hasNext()) {
				Candidato candidato = getCandidatoFromRow(rowIterator.next());
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
	private Candidato getCandidatoFromRow(Row sheet) {
		
		final int[] rowSet = new int[] {1};
		
		String concurso   = sheet.getCell(rowSet[0]).getStringCellValue();
		int codigo		  = Integer.parseInt(sheet.getCell(rowSet[1]).getStringCellValue());
		String nome       = sheet.getCell(rowSet[2]).getStringCellValue();
		String sexo	 	  = sheet.getCell(rowSet[3]).getStringCellValue();
		String nascimento = sheet.getCell(rowSet[4]).getStringCellValue();
		
		String rg			 = sheet.getCell(rowSet[5]).getStringCellValue();
		String orgaoEmissor  = sheet.getCell(rowSet[6]).getStringCellValue();
		String estadoEmissor = sheet.getCell(rowSet[7]).getStringCellValue();
		String cpf 			 = sheet.getCell(rowSet[8]).getStringCellValue();
		String dataInscricao = sheet.getCell(rowSet[9]).getStringCellValue();
		
		String situacao    = sheet.getCell(rowSet[10]).getStringCellValue();
		
		String rua 	   = sheet.getCell(rowSet[11]).getStringCellValue();
		String numCasa = sheet.getCell(rowSet[12]).getStringCellValue();
		String bairro  = sheet.getCell(rowSet[13]).getStringCellValue();
		String cep 	   = sheet.getCell(rowSet[14]).getStringCellValue();
		
		String cidade = sheet.getCell(rowSet[15]).getStringCellValue();
		String estado = sheet.getCell(rowSet[16]).getStringCellValue();
		String fone   = sheet.getCell(rowSet[17]).getStringCellValue();
		String email  = sheet.getCell(rowSet[18]).getStringCellValue();
		
		//Candidato candidato = new Candidato(concurso, codigo, nome, sexo, nascimento, rg, orgaoEmissor, estadoEmissor, cpf, dataInscricao, situacao, rua, numCasa, bairro, cep, cidade, estado, fone, email);
		
		return null;
	}
	
}
