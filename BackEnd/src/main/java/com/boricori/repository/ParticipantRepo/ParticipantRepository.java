package com.boricori.repository.ParticipantRepo;

import com.boricori.entity.GameParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<GameParticipants, Long> {

}
