package com.boricori.repository.userRepo;

import static com.boricori.entity.QUser.user;
import static org.springframework.util.StringUtils.hasText;

import com.boricori.dto.request.gameroom.PlayerInfoRequest;
import com.boricori.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl {

  EntityManager em;

  private JPAQueryFactory queryFactory;

  public UserRepositoryImpl(
      @Autowired EntityManager em) {
    this.em = em;
    queryFactory = new JPAQueryFactory(em);
  }

  public List<User> getUserList(List<PlayerInfoRequest> playerInfoList) {
    return queryFactory
        .selectFrom(user)
        .where(findUserByNameAndEmailInWhere(playerInfoList))
        .fetch();
  }

  private BooleanExpression findUserByNameAndEmailInWhere(List<PlayerInfoRequest> playerInfoList) {

    BooleanExpression expression = null;

    for (PlayerInfoRequest playerInfo : playerInfoList) {
      BooleanExpression currentExpression =
          userNameEq(playerInfo.getUsername()).and(userEmailEq(playerInfo.getEmail()));

      if (expression == null) {
        expression = currentExpression;
      } else {
        expression = expression.or(currentExpression);
      }
    }

    return expression;
  }


  private BooleanExpression userEmailEq(String email) {
    return hasText(email) ? user.email.eq(email) : null;
  }

  private BooleanExpression userNameEq(String userName) {
    return hasText(userName) ? user.username.eq(userName) : null;
  }

}
