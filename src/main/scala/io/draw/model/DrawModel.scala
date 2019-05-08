package io.draw.model

import io.draw.api.Version

case class DrawModel(version: Version,
                     width: Int,
                     height: Int,
                     chars: Array[Array[Char]])

object DrawModel {
  def apply(): DrawModel = new DrawModel(Version.empty, 0, 0, Array.ofDim(0, 0))
  def apply(v: Version, w: Int, h: Int, chars: Array[Array[Char]]): DrawModel = new DrawModel(v, w, h, chars)
}