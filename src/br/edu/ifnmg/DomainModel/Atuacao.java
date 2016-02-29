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
public enum Atuacao {
    Discente("Discente"),
    Docente("Docente"),
    TecnicoAdministrativo("Técnico Administrativo"),
    Outros("Outros");
    private final String descricao;
    public String getDescricao() {
        return descricao;
    }

    private Atuacao(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public String toString() {
        return descricao;
    }
}
