package io.draw.api

object CommandExt {
  implicit class CommandOps(c: Command) {
    def event(v: Version): Event = c match {
      case CanvasCommand(w, h) => CanvasAdded(v, w, h)
      case LineCommand(p0, p1) => LineAdded(v, p0, p1)
      case RectangleCommand(p0, p1) => RectangleAdded(v, p0, p1)
      case BucketCommand(p, ch) => BucketAdded(v, p, ch)
      case _ => NoopEvent
    }
  }
}