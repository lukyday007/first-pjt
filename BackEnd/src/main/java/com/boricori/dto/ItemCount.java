package com.boricori.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ItemCount {

  Long itemId;

  long count;

  @QueryProjection
  public ItemCount(Long itemId, long count) {
    this.itemId = itemId;
    this.count = count;
  }
}
