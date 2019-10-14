package compec.ufam.consulta.model;

/** Define as versões do sistema de origem das planilhas,
 *  bem como as colunas onde os dados estão dispostos.
 *  @author Felipe André
 *  @version 1.5, 09/10/2016 */
public enum Sistema {

	/* Disposição dos Dados nos Parâmetros
	 * 01. Concurso
	 * 02. Código
	 * 03. Nome
	 * 04. Sexo
	 * 05. Nascimento
	 * 06. Número do RG
	 * 07. Órgão Emssor do RG
	 * 08. Estado Emissor do RG
	 * 09. CPF
	 * 10. Data da Última Inscrição
	 * 11. Situação no Concurso
	 * 12. Logradouro
	 * 13. Número da Residência
	 * 14. Bairro
	 * 15. CEP
	 * 16. Cidade
	 * 17. Estado
	 * 18. Número de Telefone
	 * 19. e-mail */
	
	NOVO   (0,1,2,3,4,5,6,7,8,24,25,27,28,29,30,31,32,33,34),
	ANTIGO (0,1,2,3,4,5,6,7,8,21,22,24,25,26,27,28,29,30,31);
	
	private int[] rowSet;
	
	Sistema(int... rowSet) {
		this.rowSet = rowSet;
	}
	
	int[] getRowSet() {
		return rowSet;
	}
	
}
