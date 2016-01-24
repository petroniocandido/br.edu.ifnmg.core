/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.DomainModel.Services;


import br.edu.ifnmg.DomainModel.Services.Repositorio;
import br.edu.ifnmg.DomainModel.AreaConhecimento;
import javax.ejb.Local;

/**
 *
 * @author Isla Guedes
 */
@Local
public interface AreaConhecimentoRepositorio extends Repositorio<AreaConhecimento> {
    public AreaConhecimento Abrir(String numeroCNPQ);
}
