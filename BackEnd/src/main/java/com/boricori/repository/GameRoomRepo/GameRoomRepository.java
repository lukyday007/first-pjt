package com.boricori.repository.GameRoomRepo;

import com.boricori.entity.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {

    int findMaxPlayerById(Long roomId);
}
