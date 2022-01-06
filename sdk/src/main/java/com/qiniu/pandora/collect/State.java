package com.qiniu.pandora.collect;

public enum State {
  NEW("NEW"),
  STOPPED("STOPPED"),
  STARTED("STARTED"),
  ;

  private final String state;

  State(String state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return state;
  }
}
