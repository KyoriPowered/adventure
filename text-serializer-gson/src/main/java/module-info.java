/**
 * Gson based serialization and deserialization.
 */
module net.kyori.adventure.text.serializer.gson {
  requires transitive net.kyori.adventure.text.serializer.json;
  requires transitive com.google.gson;
  requires net.kyori.adventure.text.serializer.commons;
  requires static com.google.auto.service;

  exports net.kyori.adventure.text.serializer.gson;

  uses net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.Provider;

  provides net.kyori.adventure.text.event.DataComponentValueConverterRegistry.Provider
    with net.kyori.adventure.text.serializer.gson.impl.GsonDataComponentValueConverterProvider;
  provides net.kyori.adventure.text.serializer.json.JSONComponentSerializer.Provider
    with net.kyori.adventure.text.serializer.gson.impl.JSONComponentSerializerProviderImpl;
}
