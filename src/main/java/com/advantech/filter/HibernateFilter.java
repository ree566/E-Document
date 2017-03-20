/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.filter;

import com.advantech.helper.HibernateUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Wei.Cheng
 */
public class HibernateFilter implements Filter {

    private SessionFactory factory;
    private FilterConfig config = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        this.factory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Transaction tx = null;
        try {
            tx = factory.getCurrentSession().beginTransaction();
            System.out.println("tx start");
            String url = ((HttpServletRequest) request).getRequestURL().toString();
            String queryString = ((HttpServletRequest) request).getQueryString();
            System.out.println(url + "?" + queryString);
            chain.doFilter(request, response);
            tx.commit();
        } catch (HibernateException | IOException | ServletException ex) {
            System.out.println(ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            factory.getCurrentSession().close();
            System.out.println("close");
        }
    }

    @Override
    public void destroy() {
        factory.close();
    }

}
