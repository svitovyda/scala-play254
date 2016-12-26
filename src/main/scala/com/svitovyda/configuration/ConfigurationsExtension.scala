package com.svitovyda.configuration

import akka.actor._
import com.typesafe.config.{Config, ConfigFactory}
import ManifestLoader.JarManifest


class ConfigurationsExtensionImpl extends Extension {

  val environmentOption: Option[String] = Option(System.getenv("ENVIRONMENT"))

  val config: Config = environmentOption match {
    case Some(name) => ConfigFactory.load(s"environments/$name")
    case None => ConfigFactory.load("application")
  }
  val settings: Settings = Settings(config)

  val manifests: List[JarManifest] = ManifestLoader.getManifests("svitovyda")
}

object ConfigurationsExtension
  extends ExtensionId[ConfigurationsExtensionImpl]
  with ExtensionIdProvider {

  override def lookup() = ConfigurationsExtension

  override def createExtension(system: ExtendedActorSystem) = new ConfigurationsExtensionImpl

  override def get(system: ActorSystem): ConfigurationsExtensionImpl = super.get(system)
}
