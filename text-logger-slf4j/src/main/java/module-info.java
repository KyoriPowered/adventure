/**
 * A wrapper around <a href="https://slf4j.org">SLF4J</a> providing methods for formatted logging of Components.
 *
 * <p>This wrapper supports the API provided in 1.7/1.8, but does not yet implement the fluent API present in the 2.0 betas.</p>
 */
module net.kyori.adventure.text.logger.slf4j {
  requires transitive net.kyori.adventure.api;
  requires transitive org.slf4j;

  exports net.kyori.adventure.text.logger.slf4j;
}
