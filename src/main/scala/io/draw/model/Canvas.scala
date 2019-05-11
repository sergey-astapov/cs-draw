package io.draw.model

import io.draw.api.{CanvasSize, Point}

case class Canvas(size: CanvasSize) {
  private val X_CHAR = 'x'
  private val SPACE_CHAR = ' '
  private val fs = CanvasSize(size.w + 2, size.h + 2)
  private val chars = Array.ofDim[Char](fs.h, fs.w)

  for (i <- 0 until fs.h) {
    for (j <- 0 until fs.w) {
      (i, j) match {
        case (0, _) => chars(0)(j) = '-'
        case (x, _) if x == fs.h - 1 => chars(x)(j) = '-'
        case (_, 0) => chars(i)(0) = '|'
        case (_, x) if x == fs.w - 1 => chars(i)(x) = '|'
        case _ => chars(i)(j) = SPACE_CHAR
      }
    }
  }

  def drawLine(p0: Point, p1: Point): Unit = (p0, p1) match {
    case (s, e) if s.isHorizontalWith(e) =>
      for (j <- s.x to e.x) chars(s.y)(j) = X_CHAR
    case (s, e) if s.isVerticalWith(e) =>
      for (i <- s.y to e.y) chars(i)(s.x) = X_CHAR
    case _ => ???
  }

  def drawRectangle(p0: Point, p1: Point): Unit = {
    for (j <- p0.x to p1.x) {
      chars(p0.y)(j) = X_CHAR
      chars(p1.y)(j) = X_CHAR
    }
    for (i <- p0.y + 1 until p1.y) {
      chars(i)(p0.x) = X_CHAR
      chars(i)(p1.x) = X_CHAR
    }
  }

  def bucketFill(p: Point, c: Char): Unit = {
    def floodFill(x: Int, y: Int, target: Char, replace: Char): Unit =
      if (x != 0 && x <= fs.w && y != 0 && y <= fs.h &&
        target != replace && chars(y)(x) == target)
      {
        chars(y)(x) = replace
        floodFill(x, y - 1, target, replace)
        floodFill(x, y + 1, target, replace)
        floodFill(x - 1, y, target, replace)
        floodFill(x + 1, y, target, replace)
      }

    floodFill(p.x, p.y, chars(p.y)(p.x), c)
  }

  override def toString: String = str()

  def str(del: String = "\n"): String =
    (for (arr <- chars) yield {
      arr.mkString
    }).reduce(_ + del + _)
}

object Canvas {
  def empty: Canvas = Canvas(CanvasSize(0, 0))
  def apply(size: CanvasSize): Canvas = new Canvas(size)
}