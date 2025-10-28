package com.devouk.devouk_back.common;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestLogAppender extends AppenderBase<ILoggingEvent> {
  private final List<String> events = new CopyOnWriteArrayList<>();

  @Override
  protected void append(ILoggingEvent eventObject) {
    String fullLog = eventObject.getFormattedMessage() + " " + eventObject.getMDCPropertyMap();
    events.add(fullLog);
  }

  public List<String> eventsAsString() {
    return events;
  }
}
