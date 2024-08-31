package com.uniksoft.broker.assets;

import lombok.Builder;

@Builder
public class Asset {
  private String name;

  public Asset(String name) {
    this.name = name;
  }

  public Asset() {
  }

  public String getName() {
    return name;
  }
}
