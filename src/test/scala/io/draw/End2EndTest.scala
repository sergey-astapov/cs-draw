package io.draw

import io.draw.api.{Command, ModelChanged}
import io.draw.service.{DrawService, Listener, SimpleEventStore, SimplePublisher}
import org.scalatest.FunSuite

class End2EndTest extends FunSuite {
  var res: String = ""
  lazy val listener: Listener[ModelChanged] = (m: ModelChanged) => res = m.charsStr()
  lazy val ds = new DrawService(SimpleEventStore(), new SimplePublisher[ModelChanged](listener))
  lazy val controller = new ConsoleController(new SimplePublisher[Command](ds))

  test("Empty command") {
    assertResult(
      true
    )(controller.submit(""))

    assertResult(
      ""
    )(res)
  }

  test("Canvas command") {
    assertResult(
      true
    )(controller.submit("C 20 4"))

    assertResult(
      """----------------------
        ||                    |
        ||                    |
        ||                    |
        ||                    |
        |----------------------""".stripMargin
    )(res)
  }

  test("Horizontal line command") {
    controller.submit("C 20 4")
    controller.submit("L 1 2 6 2")

    assertResult(
      """----------------------
        ||                    |
        ||xxxxxx              |
        ||                    |
        ||                    |
        |----------------------""".stripMargin
    )(res)
  }

  test("Vertical line command") {
    controller.submit("C 20 4")
    controller.submit("L 1 2 6 2")

    assertResult(
      """----------------------
        ||                    |
        ||xxxxxx              |
        ||                    |
        ||                    |
        |----------------------""".stripMargin
    )(res)
  }
}
