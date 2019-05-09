package io.draw

package object api {
  val MAX_AREA: Long = 80 * 80

  case class Point(x: Int, y: Int) {
    def isInside(w: Int, h: Int): Boolean = x > 0 && y > 0 && x <= w && y <= h
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

  object Command {
    def apply(s: String): Command = s.split(" ").toList match {
      case l @ List("C", w, h) if leqMaxArea(w, h) => CanvasCommand(w.toInt, h.toInt)
      case l @ List("L", x0, y0, x1, y1) if isNumber(l.tail) => LineCommand(Point(x0, y0), Point(x1, y1))
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
    def isHorizontal: Boolean = p0.y == p1.y
    def isVertical: Boolean = p0.x == p1.x
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
