package compec.ufam.siscand.model;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;

import com.phill.libs.*;
import com.phill.libs.files.*;

/** Classe responsável pela extração de dados dos candidatos a partir das planilhas do Excel contidas em 'res/sheets'.
 *  @author Felipe André - felipeandre.eng@gmail.com
 *  @version 2.1 - 02/ABRV/2024 */
public class CandidatoDAO {

	private static final File sheets = ResourceManager.getResourceAsFile("sheets");
	
	/** Nomes das colunas a serem buscadas no cabeçalho de cada planilha. */
	private static final String[] columnNames = { "NOME_CAND", "SEXO_CAND", "NASCIMENTO_CAND", "RG_CAND", "UF_RG_CAND", "CPF_CAND", "LOGRADOURO", "NUMERO", "BAIRRO", "CEP",
												  "CIDADE_LOGRADOURO", "UF_LOGRADOURO", "TELEFONE", "EMAIL", "DEFICIENCIA_?", "COD_CONCURSO", "NUM_INSCRICAO", "DATA_INSC",
												  "CURSO", "ACAO_AFIRMATIVA", "CIDADE_CONCURSO", "SITUACAO_PAGAMENTO" };
	
	
	/** Monta um mapa com as listas de candidatos identificadas pelo código do concurso, a partir de todas as planilhas do diretório de recursos 'sheets'.
	 *  @return Mapa com as listas de candidatos identificadas pelo código do concurso.
	 *  @throws FileNotFoundException quando a <code>planilha</code> não pôde ser lida ou encontrada.
	 *  @throws IOException quando algum erro geral de leitura ocorre. */
	public static Map<String, List<Candidato>> load() throws FileNotFoundException, IOException {
		
		// Instanciando o mapa
		Map<String, List<Candidato>> mapaCandidatos = new HashMap<String, List<Candidato>>();
		
		// Listando planilhas xls dentro do diretório 'sheets'
		File[] listaArquivos = PhillFileUtils.listFilesOrdered(sheets, PhillFileUtils.ASCENDING);
		
		if (listaArquivos != null)
		
			// Povoando o mapa com os dados extraídos das planilhas
			for (File planilha: listaArquivos)
				if (planilha.getName().endsWith(".xls"))
					loadFromSheet(planilha, mapaCandidatos);
				
		
		return mapaCandidatos;
	}

	/** Carrega uma lista de candidatos a partir de uma <code>planilha</code> do Excel para um {@link HashMap}, identificado pelo código do concurso.
	 *  @param planilha - arquivo da planilha do Excel
	 *  @param mapaCandidatos - mapeamento de (concurso, lista de candidatos)
	 *  @throws FileNotFoundException quando a <code>planilha</code> não pôde ser lida ou encontrada.
	 *  @throws IOException quando algum erro geral de leitura ocorre. */
	private static void loadFromSheet(final File planilha, final Map<String, List<Candidato>> mapaCandidatos) throws FileNotFoundException, IOException {
		
		// Abrindo a planilha
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(planilha));
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		
		// Instanciando lista de candidatos
		List<Candidato> listaCandidatos = new ArrayList<Candidato>();
		
		// Lendo o cabeçalho
		Row   header    = rowIterator.next();
		int[] columnSet = getColumnSet(header);
		
		// Recupera o código do concurso
		final String concurso = sheet.getRow(1).getCell(0).getStringCellValue();
		
		// Iterando nas linhas da planilha, recuperando candidatos
		while (rowIterator.hasNext()) {
			
			Candidato candidato = loadFromRow(rowIterator.next(), concurso, columnSet);
			listaCandidatos.add(candidato);
			
		}
		
		// Registrando a lista de candidatos no mapa
		mapaCandidatos.put(concurso, listaCandidatos);
		
		// Desalocando recursos
		workbook.close();
	}
	
	/** Extrai os dados de um candidato de uma linha da tabela.
	 *  @param row - linha da planilha do Excel
	 *  @param concurso - código de concurso (estático e único)
	 *  @param columnSet - índices das colunas, de acordo com {@link #columnNames}
	 *  @return Objeto {@link Candidato} com os dados extraídos de uma linha da planilha. */
	private static Candidato loadFromRow(final Row row, final String concurso, final int[] columnSet) {
		
		final Candidato candidato = new Candidato();
		
		candidato.setConcurso(concurso);
		
		candidato.setNome             (row.getCell(columnSet[ 0]).getStringCellValue());
		candidato.setSexo             (row.getCell(columnSet[ 1]).getStringCellValue());
		candidato.setDataNascimento   (row.getCell(columnSet[ 2]).getStringCellValue());
		candidato.setRG               (row.getCell(columnSet[ 3]).getStringCellValue());
		candidato.setRGUF             (row.getCell(columnSet[ 4]).getStringCellValue());
		candidato.setCPF              (row.getCell(columnSet[ 5]).getStringCellValue());
		candidato.setLogradouro       (row.getCell(columnSet[ 6]).getStringCellValue());
		candidato.setNumero           (row.getCell(columnSet[ 7]).getStringCellValue());
		candidato.setBairro           (row.getCell(columnSet[ 8]).getStringCellValue());
		candidato.setCEP              (row.getCell(columnSet[ 9]).getStringCellValue());
		candidato.setCidade           (row.getCell(columnSet[10]).getStringCellValue());
		candidato.setUF               (row.getCell(columnSet[11]).getStringCellValue());
		candidato.setTelefone         (row.getCell(columnSet[12]).getStringCellValue());
		candidato.setEmail            (row.getCell(columnSet[13]).getStringCellValue());
		candidato.setTemDeficiencia   (row.getCell(columnSet[14]).getStringCellValue());
		candidato.setInscricao        (row.getCell(columnSet[16]).getStringCellValue());
		candidato.setDataInscricao    (row.getCell(columnSet[17]).getStringCellValue());
		candidato.setCurso            (row.getCell(columnSet[18]).getStringCellValue());
		candidato.setAcaoAfirmativa   (row.getCell(columnSet[19]).getStringCellValue());
		candidato.setCidadeConcurso   (row.getCell(columnSet[20]).getStringCellValue());
		candidato.setSituacaoPagamento(row.getCell(columnSet[21]).getStringCellValue());
		
		return candidato;
	}

	/** Extrai os índices de coluna a partir do cabeçalho do .xls.
	 *  @param header - linha de cabeçalho da planilha de candidatos
	 *  @return Array com os índices de coluna na ordem de {@link #columnNames}. */
	private static int[] getColumnSet(final Row header) {
	
		// Recupera a quantidade de nomes de campos a serem buscados
		final int campos = columnNames.length;
		
		// Recupera a quantidade de colunas do cabeçalho
		final int colunas = header.getPhysicalNumberOfCells();
		
		// Inicializando o array de índices
		final int[] indices = new int[campos];
		
		// Iterando sobre o array de nomes de colunas
		for (int iNomeColuna = 0; iNomeColuna < campos; iNomeColuna++) {
			
			// Iterando sobre as colunas físicas do Excel
			for (int iColuna = 0; iColuna < colunas; iColuna++) {
				
				if (header.getCell(iColuna).getStringCellValue().equals(columnNames[iNomeColuna])) {
					
					indices[iNomeColuna] = iColuna;
					
					break;
					
				}
				
			}
			
		}
		
		return indices;
	}

}