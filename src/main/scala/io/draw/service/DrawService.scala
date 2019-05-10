package io.draw.service

import com.typesafe.scalalogging.StrictLogging
import io.draw.api.CommandExt._
import io.draw.api._
import io.draw.model.DrawModel
import io.draw.model.DrawModelExt._

class DrawService(val es: EventStore, val publisher: Publisher[ModelChanged]) extends Listener[Command] with StrictLogging {
  def handle(c: Command): Unit = {
    logger.info("handle command={}", c)

    val m = es.all.foldLeft(DrawModel())((m, e) => m.apply(e))
    c.event(m.version.next) match {
      case NoopEvent =>
        logger.info("Unsupported command={}", c)
      case e if c.isInside(CanvasSize(m.width, m.height)) =>
        val nm = m.apply(e)
        es.add(e)
        publisher.publish(ModelChanged(nm.width, nm.height, nm.chars.clone()))
      case _ =>
        logger.info("Unsupported command={}", c)
    }
  }
}
