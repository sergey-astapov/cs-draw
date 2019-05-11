package io.draw.model

import io.draw.api.{CanvasSize, Point}
import org.scalatest.FunSuite

class CanvasTest extends FunSuite {
  test("Empty canvas") {
    val sut = Canvas(CanvasSize(0, 0))

    assertResult(
      """--
        |--""".stripMargin
    )(sut.toString)
  }

  test("Create canvas") {
    val sut = Canvas(CanvasSize(20, 4))

    assertResult(
      """----------------------
        ||                    |
        ||                    |
        ||                    |
        ||                    |
        |----------------------""".stripMargin
    )(sut.toString)
  }

  test("Draw horizontal line") {
    val sut = Canvas(CanvasSize(20, 4))
    sut.drawLine(Point(1, 2), Point(6, 2))

    assertResult(
      """----------------------
        ||                    |
        ||xxxxxx              |
        ||                    |
        ||                    |
        |----------------------""".stripMargin
    )(sut.toString)
  }

  test("Draw vertical line") {
    val sut = Canvas(CanvasSize(20, 4))
    sut.drawLine(Point(1, 2), Point(6, 2))
    sut.drawLine(Point(6, 3), Point(6, 4))

    assertResult(
      """----------------------
        ||                    |
        ||xxxxxx              |
        ||     x              |
        ||     x              |
        |----------------------""".stripMargin
    )(sut.toString)
  }

  test("Draw rectangle") {
    val sut = Canvas(CanvasSize(20, 4))
    sut.drawLine(Point(1, 2), Point(6, 2))
    sut.drawLine(Point(6, 3), Point(6, 4))
    sut.drawRectangle(Point(14, 1), Point(18, 3))

    assertResult(
      """----------------------
        ||             xxxxx  |
        ||xxxxxx       x   x  |
        ||     x       xxxxx  |
        ||     x              |
        |----------------------""".stripMargin
    )(sut.toString)
  }

  test("Bucket fill") {
    val sut = Canvas(CanvasSize(20, 4))
    sut.drawLine(Point(1, 2), Point(6, 2))
    sut.drawLine(Point(6, 3), Point(6, 4))
    sut.drawRectangle(Point(14, 1), Point(18, 3))
    sut.bucketFill(Point(10, 3), 'o')

    assertResult(
      """----------------------
        ||oooooooooooooxxxxxoo|
        ||xxxxxxooooooox   xoo|
        ||     xoooooooxxxxxoo|
        ||     xoooooooooooooo|
        |----------------------""".stripMargin
    )(sut.toString)
  }
}
