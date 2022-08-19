/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.htv.nttv.respository.impl;

import com.htv.nttv.pojo.User;
import com.htv.nttv.respository.UserGroupRepository;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Repository
@PropertySource("classpath:messages.properties")
@Transactional
public class UserGroupRepositoryImpl implements UserGroupRepository{
    
    @Autowired
    private LocalSessionFactoryBean SessionFactory;

    @Autowired
    private Environment env;

    @Override
    public List<Object[]> getUser(Map<String, String> params, int page) {
        Session session = this.SessionFactory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
       
        Root<User> root = q.from(User.class);
        //List<Predicate> predicates = new ArrayList<>();
        
        q.multiselect(root.get("firstName"), root.get("lastName"), root.get("email"), root.get("username"))
                    .where(b.equal(root.get("userRole"), 1));
       
        //if
        
        //q.where(predicates.toArray(new Predicate[]{}));
        Query query = session.createQuery(q);
        if (page > 0) {
            int size = Integer.parseInt(env.getProperty("page.size").toString());
            int start = (page - 1) * size;
            query.setFirstResult(start);
            query.setMaxResults(size);
        }

        return query.getResultList();
    }
}
