/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.DomainModel.Services;

import br.edu.ifnmg.DomainModel.Campus;
import javax.ejb.Local;

/**
 *
 * @author Isla Guedes
 */
@Local
public interface CampusRepositorio  extends Repositorio<Campus> {
    public Campus Abrir(String sigla);
}
