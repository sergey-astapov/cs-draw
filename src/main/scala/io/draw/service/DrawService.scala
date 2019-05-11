package io.draw.service

import com.typesafe.scalalogging.StrictLogging
import io.draw.api._
import io.draw.model.DrawModel
import io.draw.model.DrawModelExt._

class DrawService(val es: EventStore, val publisher: Publisher[ModelEvent]) extends Listener[Command] with StrictLogging {
  def handle(c: Command): Unit = {
    logger.info(s"handle command=$c")

    val m = es.all.foldLeft(DrawModel())((m, e) => m.apply(e))
    val me = c.event(m.version.next) match {
      case NoopEvent => unsupported(m, c)
      case e @ CanvasAdded(_, _, _) => supported(m, e)
      case e if c.isInside(CanvasSize(m.width, m.height)) => supported(m, e)
      case _ => unsupported(m, c)
    }
    publisher.publish(me)
  }

  private def supported(m: DrawModel, e: Event) = {
    val nm = m.apply(e)
    es.add(e)
    ModelChanged(nm.version, nm.width, nm.height, nm.chars.clone())
  }

  private def unsupported(m: DrawModel, c: Command): ModelEvent = {
    val msg = s"Unsupported command=$c"
    logger.info(msg)
    ModelUnsupportedOperation(m.version, msg)
  }
}
