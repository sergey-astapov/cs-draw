package io.draw.model

import io.draw.api._

object DrawModelExt {
  private val MAX_AREA: Long = 80 * 80
  private val X_CHAR = 'x'
  private val SPACE_CHAR = ' '

  implicit class DraModelOps(m: DrawModel) {
    def apply(c: Command): (DrawModel, Event) = c match {
      case CanvasCommand(w, h) =>
        val e = CanvasAdded(m.version.next, w, h)
        (m.apply(e), e)
      case LineCommand(p0, p1) => ???
      case RectangleCommand(p0, p1) => ???
      case BucketCommand(p, c) => ???
      case _ => ???
    }

    def apply(e: Event): DrawModel = e match {
      case CanvasAdded(v, w, h) if w * h <= MAX_AREA =>
        val newHeight = h + 2
        val newWidth = w + 2
        val chars = Array.ofDim[Char](newHeight, newWidth)
        for (i <- 0 to chars.length) {
          for (j <- 0 to chars(0).length) {
            (i, j) match {
              case (0, _) => chars(i)(j) = '-'
              case (_, `newHeight`) => chars(i)(j) = '-'
              case (_, 0) => chars(i)(j) = '|'
              case (_, `newWidth`) => chars(i)(j) = '|'
              case _ => chars(i)(j) = SPACE_CHAR
            }
          }
        }
        DrawModel(v, w, h, chars)
      case LineAdded(v) => ???
      case _ => ???
    }
  }
}
