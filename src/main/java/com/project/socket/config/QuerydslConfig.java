package com.project.socket.config;

import com.querydsl.jpa.DefaultQueryHandler;
import com.querydsl.jpa.Hibernate5Templates;
import com.querydsl.jpa.QueryHandler;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

  @PersistenceContext
  private EntityManager entityManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(new CustomHibernate5Templates(), entityManager);
  }

  public static class CustomHibernate5Templates extends Hibernate5Templates {

    @Override
    public QueryHandler getQueryHandler() {
      return DefaultQueryHandler.DEFAULT;
    }
  }

}
