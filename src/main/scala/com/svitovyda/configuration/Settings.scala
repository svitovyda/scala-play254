package com.svitovyda.configuration

import com.typesafe.config.Config

import scala.collection.JavaConversions._


case class Settings(config: Config) {
  val strategies: Set[Strategy] = config.getStringList("play254.settings.strategies").toSet.map {
    s: String => Strategy(s)
  }
  val anyBool: Boolean = config.getBoolean("play254.settings.any-bool")
}

object Settings {
  implicit def conf2Settings: (Config) => Settings = Settings apply
}
