package io.draw

import io.draw.api.Event
import scala.collection.mutable

package object service {
  trait EventStore {
    def add(e: Event)
    def all: List[Event]
    def snapshot
  }

  class SimpleEventStore extends EventStore {
    private val events: mutable.ListBuffer[Event] = new mutable.ListBuffer[Event]

    override def add(e: Event): Unit = events += e

    override def all: List[Event] = events.toList

    override def snapshot: Unit = ???
  }

  object SimpleEventStore {
    def apply(): SimpleEventStore = new SimpleEventStore()
  }

  trait Publisher[T] {
    def publish(t: T)
  }

  trait Listener[T] {
    def handle(t: T)
  }

  class SimplePublisher[T](listener: Listener[T]) extends Publisher[T] {
    override def publish(t: T): Unit = listener.handle(t)
  }
}
