package io.draw

package object api {
  case class Point(x: Int, y: Int) {
    def isInside(w: Int, h: Int): Boolean = x > 0 && y > 0 && x <= w && y <= h
  }

  object Point {
    def apply(x: Int, y: Int): Point = (x, y) match {
      case (x1, y1) if x1 >= 0 && y1 >= 0 => new Point(x1, y1)
      case _ => ???
    }

    def apply(x: String, y: String): Point = (x, y) match {
      case (s1, s2) if isNumber(s1) && isNumber(s2) => Point(s1.toInt, s2.toInt)
      case _ => throw new IllegalArgumentException(s"Not a numbers x=$x, y=$y")
    }
  }

  private def isNumber(x: String) = x forall Character.isDigit

  sealed trait Command

  case class CanvasCommand(width: Int, height: Int) extends Command
  case class LineCommand(p0: Point, p1:Point) extends Command
  case class RectangleCommand(p0: Point, p1: Point) extends Command
  case class BucketCommand(p: Point, c: Char) extends Command
  case object QuitCommand extends Command

  object Command {
    def apply(s: String): Command = s.split(" ").toList match {
      case List("C", w, h) => CanvasCommand(w.toInt, h.toInt)
      case List("L", x0, y0, x1, y1) => LineCommand(Point(x0, y0), Point(x1, y1))
      case List("R", x0, y0, x1, y1) => RectangleCommand(Point(x0, y0), Point(x1, y1))
      case List("B", x, y, c) => BucketCommand(Point(x, y), c.charAt(0))
      case List("Q") => QuitCommand
      case _ => throw new IllegalArgumentException(s"Unknown command=$s")
    }
  }

  class Version(val v: Int) extends AnyVal {
    def next: Version = new Version(v + 1)
  }

  object Version {
    val empty: Version = new Version(-1)
  }

  sealed trait Event {
    val version: Version
  }

  case class CanvasAdded(version: Version, width: Int, height: Int) extends Event
  case class LineAdded(version: Version) extends Event
  case class RectangleAdded(version: Version) extends Event
  case class BucketAdded(version: Version) extends Event
  case object NoopEvent extends Event {
    override val version: Version = Version.empty
  }

  case class ModelChanged(width: Int, height: Int, chars: Array[Array[Char]])
}
