package com.boricori.repository.ParticipantRepo;

import com.boricori.entity.GameParticipants;
import com.boricori.entity.QGameParticipants;
import com.boricori.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipantRepositoryImpl {

  private EntityManager em;

  private JPAQueryFactory queryFactory;

  private QGameParticipants participants = QGameParticipants.gameParticipants;

  private QUser user = QUser.user;

  public ParticipantRepositoryImpl(
      @Autowired EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  public void makeGameParticipants() {

  }

  public GameParticipants getByEmail(String email, Long roomId){
    return queryFactory
        .selectFrom(participants)
        .join(participants.user, user)
        .where(user.email.eq(email)
        .and(participants.gameRoom.id.eq(roomId)))
        .fetchOne();
  }
}
