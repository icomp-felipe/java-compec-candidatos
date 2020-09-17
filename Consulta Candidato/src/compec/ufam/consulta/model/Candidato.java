package compec.ufam.consulta.model;

import com.phill.libs.StringUtils;
import com.phill.libs.table.JTableRowData;
import com.phill.libs.time.DateUtils;

public class Candidato implements JTableRowData {

	private String concurso, nome, sexo, nascimento, rg, orgaoEmissor, estadoEmissor;
	private String cpf, dataInscricao, rua, numCasa, bairro, cep, cidade, estado, fone, email;
	private int codigo;
	private Situacao situacao;
	private Sistema versao;
	
	public Candidato(String concurso, int codigo, String nome, String sexo, String nascimento,
					 String rg, String orgaoEmissor, String estadoEmissor, String cpf, String dataInscricao,
					 Situacao situacao, String rua, String numCasa, String bairro, String cep, String cidade,
					 String estado, String fone, String email, Sistema versao) {
		
		this.concurso = concurso;
		this.codigo = codigo;
		
		this.nome = nome;
		this.sexo = sexo;
		this.nascimento = nascimento;
		
		this.rg = rg;
		this.orgaoEmissor = orgaoEmissor;
		this.estadoEmissor = estadoEmissor;
		this.cpf = StringUtils.extractNumbers(cpf);
		this.dataInscricao = dataInscricao;
		
		this.situacao = situacao;
		
		this.rua = rua;
		this.numCasa = numCasa;
		this.bairro = bairro;
		this.cep = cep;
		this.cidade = cidade;
		this.estado = estado;
		
		this.fone = StringUtils.extractNumbers(fone);
		this.email = email;
		this.versao = versao;
	}
	
	/************************* Bloco de Getters **********************************************/
	
	public String getConcurso() {
		return concurso;
	}
	
	public String getCodigoAsString() {
		return Integer.toString(codigo);
	}
	
	public String getNome(boolean isUserFormat) {
		return (isUserFormat) ? StringUtils.firstLetterLowerCase(nome) : nome;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public String getDataNascimento() {
		return getFormattedDate(nascimento);
	}
	
	public String getRG() {
		return rg;
	}
	
	public String getOrgaoEmissor() {
		return orgaoEmissor;
	}
	
	public String getUF() {
		return estadoEmissor;
	}
	
	public String getCPF(boolean isUserFormat) {
		return (isUserFormat) ? StringUtils.parseCPF(cpf) : cpf;
	}
	
	public String getDataInscricao() {
		return getFormattedDate(dataInscricao);
	}
	
	public Situacao getSituacao() {
		return situacao;
	}
	
	public String getLogradouro() {
		return rua;
	}
	
	public String getNumeroCasa() {
		return numCasa;
	}
	
	public String getBairro() {
		return bairro;
	}
	
	public String getCEP(boolean isUserFormat) {
		return (isUserFormat) ? StringUtils.parseCEP(cep) : cep;
	}
	
	public String getCidade() {
		return StringUtils.firstLetterLowerCase(cidade);
	}
	
	public String getFone() {
		
		if (fone.length() == 10)
			return String.format("(%s) %s-%s", fone.substring(0,2),fone.substring(2,6),fone.substring(6));
		
		else if (fone.length() == 11)
			return String.format("(%s) %s-%s", fone.substring(0,2),fone.substring(2,7),fone.substring(7));
		
		return fone;
		
	}
	
	public String getEmail() {
		return email;
	}
	
	/****************** Bloco de Métodos Auxiliares à esta classe ****************************/
	
	public synchronized String getFormattedDate(String date) {
		
		switch (versao) {
			case ANTIGO:
				return DateUtils.recoverMySQLDate(date);
			case NOVO:
				return DateUtils.recoverXLSDate(date);
		}
		
		return null;
		
	}
	
	public boolean matches(String regex) {
		
		String dados = String.format("%s-%s-%s-%s",concurso, nome, rg, cpf);
		
		return dados.matches(regex);
	}
	
	/***************** Bloco de Getters Personalizados ***************************************/
	
	@Override
	public Object[] getRowData() {
		return new Object[]{concurso,getCodigoAsString(),getNome(true),getDataNascimento(),rg,getCPF(true)};
	}
	
	public String getRGCompleto() {
		return String.format("%s – %s – %s", rg, orgaoEmissor, estadoEmissor);
	}
	
	public String getEnderecoCompleto() {
		return String.format("%s, %s, %s, %s – %s – %s", rua,numCasa,bairro,getCEP(true),cidade,estado);
	}
	
	public String getCidadeUF() {
		return String.format("%s - %s",getCidade(),estado);
	}
	
}
