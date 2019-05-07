package io.draw.service

import io.draw.api._
import io.draw.model.DrawModel

class DrawService(val es: EventStore, val publisher: Publisher[ModelChanged]) extends Listener[Command] {
  def handle(c: Command): Unit = {
    val tuple = es.all.foldLeft(DrawModel())((m, e) => m.apply(e)).apply(c)
    es.add(tuple._2)
    val m = tuple._1
    publisher.publish(ModelChanged(m.width, m.height, m.chars.clone()))
  }
}
