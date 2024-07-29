package com.boricori.repository.inGameRepo;

import com.boricori.entity.GameParticipants;
import com.boricori.entity.InGameItems;
import com.boricori.entity.InGameMissions;
import com.boricori.entity.Item;
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
    jpaQueryFactory.insert(missions).values(igm).execute();
  }


  public void updateMission(Long missionId, GameParticipants player) {
    jpaQueryFactory
        .update(missions)
        .where(missions.user.eq(player).and(missions.missionId.id.eq(missionId)))
        .set(missions.done, true)
        .execute();
  }

  public void saveItem(InGameItems igi) {
    jpaQueryFactory
        .insert(items)
        .values(igi)
        .execute();
  }

  public void useItem(GameParticipants player, Long itemId) {
    jpaQueryFactory
        .update(items)
        .set(items.used, true)
        .where(items.user.id.eq(player.getId()).and(items.itemId.id.eq(itemId)))
        .execute();
  }
}
