package io.draw.model

import io.draw.api._

object DrawModelExt {
  private val X_CHAR = 'x'
  private val SPACE_CHAR = ' '

  implicit class DraModelOps(m: DrawModel) {

    def apply(e: Event): DrawModel = e match {
      case CanvasAdded(v, w, h) if w * h <= MAX_AREA =>
        val ah = h + 2
        val aw = w + 2
        val chars = Array.ofDim[Char](ah, aw)
        for (i <- 0 until ah) {
          for (j <- 0 until aw) {
            (i, j) match {
              case (0, _) => chars(0)(j) = '-'
              case (x, _) if x == ah - 1 => chars(x)(j) = '-'
              case (_, 0) => chars(i)(0) = '|'
              case (_, x) if x == aw - 1 => chars(i)(x) = '|'
              case _ => chars(i)(j) = SPACE_CHAR
            }
          }
        }
        DrawModel(v, w, h, chars)
      case LineAdded(v, p0, p1) => ???
      case RectangleAdded(v, p0, p1) => ???
      case _ => ???
    }
  }
}
