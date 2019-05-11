package io.draw.model

import com.typesafe.scalalogging.StrictLogging
import io.draw.api._

case class DrawModel(version: Version, canvas: Canvas) extends StrictLogging {
  def apply(e: Event): DrawModel = {
    logger.info(s"apply event=$e")

    e match {
      case CanvasAdded(v, w, h) if w * h <= MAX_AREA =>
        DrawModel(v, Canvas(CanvasSize(w, h)))
      case la@LineAdded(v, p0, p1) if la.isHorizontal =>
        canvas.drawLine(p0, p1)
        copy(version = v)
      case la@LineAdded(v, p0, p1) if la.isVertical =>
        canvas.drawLine(p0, p1)
        copy(version = v)
      case RectangleAdded(v, p0, p1) =>
        canvas.drawRectangle(p0, p1)
        copy(version = v)
      case BucketAdded(v, p, c) =>
        canvas.bucketFill(p, c)
        copy(version = v)
      case _ => ???
    }
  }
}

object DrawModel {
  def apply(): DrawModel = new DrawModel(Version.empty, Canvas.empty)
  def apply(v: Version, canvas: Canvas): DrawModel = new DrawModel(v, canvas)
}