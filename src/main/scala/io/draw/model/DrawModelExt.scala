package io.draw.model

import io.draw.api._
import org.slf4j.LoggerFactory

object DrawModelExt {
  private val LOG = LoggerFactory.getLogger(getClass)

  private val X_CHAR = 'x'
  private val SPACE_CHAR = ' '

  implicit class DraModelOps(m: DrawModel) {
    def apply(e: Event): DrawModel = {
      LOG.info("apply event={}", e)

      e match {
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
        case la@LineAdded(v, p0, p1) if la.isHorizontal =>
          for (j <- p0.x to p1.x) m.chars(p0.y)(j) = X_CHAR
          m.copy(version = v)
        case la@LineAdded(v, p0, p1) if la.isVertical =>
          for (i <- p0.y to p1.y) m.chars(i)(p0.x) = X_CHAR
          m.copy(version = v)
        case RectangleAdded(v, p0, p1) =>
          for (j <- p0.x to p1.x) {
            m.chars(p0.y)(j) = X_CHAR
            m.chars(p1.y)(j) = X_CHAR
          }
          for (i <- p0.y + 1 until p1.y) {
            m.chars(i)(p0.x) = X_CHAR
            m.chars(i)(p1.x) = X_CHAR
          }
          m.copy(version = v)
        case BucketAdded(v, p, c) =>
          def floodFill(x: Int, y: Int, target: Char, replace: Char): Unit =
            if (x != 0 && x <= m.width && y != 0 && y <= m.height &&
              target != replace && m.chars(y)(x) == target) {
              m.chars(y)(x) = replace
              floodFill(x, y - 1, target, replace)
              floodFill(x, y + 1, target, replace)
              floodFill(x - 1, y, target, replace)
              floodFill(x + 1, y, target, replace)
            }

          floodFill(p.x, p.y, m.chars(p.y)(p.x), c)
          m.copy(version = v)
        case _ => ???
      }
    }
  }
}
