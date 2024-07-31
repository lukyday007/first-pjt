package com.boricori.repository.inGameRepo;

import com.boricori.entity.InGameItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InGameItemsRepository extends JpaRepository<InGameItems, Long> {

}
