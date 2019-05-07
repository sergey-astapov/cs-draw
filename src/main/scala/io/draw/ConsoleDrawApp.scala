package io.draw

import java.util.Scanner

import io.draw.api._
import io.draw.service._

object ConsoleDrawApp extends App {
  lazy val view = new ConsoleView
  lazy val ds = new DrawService(SimpleEventStore(), new SimplePublisher[ModelChanged](view))
  lazy val controller = new ConsoleController(new SimplePublisher[Command](ds))
  controller.userLoop()
}

class ConsoleController(val publisher: Publisher[Command]) {
  def userLoop(): Unit = {
    var flag = true
    while (flag) {
      val scanner = new Scanner(System.in)
      Command(scanner.nextLine()) match {
        case QuitCommand => flag = false
        case c => publisher.publish(c)
      }
    }
  }
}

class ConsoleView extends Listener[ModelChanged]{
  override def handle(e: ModelChanged): Unit = {
    for (
      aChars <- e.chars;
      c <- aChars
    ) yield {
      print(c)
      //System.out.println();
    }
    println()
    print("enter command : ")
  }
}
