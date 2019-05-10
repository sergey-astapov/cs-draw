package io.draw

package object api {
  val MAX_AREA: Long = 80 * 80

  case class CanvasSize(w: Int, h: Int)

  case class Point(x: Int, y: Int) {
    def isInside(cs: CanvasSize): Boolean = x > 0 && y > 0 && x <= cs.w && y <= cs.h
    def isHorizontalWith(p: Point): Boolean = y == p.y
    def isVerticalWith(p: Point): Boolean = x == p.x
  }

  object Point {
    def apply(x: Int, y: Int): Point = (x, y) match {
      case (x1, y1) if x1 >= 0 && y1 >= 0 => new Point(x1, y1)
    }

    def apply(x: String, y: String): Point = Point(x.toInt, y.toInt)
  }

  private def isNumber(x: String): Boolean = x forall Character.isDigit
  private def isNumber(l: List[String]): Boolean = l.forall(isNumber)
  private def leqMaxArea(w: String, h: String): Boolean = isNumber(List(w, h)) && w.toInt * h.toInt <= MAX_AREA

  sealed trait Command

  case class CanvasCommand(width: Int, height: Int) extends Command
  case class LineCommand(p0: Point, p1:Point) extends Command
  case class RectangleCommand(p0: Point, p1: Point) extends Command
  case class BucketCommand(p: Point, c: Char) extends Command
  case object QuitCommand extends Command
  case class UnsupportedCommand(s: String) extends Command

  private def isLineSupported(p0: Point, p1:Point): Boolean = p0.isHorizontalWith(p1) || p0.isVerticalWith(p1)

  object Command {
    def apply(s: String): Command = s.split(" ").toList match {
      case List("C", w, h) if leqMaxArea(w, h) => CanvasCommand(w.toInt, h.toInt)
      case l @ List("L", x0, y0, x1, y1) if isNumber(l.tail) && isLineSupported(Point(x0, y0), Point(x1, y1)) =>
        LineCommand(Point(x0, y0), Point(x1, y1))
      case l @ List("R", x0, y0, x1, y1) if isNumber(l.tail) => RectangleCommand(Point(x0, y0), Point(x1, y1))
      case List("B", x, y, c) if isNumber(List(x, y)) => BucketCommand(Point(x, y), c.charAt(0))
      case List("Q") => QuitCommand
      case _ => UnsupportedCommand(s)
    }
  }

  class Version(val v: Int) extends AnyVal {
    def next: Version = new Version(v + 1)
    override def toString: String = s"v=${v.toString}"
  }

  object Version {
    val empty: Version = new Version(-1)
  }

  sealed trait Event {
    val version: Version
  }

  case class CanvasAdded(version: Version, width: Int, height: Int) extends Event
  case class LineAdded(version: Version, p0: Point, p1: Point) extends Event {
    def isHorizontal: Boolean = p0.isHorizontalWith(p1)
    def isVertical: Boolean = p0.isVerticalWith(p1)
  }
  case class RectangleAdded(version: Version, p0: Point, p1: Point) extends Event
  case class BucketAdded(version: Version, p: Point, c: Char) extends Event
  case object NoopEvent extends Event {
    override val version: Version = Version.empty
  }

  case class ModelChanged(width: Int, height: Int, chars: Array[Array[Char]]) {
    def charsStr(del: String = "\n"): String =
      (for (arr <- chars) yield {
        arr.mkString
      }).reduce(_ + del + _)
  }
}
