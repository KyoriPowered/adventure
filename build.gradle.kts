plugins {
  alias(libs.plugins.indra.sonatype)
  alias(libs.plugins.nexusPublish)
}

// Project metadata is configured in gradle.properties

indraSonatype {
  useAlternateSonatypeOSSHost("s01")
}
