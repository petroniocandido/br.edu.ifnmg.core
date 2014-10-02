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
package br.edu.ifnmg.DataAccess;

import br.edu.ifnmg.DomainModel.Entidade;
import br.edu.ifnmg.DomainModel.Services.Repositorio;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author petronio
 * @param <T>
 */
public abstract class DAOGenerico<T extends Entidade> implements Repositorio<T> {

    private final Class tipo;
    private Exception erro;
    private String logicalConnector = " and ";
    private StringBuilder where = new StringBuilder();
    private StringBuilder update = new StringBuilder();
    private StringBuilder order = new StringBuilder();
    private HashMap<String, String> join = new HashMap<>();
    private HashMap<Integer, Object> params = new HashMap<>();
    private boolean jts = true;

    public DAOGenerico(Class t) {
        tipo = t;
    }

    public abstract EntityManager getManager();

    @Override
    public Class getTipo() {
        return tipo;
    }

    /**
     * Cria um filtro para consulta de igualdade
     *
     * @param campo Atributo da classe que se deseja filtrar
     * @param valor Valor do filtro
     * @author petronio
     * @return
     */
    @PostConstruct
    public void configure() {
        getManager().setFlushMode(FlushModeType.COMMIT);
    }

    @Override
    public Repositorio<T> Join(String campo, String alias) {
        join.put(campo, alias);
        return this;
    }

    @Override
    public Repositorio<T> IgualA(String campo, Object valor) {
        addOp(campo, "=", valor);
        return this;
    }

    @Override
    public Repositorio<T> DiferenteDe(String campo, Object valor) {
        addOp(campo, "<>", valor);
        return this;
    }

    @Override
    public Repositorio<T> MaiorQue(String campo, Object valor) {
        addOp(campo, ">", valor);
        return this;
    }

    @Override
    public Repositorio<T> MaiorOuIgualA(String campo, Object valor) {
        addOp(campo, ">=", valor);
        return this;
    }

    @Override
    public Repositorio<T> MenorQue(String campo, Object valor) {
        addOp(campo, "<", valor);
        return this;
    }

    @Override
    public Repositorio<T> MenorOuIgualA(String campo, Object valor) {
        addOp(campo, "<=", valor);
        return this;
    }

    private void addSpecialOp(StringBuilder where, String campo, String op) {
        if (where.length() > 0) {
            where.append(logicalConnector);
        }

        if (!campo.contains(".")) {
            where.append("o.");
        }

        where.append(campo).append(" ").append(op);
    }

    private void addSpecialOp(String campo, String op) {
        addSpecialOp(where, campo, op);
    }

    @Override
    public Repositorio<T> Like(String campo, String valor) {
        if (valor == null || valor.toString().length() == 0) {
            return this;
        }
        addSpecialOp(campo, "like '%" + valor + "%'");
        return this;
    }

    @Override
    public Repositorio<T> ENulo(String campo) {
        addSpecialOp(campo, "is null");
        return this;
    }

    @Override
    public Repositorio<T> NaoENulo(String campo) {
        addSpecialOp(campo, "is null");
        return this;
    }

    @Override
    public Repositorio<T> Ordenar(String campo, String sentido) {
        if (order.length() > 0) {
            order.append(",");
        }

        if (!campo.contains(".")) {
            order.append("o.");
        }

        order.append(campo).append(" ").append(sentido);

        return this;
    }

    @Override
    public Repositorio<T> Setar(String campo, Object valor) {
        int key = params.size();

        if (update.length() > 0) {
            update.append(", ");
        }
        if (!campo.contains(".")) {
            update.append("o.");
        }

        update.append(campo).append(" = ").append(" :p").append(Integer.toString(key));
        params.put(key, valor);

        return this;
    }

    @Override
    public boolean Atualiza() {
        EntityTransaction tran = null;
        try {
            StringBuilder sql = new StringBuilder("update ").append(tipo.getSimpleName()).append(" o set ").append(update.toString());

            if (where.length() > 0) {
                sql.append(" where ").append(where.toString());
            }

            Query query = getManager().createQuery(sql.toString(), tipo);

            for (Integer key : params.keySet()) {
                query.setParameter("p" + key.toString(), params.get(key));
            }

            if (!jts) {
                tran = getManager().getTransaction();
                tran.begin();
            }

            query.executeUpdate();

            if (!jts) {
                tran.commit();
            }

            return true;
        } catch (Exception ex) {
            if (!jts) {
                tran.rollback();
            }
            setErro(ex);
            return false;
        } finally {
            join.clear();
            params.clear();
            where = new StringBuilder();
            order = new StringBuilder();
            update = new StringBuilder();
        }
    }

    @Override
    public boolean Apaga() {
        EntityTransaction tran = null;
        try {
            StringBuilder sql = new StringBuilder("delete from ").append(tipo.getSimpleName()).append(" o ");

            if (join.size() > 0) {
                for (String key : join.keySet()) {
                    sql.append(" join o.").append(key).append(" ").append(join.get(key));
                }
            }

            if (where.length() > 0) {
                sql.append(" where ").append(where.toString());
            }

            Query query = getManager().createQuery(sql.toString(), tipo);

            for (Integer key : params.keySet()) {
                query.setParameter("p" + key.toString(), params.get(key));
            }

            if (!jts) {
                tran = getManager().getTransaction();
                tran.begin();
            }

            query.executeUpdate();

            if (!jts) 
                tran.commit();
            
            return true;
        } catch (Exception ex) {
            if (!jts) 
                tran.rollback();
            
            setErro(ex);
            return false;
        } finally {
            join.clear();
            params.clear();
            where = new StringBuilder();
            order = new StringBuilder();
        }
    }

    private Query processQuery() {
        try {
            StringBuilder sql = new StringBuilder("select o from ").append(tipo.getSimpleName()).append(" o ");

            if (join.size() > 0) {
                for (String key : join.keySet()) {
                    if (!key.contains(".")) {
                        sql.append(" join o.");
                    } else {
                        sql.append(" join ");
                    }

                    sql.append(key).append(" ").append(join.get(key));
                }
            }
            if (where.length() > 0) {
                sql.append(" where ").append(where.toString());
            }
            if (order.length() > 0) {
                sql.append(" order by ").append(order.toString());
            }

            String tmp = sql.toString();

            Query query = getManager().createQuery(tmp, tipo);

            for (Integer key : params.keySet()) {
                query.setParameter("p" + key.toString(), params.get(key));
            }

            return query;
        } catch (Exception ex) {
            setErro(ex);
            return null;
        } finally {
            join.clear();
            params.clear();
            where = new StringBuilder();
            order = new StringBuilder();
        }
    }

    @Override
    public T Abrir() {
        try {
            return (T) processQuery().getSingleResult();
        } catch (Exception ex) {
            setErro(ex);
            return null;
        }
    }

    @Override
    public List<T> Buscar() {
        try {
            return processQuery().getResultList();
        } catch (Exception ex) {
            setErro(ex);
            return null;
        }
    }

    protected StringBuilder addOp(StringBuilder where, HashMap<Integer, Object> params, String field, String op, Object value) {
        if (value == null) {
            return where;
        }
        if (value.toString().equals("")) {
            return where;
        }

        if (value.toString().equals("0")) {
            return where;
        }

        int key = params.size();

        if (where.length() > 0) {
            where.append(logicalConnector);
        }

        if (!field.contains(".")) {
            where.append("o.");
        }

        where.append(field).append(" ").append(op).append(" :p").append(Integer.toString(key));
        params.put(key, value);
        return where;
    }

    private void addOp(String field, String op, Object value) {
        addOp(where, params, field, op, value);
    }

    @Override
    public boolean Salvar(T obj) {
        EntityTransaction tran = null;
        try {
            if (!jts) {
                tran = getManager().getTransaction();
                tran.begin();
            }

            if (getManager().contains(obj) || (obj.getId() != null && obj.getId() > 0)) {
                obj = getManager().merge(obj);
            } else {
                getManager().persist(obj);
            }

            getManager().flush();

            if (!jts) 
                tran.commit();
            
            return true;
        } catch (Exception ex) {
            if (!jts) 
                tran.rollback();
            
            setErro(ex);
            return false;
        }
    }

    @Override
    public boolean Apagar(T obj) {
        EntityTransaction tran = null;
        try {
            if (!jts) {
                tran = getManager().getTransaction();
                tran.begin();
            }

            // Persiste o objeto
            getManager().remove(getManager().merge(obj));

            getManager().flush();

            if (!jts) 
                tran.commit();
            
            return true;
        } catch (Exception ex) {
            if (!jts) 
                tran.rollback();
            
            setErro(ex);
            return false;
        }
    }

    @Override
    public T Abrir(Long id) {
        try {

            // Persiste o objeto
            T obj = (T) getManager().find(tipo, id);

            return obj;

        } catch (Exception ex) {
            setErro(ex);
            return null;
        }
    }

    public void setErro(Exception erro) {
        System.out.println(erro.getMessage());
        erro.printStackTrace();
        this.erro = erro;
    }

    @Override
    public Exception getErro() {
        return erro;
    }

    @Override
    public List<T> Buscar(T filtro) {
        return Buscar();
    }

    @Override
    public T Refresh(T obj) {
        if (obj.getId() != null && obj.getId() > 0) {
            getManager().flush();
            //manager.refresh(obj);
            obj = (T) getManager().getReference(tipo, obj.getId());
        }
        return obj;
    }

    public boolean isJts() {
        return jts;
    }

    public void setJts(boolean jts) {
        this.jts = jts;
    }
    
    
}
