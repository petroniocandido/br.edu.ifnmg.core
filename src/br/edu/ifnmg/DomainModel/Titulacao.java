/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.DomainModel;

/**
 *
 * @author petronio
 */
public enum Titulacao {
    EnsinoBasico(false,false,"Ensino Básico e Fundamental"),
    Tecnico(false,false,"Técnico"),
    Tecnologo(true,false,"Ensino Superior - Tecnologia"),
    Bacharel(true,false,"Ensino Superior - Bacharelado"),
    Licenciado(true,false,"Ensino Superior - Licenciatura"),
    Especialista(false,true,"Pós-Graduação Latu Sensu - Especialização/MBA"),
    Mestre(false,true,"Pós-Graduação Strictu Sensu - Mestrado"),
    Doutor(false,true,"Pós-Graduação Strictu Sensu - Doutorado");
    
    private final boolean graduacao;
    private final boolean posGraduacao;
    private final String descricao;

    public boolean isGraduacao() {
        return graduacao;
    }

    public boolean isPosGraduacao() {
        return posGraduacao;
    }

    public String getDescricao() {
        return descricao;
    }

    private Titulacao(boolean graduacao, boolean posGraduacao, String descricao) {
        this.graduacao = graduacao;
        this.posGraduacao = posGraduacao;
        this.descricao = descricao;
    }
    
    @Override
    public String toString() {
        return descricao;
    }
    
}
