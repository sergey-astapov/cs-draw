package io.draw

import java.util.Scanner

import io.draw.api._
import io.draw.service._

object ConsoleDrawApp extends App {
  private val view = new ConsoleView
  private val dsPublisher = new SimplePublisher[ModelEvent](List())
  private val ds = new DrawService(SimpleEventStore(), dsPublisher)
  private val controller = new ConsoleController(new SimplePublisher[Command](List(ds)))
  dsPublisher.add(List(view, controller))
  controller.loop()
}

class ConsoleController(private val publisher: Publisher[Command]) extends Listener[ModelEvent] {
  def loop(): Unit = {
    print("enter command: ")
    val scanner = new Scanner(System.in)
    while (submit(scanner.nextLine())) {}
  }

  def submit(str: String): Boolean = Command(str) match {
    case QuitCommand => false
    case UnsupportedCommand(s) =>
      handle(ModelUnsupportedOperation(Version.empty, s"Unsupported command=$s"))
      true
    case c  =>
      publisher.publish(c)
      true
  }

  override def handle(e: ModelEvent): Unit = e match {
    case ModelUnsupportedOperation(_, s) =>
      println(s"Unsupported, details='$s'")
      print("enter command: ")
    case _ =>
  }
}

class ConsoleView extends Listener[ModelEvent]{
  override def handle(e: ModelEvent): Unit = e match {
    case mc @ ModelChanged(_, _, _, _) =>
      println(mc.charsStr())
      println()
      print("enter command: ")
    case _ =>
  }
}
