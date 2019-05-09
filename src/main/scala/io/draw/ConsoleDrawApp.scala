package io.draw

import java.util.Scanner

import io.draw.api._
import io.draw.service._

object ConsoleDrawApp extends App {
  lazy val view = new ConsoleView
  lazy val ds = new DrawService(SimpleEventStore(), new SimplePublisher[ModelChanged](view))
  lazy val controller = new ConsoleController(new SimplePublisher[Command](ds))
  controller.loop
}

class ConsoleController(private val publisher: Publisher[Command]) {
  def loop(): Unit = {
    print("enter command: ")
    val scanner = new Scanner(System.in)
    while (submit(scanner.nextLine())) {}
  }

  def submit(str: String): Boolean = Command(str) match {
    case QuitCommand => false
    case UnsupportedCommand(s) =>
      println(s"Unsupported=[$s]")
      print("enter command: ")
      true
    case c =>
      publisher.publish(c)
      true
  }
}

class ConsoleView extends Listener[ModelChanged]{
  override def handle(e: ModelChanged): Unit = {
    println(e.charsStr())
    println()
    print("enter command: ")
  }
}
