/*
 *   This file is part of SGEA - Sistema de Gestão de Eventos Acadêmicos - TADS IFNMG Campus Januária.
 *
 *   SGEA is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SGEA is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with SGEA.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.edu.ifnmg.DomainModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 *
 * @author petronio
 */
@Cacheable(true)
@Entity
@Table(name = "pessoas", indexes = {
    @Index(columnList = "cpf"),
    @Index(columnList = "email")})
//@Inheritance(strategy= InheritanceType.JOINED)
public class Pessoa implements Entidade, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @Column(nullable = false, length = 300)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, unique = true, length = 300)
    private String email;

    @Column(length = 12)
    private String telefone;

    @Column(nullable = false)
    private String senha;
    
    @Column()
    private String outraInstituicao;

    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @ManyToOne(fetch = FetchType.EAGER)
    private Perfil perfil;
    
    @Enumerated(EnumType.STRING)
    protected PessoaTipo tipo;
    
    @Enumerated(EnumType.STRING)
    protected PronomeTratamento tratamento;
    
    @Enumerated(EnumType.STRING)
    protected Titulacao titulacaoMaxima;
    
    @Enumerated(EnumType.STRING)
    protected Atuacao atuacao;
    
    @Enumerated(EnumType.STRING)
    protected Sexo sexo;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, targetEntity = AreaConhecimento.class)
    @JoinTable(name = "pessoas_areasconhecimento", 
            joinColumns=@JoinColumn(name="Pessoa_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="areasConhecimento_ID", referencedColumnName="ID"))
    private List<AreaConhecimento> areasConhecimento;
    
    @ManyToOne 
    private Campus campus;
    
    private boolean necessidadesEspecificas;
    
    private String descricaoNecessidadesEspecificas;
    
    private String lattes;

    public Pessoa() {
        this.areasConhecimento = new ArrayList<>();
        this.sexo = Sexo.M;
        this.tipo = PessoaTipo.Fisica;
        this.tratamento = PronomeTratamento.Senhor;
    }
     
    
    public void add(AreaConhecimento a) {
        if(a == null) return;
        if (!areasConhecimento.contains(a)) {
            areasConhecimento.add(a);
        }
    }

    public void remove(AreaConhecimento a) {
        if(a == null) return;
        if (areasConhecimento.contains(a)) {
            areasConhecimento.remove(a);
        }
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public List<AreaConhecimento> getAreasConhecimento() {
        return areasConhecimento;
    }

    public void setAreasConhecimento(List<AreaConhecimento> areasConhecimento) {
        this.areasConhecimento = areasConhecimento;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public String getLattes() {
        return lattes;
    }

    public void setLattes(String lattes) {
        this.lattes = lattes;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Transient
    private String cpfFormatado;

    public String getCpf() {
        if (cpfFormatado == null) {
            if (cpf != null && cpf.length() == 11) {
                cpfFormatado = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
            }
        }
        return cpfFormatado;
    }

    public void setCpf(String cpf) {
        if (cpf != null) {
            this.cpf = cpf.replace(".", "").replace("-", "");
            cpfFormatado = null;
        }
    }

    @Transient
    private String telefoneFormatado;

    public String getTelefone() {
        if (telefoneFormatado == null) {
            if (telefone != null && telefone.length() == 11) {
                telefoneFormatado = "(" + telefone.substring(0, 2) + ") " + telefone.substring(2, 7) + "-" + telefone.substring(7, 11);
            }
        }
        return telefoneFormatado;
    }

    public void setTelefone(String telefone) {
        if (telefone != null) {
            this.telefone = telefone.replace(" ", "").replace("(", "").replace(")", "").replace("-", "");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public PessoaTipo getTipo() {
        return tipo;
    }

    public void setTipo(PessoaTipo tipo) {
        this.tipo = tipo;
    }

    public PronomeTratamento getTratamento() {
        return tratamento;
    }

    public void setTratamento(PronomeTratamento tratamento) {
        this.tratamento = tratamento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Titulacao getTitulacaoMaxima() {
        return titulacaoMaxima;
    }

    public void setTitulacaoMaxima(Titulacao titulacaoMaxima) {
        this.titulacaoMaxima = titulacaoMaxima;
    }

    public Atuacao getAtuacao() {
        return atuacao;
    }

    public void setAtuacao(Atuacao atuacao) {
        this.atuacao = atuacao;
    }

    public boolean isNecessidadesEspecificas() {
        return necessidadesEspecificas;
    }

    public void setNecessidadesEspecificas(boolean necessidadesEspecificas) {
        this.necessidadesEspecificas = necessidadesEspecificas;
    }

    public String getDescricaoNecessidadesEspecificas() {
        return descricaoNecessidadesEspecificas;
    }

    public void setDescricaoNecessidadesEspecificas(String descricaoNecessidadesEspecificas) {
        this.descricaoNecessidadesEspecificas = descricaoNecessidadesEspecificas;
    }

    public String getOutraInstituicao() {
        return outraInstituicao;
    }

    public void setOutraInstituicao(String outraInstituicao) {
        this.outraInstituicao = outraInstituicao;
    }
    
    @Transient
    String ac = null;
    public String areasConhecimento(){
        if(ac == null){
            ac = "";
            for(AreaConhecimento a : getAreasConhecimento()){
                if(!ac.isEmpty()) ac = ac + ",";
                ac = ac + a.toString();
            }
        }
        return ac;
    }
    
    
        
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pessoa)) {
            return false;
        }
        Pessoa other = (Pessoa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome + " [" + getEmail()+ "]";
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Pessoa criador;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Pessoa ultimoAlterador;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    
    @Version
    private Long versao;
    

    @Override
    public Pessoa getCriador() {
        return criador;
    }

    @Override
    public void setCriador(Pessoa criador) {
        this.criador = criador;
    }

    @Override
    public Date getDataCriacao() {
        return dataCriacao;
    }

    @Override
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public Pessoa getUltimoAlterador() {
        return ultimoAlterador;
    }

    @Override
    public void setUltimoAlterador(Pessoa ultimoAlterador) {
        this.ultimoAlterador = ultimoAlterador;
    }

    @Override
    public Date getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    @Override
    public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }

    @Override
    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }
}
