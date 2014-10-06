/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.DomainModel.Services;

import br.edu.ifnmg.DomainModel.MensagemPerfil;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface MensagemPerfilRepositorio extends Repositorio<MensagemPerfil> {
    public MensagemPerfil getPadrao();
}
