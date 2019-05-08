package io.draw.api

import org.scalatest.FunSuite

class CommandTest extends FunSuite {
  test("Empty command") {
    assertResult(
      UnsupportedCommand("")
    )(Command(""))
  }

  test("Canvas command") {
    assertResult(
      CanvasCommand(20, 4)
    )(Command("C 20 4"))
  }

  test("Limit canvas command") {
    assertResult(
      CanvasCommand(80, 80)
    )(Command("C 80 80"))
  }

  test("Unsupported canvas command") {
    assertResult(
      UnsupportedCommand("C 81 80")
    )(Command("C 81 80"))
  }

  test("Line command") {
    assertResult(
      LineCommand(Point(1, 2), Point(6, 2))
    )(Command("L 1 2 6 2"))
  }

  test("Line command error") {
    assertResult(
      UnsupportedCommand("L M N P O")
    )(Command("L M N P O"))
  }

  test("Quit command") {
    assertResult(
      QuitCommand
    )(Command("Q"))
  }

  test("Unsupported command") {
    assertResult(
      UnsupportedCommand("XXX")
    )(Command("XXX"))
  }
}
