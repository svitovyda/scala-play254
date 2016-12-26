package com.svitovyda.configuration

import java.util.jar.Manifest

import play.api.libs.json.Json

import scala.collection.JavaConverters._


object ManifestLoader {
  case class JarManifest(url: String, attributes: List[String]) {
    override def toString: String = s"$url\n - ${attributes.mkString("\n - ")}"
  }
  object JarManifest {
    implicit val writes = Json.writes[JarManifest]
  }

  def getManifests(filterBy: String): List[JarManifest] = {
    val manifests = getClass.getClassLoader.getResources("META-INF/MANIFEST.MF").asScala

    manifests.toList collect {
      case url if url.toExternalForm.contains(filterBy) =>
        val mainAttributes = new Manifest(url.openStream()).getMainAttributes
        val attributes = mainAttributes.keySet().toArray.toList.map { key =>
          s"$key = ${mainAttributes.getValue(key.toString)}"
        }
        JarManifest(url.toExternalForm, attributes)
    }

  }
}
