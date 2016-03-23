/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.DomainModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author Isla Guedes
 */
@Entity
@Table(name="areasconhecimento")
@Cacheable(true)
public class AreaConhecimento implements Entidade, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String nome;
    
    @Column(nullable=false, unique = true)
    private String numeroCNPQ;
    
    @ManyToOne
    private AreaConhecimento grandeArea;
    
    @ManyToOne
    private AreaConhecimento area;
    
    @ManyToOne
    private AreaConhecimento superArea;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "superArea")
    private List<AreaConhecimento> subAreas;
    
    @ManyToMany(cascade = CascadeType.MERGE, targetEntity = Pessoa.class)
    @JoinTable(name = "pessoas_areasconhecimento", 
            inverseJoinColumns=@JoinColumn(name="Pessoa_ID", referencedColumnName="ID"),
            joinColumns=@JoinColumn(name="areasConhecimento_ID", referencedColumnName="ID"))
    private List<Pessoa> pessoas;
    
    public boolean isGrandeArea() {
        String tmp = numeroCNPQ.substring(1, 10);
        return tmp.contentEquals(".00.00.00");
    }
    
    public boolean isArea() {
        String tmp = numeroCNPQ.substring(5, 10);
        return tmp.contentEquals("00.00");
    }
        
    public String getGrandeAreaCodigo() {
        return numeroCNPQ.substring(0, 1) + ".00.00.00";
    }
    
    public String getAreaCodigo() {
        return numeroCNPQ.substring(0, 4) + ".00.00";
    }
    
    public String getPaiCodigo() {
        if(isGrandeArea()) return null;
        if(isArea()) return getGrandeAreaCodigo();
        if(!numeroCNPQ.substring(8,9).contentEquals("00")) 
            return numeroCNPQ.substring(0, 7) + "00";
        else
            return numeroCNPQ.substring(0, 4) + ".00.00";
    }
    
    

    //GETTER E SETTER
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroCNPQ() {
        return numeroCNPQ;
    }

    public void setNumeroCNPQ(String numeroCNPQ) {
        this.numeroCNPQ = numeroCNPQ;
    }

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public AreaConhecimento getGrandeArea() {
        return grandeArea;
    }

    public void setGrandeArea(AreaConhecimento grandeArea) {
        this.grandeArea = grandeArea;
    }

    public AreaConhecimento getArea() {
        return area;
    }

    public void setArea(AreaConhecimento area) {
        this.area = area;
    }

    public AreaConhecimento getSuperArea() {
        return superArea;
    }

    public void setSuperArea(AreaConhecimento superArea) {
        this.superArea = superArea;
    }

    public List<AreaConhecimento> getSubAreas() {
        return subAreas;
    }

    public void setSubAreas(List<AreaConhecimento> subAreas) {
        this.subAreas = subAreas;
    }
    
    
       

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.numeroCNPQ);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AreaConhecimento other = (AreaConhecimento) obj;
        if (!Objects.equals(this.numeroCNPQ, other.numeroCNPQ)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return numeroCNPQ + " - " + nome;
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
