package io.draw

import java.util.Scanner
import java.util.concurrent.atomic.AtomicReference

import io.draw.api._
import io.draw.service._

object ConsoleDrawApp extends App {
  private val view = new ConsoleView
  private val dsPublisher = new SimplePublisher[ModelChanged](List())
  private val ds = new DrawService(SimpleEventStore(), dsPublisher)
  private val controller = new ConsoleController(new SimplePublisher[Command](List(ds)))
  dsPublisher.add(List(view, controller))
  controller.loop()
}

class ConsoleController(private val publisher: Publisher[Command]) extends Listener[ModelChanged] {
  private val cs: AtomicReference[Option[CanvasSize]] = new AtomicReference[Option[CanvasSize]](None)

  def loop(): Unit = {
    print("enter command: ")
    val scanner = new Scanner(System.in)
    while (submit(scanner.nextLine())) {}
  }

  import io.draw.api.CommandExt._

  def submit(str: String): Boolean = (Command(str), cs.get()) match {
    case (QuitCommand, _) => false
    case (UnsupportedCommand(s), _) =>
      println(s"Unsupported=[$s]")
      print("enter command: ")
      true
    case (c @ CanvasCommand(_, _), _) => send(c)
    case (c, None) =>
      println(s"Unsupported=[$c]. You need to setup canvas first")
      print("enter command: ")
      true
    case (c, Some(x)) if c.isInside(x) => send(c)
    case (c, Some(x)) =>
      println(s"Unsupported=[$c]. Canvas size=$x")
      print("enter command: ")
      true
  }

  private def send(c: Command) = {
    publisher.publish(c)
    true
  }

  override def handle(e: ModelChanged): Unit = cs.set(Some(CanvasSize(e.width, e.height)))
}

class ConsoleView extends Listener[ModelChanged]{
  override def handle(e: ModelChanged): Unit = {
    println(e.charsStr())
    println()
    print("enter command: ")
  }
}
