package io.draw.service

import io.draw.api.CommandExt._
import io.draw.api._
import io.draw.model.DrawModel
import io.draw.model.DrawModelExt._
import org.slf4j.LoggerFactory

class DrawService(val es: EventStore, val publisher: Publisher[ModelChanged]) extends Listener[Command] {
  private val LOG = LoggerFactory.getLogger(getClass)

  def handle(c: Command) = {
    LOG.info("handle command={}", c)

    val m = es.all.foldLeft(DrawModel())((m, e) => m.apply(e))
    c.event(m.version.next) match {
      case NoopEvent =>
        LOG.info("Unsupported command={}", c)
      case e if c.isInside(CanvasSize(m.width, m.height)) =>
        val nm = m.apply(e)
        es.add(e)
        publisher.publish(ModelChanged(nm.width, nm.height, nm.chars.clone()))
      case _ =>
        LOG.info("Unsupported command={}", c)
    }
  }
}
