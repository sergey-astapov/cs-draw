package io.draw.model

import io.draw.api.{Command, Event, NoopEvent, Version}

case class DrawModel(version: Version, width: Int, height: Int, chars: Array[Array[Char]]) {
  def apply(e: Event): DrawModel = this.copy(version = e.version)
  def apply(c: Command): (DrawModel, Event) = (this, NoopEvent)
}

object DrawModel {
  def apply(): DrawModel = new DrawModel(Version.empty, 0, 0, Array.ofDim(0, 0))
  def apply(v: Version, w: Int, h: Int, chars: Array[Array[Char]]): DrawModel = new DrawModel(v, w, h, chars)
}