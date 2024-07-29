package com.boricori.repository.inGameRepo;

import com.boricori.entity.GameParticipants;
import com.boricori.entity.InGameMissions;
import com.boricori.entity.QInGameItems;
import com.boricori.entity.QInGameMissions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class InGameRepositoryImpl {
  private final JPAQueryFactory jpaQueryFactory;

  private QInGameItems items = QInGameItems.inGameItems;

  private QInGameMissions missions = QInGameMissions.inGameMissions;

  public InGameRepositoryImpl(JPAQueryFactory qf){
    jpaQueryFactory = qf;
  }

  public void saveMission(InGameMissions igm){
    jpaQueryFactory.insert(missions).values(igm);
  }


  public void updateMission(Long missionId, GameParticipants player) {
    jpaQueryFactory
        .update(missions)
        .where(missions.user.eq(player).and(missions.missionId.id.eq(missionId)))
        .set(missions.done, true)
        .execute();
  }
}
