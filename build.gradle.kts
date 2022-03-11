plugins {
  alias(libs.plugins.indra.sonatype)
}

// Project metadata is configured in gradle.properties

indraSonatype {
  useAlternateSonatypeOSSHost("s01")
}
