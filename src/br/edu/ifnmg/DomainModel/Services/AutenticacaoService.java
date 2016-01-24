
package br.edu.ifnmg.DomainModel.Services;

import br.edu.ifnmg.DomainModel.Pessoa;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface AutenticacaoService {
    public boolean login(String email, String senha);
    public boolean logout();
    public boolean redefinirSenha(String email);
    public Pessoa getUsuarioCorrente();
}
