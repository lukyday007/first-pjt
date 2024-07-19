package com.boricori.repository.ParticipantRepo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipantRepositoryImpl {

  @Autowired
  private EntityManager em;

  private JPAQueryFactory queryFactory = new JPAQueryFactory(em);

  public void makeGameParticipants() {
    
  }
}
