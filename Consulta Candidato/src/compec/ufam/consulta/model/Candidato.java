package compec.ufam.consulta.model;

import com.phill.libs.StringUtils;
import com.phill.libs.br.CPFParser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/** Modelagem de um candidato.
 *  @author Felipe André - felipeandresouza@hotmail.com
 *  @version 2.0 - 18/FEV/2023 */
public class Candidato {
	
	// Dados pessoais
	private String nome, sexo, rg, rgUF, cpf;
	private DateTime dataNascimento;
	private boolean temDeficiencia;
	private String logradouro, numero, bairro, cep, cidade, uf;
	private String telefone, email;
	
	// Dados relativos à inscrição no concurso
	private String concurso;
	private int inscricao;
	private DateTime dataInscricao;
	private String curso, acaoAfirmativa, cidadeConcurso, situacaoPagamento;
	
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
		this.dataNascimento = DateTimeFormat.forPattern("ddMMyyyy").parseDateTime(dataNascimento);
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
		this.dataInscricao = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").parseDateTime(dataInscricao);
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

	public String getNome() {
		return StringUtils.BR.normaliza(this.nome);
	}
	
	public String getSexo() {
		return this.sexo.equals("F") ? "Feminino" : "Masculino";
	}
	
	public String getCpf() {
		return CPFParser.format(this.cpf);
	}
	
	public String getNascimento() {
		return this.dataNascimento.toString(DateTimeFormat.forPattern("dd/MM/YYYY"));
	}
	
	public String getRg() {
		return this.rg;
	}
	
	public String getUfRG() {
		return this.rgUF;
	}

	public String getLogradouro() {
		return this.logradouro;
	}

	public String getNumero() {
		return this.numero;
	}
	
	public String getBairro() {
		return this.bairro;
	}
	
	public String getCidadeUF() {
		return this.cidade + " - " + this.uf;
	}
	
	public String getCep() {
		return this.cep;
	}

	public String getTelefone() {
		return this.telefone;
	}

	public String getEmail() {
		return this.email;
	}

	public String getConcurso() {
		return this.concurso;
	}
	
	public String getCodigo() {
		return Integer.toString(this.inscricao);
	}
	
	public String getDataInscricao() {
		return this.dataInscricao.toString(DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss"));
	}
	
	public String getCidadeConcurso() {
		return this.cidadeConcurso;
	}
	
	public String getCurso() {
		return this.curso;
	}
	
	public String getCotas() {
		return this.acaoAfirmativa;
	}
	
	public String getPcd() {
		return this.temDeficiencia ? "Sim" : "Não";
	}

	public String getSituacao() {
		return this.situacaoPagamento;
	}
	
}