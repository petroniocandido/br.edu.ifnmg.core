
package br.edu.ifnmg.DomainModel.Services;

import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface ConfiguracaoService {
    public String get(String chave);
    public boolean set(String chave, String valor);
    public boolean setLocal(String chave, String valor);
}
