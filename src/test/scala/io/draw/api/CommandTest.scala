package io.draw.api

import org.scalatest.FunSuite

class CommandTest extends FunSuite {
  test("Empty command") {
    assertResult(
      None
    )(Command(""))
  }

  test("Canvas command") {
    assertResult(
      Some(CanvasCommand(20, 4))
    )(Command("C 20 4"))
  }

  test("Line command") {
    assertResult(
      Some(LineCommand(Point(1, 2), Point(6, 2)))
    )(Command("L 1 2 6 2"))
  }

  test("Line command error") {
    intercept[IllegalArgumentException]{
      Command("L M N P O")
    }
  }

  test("Quit command") {
    assertResult(
      Some(QuitCommand)
    )(Command("Q"))
  }

  test("Unknown command") {
    intercept[IllegalArgumentException]{
      Command("XXX")
    }
  }
}
