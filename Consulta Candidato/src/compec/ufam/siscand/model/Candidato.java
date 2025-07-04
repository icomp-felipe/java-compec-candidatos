package compec.ufam.siscand.model;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.phill.libs.StringUtils;
import com.phill.libs.br.CPFParser;
import com.phill.libs.br.PhoneNumberUtils;
import com.phill.libs.table.JTableRowData;

/** Modelagem de um candidato.
 *  @author Felipe André - felipeandre.eng@gmail.com
 *  @version 2.1 - 06/JUN/2025 */
public class Candidato implements JTableRowData {
	
	// Dados pessoais
	private String nome, sexo, rg, rgUF, cpf;
	private LocalDate dataNascimento;
	private boolean temDeficiencia;
	private String logradouro, numero, bairro, cep, cidade, uf;
	private String telefone, email;
	
	// Dados relativos à inscrição no concurso
	private String concurso;
	private int inscricao;
	private LocalDateTime dataInscricao;
	private String curso, acaoAfirmativa, cidadeConcurso, situacaoPagamento;
	
	private final DateTimeFormatter formatter;
	
	{
		
		this.formatter = new DateTimeFormatterBuilder()
	            .appendPattern("uuuu-MM-dd HH:mm:ss")
	            .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
	            .toFormatter();
		
	}
	
	/********************************************************************/
	/********************* Setters de Identificação *********************/
	/********************************************************************/
	
	/** Setter do nome do candidato.
	 *  @param nome - nome do candidato */
	public void setNome(final String nome) {
		this.nome = nome;
	}
	
	/** Setter do sexo do candidato.
	 *  @param sexo - sexo do candidato */
	public void setSexo(final String sexo) {
		this.sexo = sexo;
	}
	
	/** Setter do número de RG do candidato.
	 *  @param rg - número de RG do candidato */
	public void setRG(final String rg) {
		this.rg = rg;
	}
	
	/** Setter da UF do RG do candidato.
	 *  @param rgUF - sigla da UF do RG do candidato */
	public void setRGUF(final String rgUF) {
		this.rgUF = rgUF;
	}
	
	/** Setter do número de CPF do candidato.
	 *  @param cpf - número de CPF do candidato */
	public void setCPF(final String cpf) {
		this.cpf = cpf;
	}
	
	/** Setter da data de nascimento do candidato.
	 *  @param dataNascimento - data de nascimento do candidato */
	public void setDataNascimento(final String dataNascimento) {
		this.dataNascimento = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("ddMMyyyy"));
	}
	
	/** Setter do sinalizador de condição especial (candidato)
	 *  @param temDeficiencia - indica se o candidato é PcD ou não */
	public void setTemDeficiencia(final String temDeficiencia) {
		this.temDeficiencia = temDeficiencia.equals("S");
	}
	
	/** Setter do logradouro do candidato.
	 *  @param logradouro - logradouro do candidato */
	public void setLogradouro(final String logradouro) {
		this.logradouro = logradouro;
	}
	
	/** Setter do número de logradouro do candidato.
	 *  @param numero - número de logradouro do candidato */
	public void setNumero(final String numero) {
		this.numero = numero;
	}
	
	/** Setter do bairro do logradouro do candidato.
	 *  @param bairro - bairro do logradouro do candidato */
	public void setBairro(final String bairro) {
		this.bairro = bairro;
	}
	
	/** Setter do CEP do logradouro do candidato.
	 *  @param cep - número de CEP do logradouro do candidato */
	public void setCEP(final String cep) {
		this.cep = cep;
	}
	
	/** Setter da cidade do logradouro do candidato.
	 *  @param cidade - cidade do logradouro do candidato */
	public void setCidade(final String cidade) {
		this.cidade = cidade;
	}
	
	/** Setter da UF do logradouro do candidato.
	 *  @param uf - sigla da UF do logradouro do candidato */
	public void setUF(String uf) {
		this.uf = uf;
	}
	
	/** Setter do número de telefone do candidato.
	 *  @param telefone - número de telefone do candidato */
	public void setTelefone(final String telefone) {
		this.telefone = telefone;
	}
	
	/** Setter do endereço de e-mail do candidato.
	 *  @param email - endereço de e-mail do candidato */
	public void setEmail(final String email) {
		this.email = email;
	}
	
	/********************************************************************/
	/*********************** Setters de Concurso ************************/
	/********************************************************************/
	
	/** Setter do código de concurso.
	 *  @param concurso - código do concurso */
	public void setConcurso(final String concurso) {
		this.concurso = concurso;
	}
	
	/** Setter do número de inscrição do candidato.
	 *  @param inscricao - número de inscrição do candidato */
	public void setInscricao(final String inscricao) {
		this.inscricao = Integer.parseInt(inscricao);
	}
	
	/** Setter do timestamp de inscrição.
	 *  @param dataInscricao - timestamp de criação da inscrição */
	public void setDataInscricao(final String dataInscricao) {
		this.dataInscricao = LocalDateTime.parse(dataInscricao, formatter);
	}
	
	/** Setter do curso escolhido pelo candidato.
	 *  @param curso - curso escolhido pelo candidato */
	public void setCurso(final String curso) {
		this.curso = curso;
	}
	
	/** Setter da ação afirmativa do candidato.
	 *  @param acaoAfirmativa - ação afirmativa do candidato */
	public void setAcaoAfirmativa(final String acaoAfirmativa) {
		this.acaoAfirmativa = acaoAfirmativa;
	}
	
	/** Setter da cidade de execução do concurso.
	 *  @param cidadeConcurso - cidade de execução do concurso */
	public void setCidadeConcurso(final String cidadeConcurso) {
		this.cidadeConcurso = cidadeConcurso;
	}
	
	/** Setter da situação de pagamento. */
	public void setSituacaoPagamento(final String situacaoPagamento) {
		this.situacaoPagamento = situacaoPagamento;
	}
	
	/********************************************************************/
	/************************* Bloco de Getters *************************/
	/********************************************************************/

	/** @return Nome do candidato já normalizado. */
	public String getNome() {
		return StringUtils.BR.normaliza(this.nome);
	}
	
	/** @return Sexo do candidato. */
	public String getSexo() {
		return this.sexo.equals("F") ? "Feminino" : "Masculino";
	}
	
	/** @return Número de CPF do candidato. */
	public String getCpf() {
		return CPFParser.format(this.cpf);
	}
	
	/** @return Data de nascimento do candidato, no formato 'dd/MM/YYYY'. */
	public String getNascimento() {
		try { return this.dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); }
		catch (Exception exception) { return null; }
	}
	
	/** @return Número de RG do candidato. */
	public String getRg() {
		return this.rg;
	}
	
	/** @return Sigla da UF de emissão de RG do candidato. */
	public String getUfRG() {
		return this.rgUF;
	}

	/** @return Logradouro de endereço do candidato. */
	public String getLogradouro() {
		return StringUtils.BR.normaliza(this.logradouro);
	}

	/** @return Número de endereço do candidato. */
	public String getNumero() {
		return this.numero;
	}
	
	/** @return Bairro de endereço do candidato. */
	public String getBairro() {
		return StringUtils.BR.normaliza(this.bairro);
	}
	
	/** @return Nome da cidade de endereço do candidato. */
	public String getCidadeUF() {
		return StringUtils.BR.normaliza(this.cidade) + " - " + this.uf;
	}
	
	/** @return Número de CEP de endereço do candidato. */
	public String getCep() {
		return this.cep;
	}

	/** @return Número de telefone do candidato (com formatação). */
	public String getTelefone() {
		return PhoneNumberUtils.format(this.telefone);
	}

	/** @return Endereço de e-mail do candidato. */
	public String getEmail() {
		return this.email == null ? null : this.email.toLowerCase();
	}

	/** @return Nome do concurso. */
	public String getConcurso() {
		return this.concurso;
	}
	
	/** @return Número de inscrição do candidato. */
	public String getCodigo() {
		return Integer.toString(this.inscricao);
	}
	
	/** @return Data e hora da inscrição do candidato, no formato 'dd/MM/YYYY - HH:mm:ss'. */
	public String getDataInscricao() {
		try { return this.dataInscricao.format(DateTimeFormatter.ofPattern("dd/MM/YYYY - HH:mm:ss")); }
		catch (Exception exception) { return null; }
	}
	
	/** @return Cidade de concurso do candidato. */
	public String getCidadeConcurso() {
		return StringUtils.BR.normaliza(this.cidadeConcurso);
	}
	
	/** @return Nome do curso escolhido pelo candidato. */
	public String getCurso() {
		return this.curso;
	}
	
	/** @return Cota em concurso. */
	public String getCotas() {
		return this.acaoAfirmativa;
	}
	
	/** @return Texto indicando se o candidato se identificou como PcD. */
	public String getPcd() {
		return this.temDeficiencia ? "Sim" : "Não";
	}

	/** @return Status da inscrição. */
	public String getSituacao() {
		return StringUtils.BR.normaliza(this.situacaoPagamento);
	}
	
	/** @return Número de WhatsApp do candidato. */
	public String getWhatsApp() {
		return this.telefone == null ? null : "55" + this.telefone;
	}
	
	/** @return 'true' apenas se a inscrição do candidato está confirmada. */
	public boolean inscricaoConfirmada() {
		return this.situacaoPagamento.equals("PAGAMENTO CONFIRMADO") || this.situacaoPagamento.equals("ISENCAO DE TAXA DE INSCRICAO CONFIRMADA.");
	}
	
	/********************************************************************/
	/******************* Bloco de Métodos Utilitários *******************/
	/********************************************************************/
	
	/** Compara se os dados recebidos via parâmetro conferem com os contidos nessa instância.
	 *  @param nome - filtro por nome do candidato
	 *  @param cpf - filtro por número de RG do candidato
	 *  @param concurso - filtro por nome do concurso
	 *  @param inscrito - filtra apenas por inscrição confirmada
	 *  @return 'true' se e somente se os dados recebidos conferem com os contidos nessa instância. */
	public boolean matches(final String nome, final String cpf, final String rg, final String concurso, final boolean inscrito) {
	    if (inscrito && !inscricaoConfirmada())
	        return false;

	    // Prepara filtros normalizados
	    String concursoFiltro = concurso.equals("Todos") ? "" : concurso;
	    String concursoRegex = Pattern.quote(normalizar(concursoFiltro)).replace("%", ".*");
	    String nomeRegex = Pattern.quote(normalizar(nome)).replace("%", ".*");
	    String rgRegex = Pattern.quote(normalizar(rg)).replace("%", ".*");
	    String cpfNumeros = StringUtils.extractNumbers(cpf);

	    // Monta regex final
	    String regex = String.format("%s.*-%s.*-%s.*-%s.*", concursoRegex, nomeRegex, rgRegex, cpfNumeros);
	    Pattern pattern = Pattern.compile(regex);

	    // Normaliza dados da instância
	    String concursoInstancia = normalizar(this.concurso);
	    String nomeInstancia = normalizar(this.nome);
	    String rgInstancia = normalizar(this.rg);
	    String cpfInstancia = StringUtils.extractNumbers(this.cpf);

	    String dados = String.format("%s-%s-%s-%s", concursoInstancia, nomeInstancia, rgInstancia, cpfInstancia);
	    Matcher matcher = pattern.matcher(dados);

	    return matcher.matches();
	}

	private String normalizar(String texto) {
	    if (texto == null) return "";
	    String n = Normalizer.normalize(texto, Normalizer.Form.NFD);
	    return n.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
	            .replaceAll("[çÇ]", "c")
	            .toLowerCase();
	}

	
	@Override
	public Object[] getRowData() {
		return new Object[] {
				this.concurso,
				getNome(),
				this.rg,
				getCpf(),
				this.inscricao,
				getDataInscricao(),
				inscricaoConfirmada()
		};
	}
	
}