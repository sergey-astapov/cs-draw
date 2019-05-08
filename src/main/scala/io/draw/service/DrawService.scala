package io.draw.service

import io.draw.api.CommandExt._
import io.draw.api._
import io.draw.model.DrawModel
import io.draw.model.DrawModelExt._

class DrawService(val es: EventStore, val publisher: Publisher[ModelChanged]) extends Listener[Command] {
  def handle(c: Command): Unit = {
    val m = es.all.foldLeft(DrawModel())((m, e) => m.apply(e))
    c.event(m.version.next) match {
      case NoopEvent =>
      case e =>
        val nm = m.apply(e)
        es.add(e)
        publisher.publish(ModelChanged(nm.width, nm.height, nm.chars.clone()))
    }
  }
}
