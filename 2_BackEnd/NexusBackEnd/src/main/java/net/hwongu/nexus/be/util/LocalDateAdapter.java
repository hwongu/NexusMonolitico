package net.hwongu.nexus.be.util;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador de Gson para la serialización y deserialización de objetos {@link LocalDateTime}.
 * Esta clase es necesaria porque la librería Gson no soporta de forma nativa los
 * tipos de fecha y hora del paquete {@code java.time}. Utiliza el formato estándar
 * ISO 8601 ({@code yyyy-MM-dd'T'HH:mm:ss}) para la representación en JSON.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class LocalDateAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Serializa un objeto {@link LocalDateTime} a su representación JSON como una cadena de texto.
     *
     * @param src El objeto {@link LocalDateTime} a serializar.
     * @param typeOfSrc El tipo del objeto fuente.
     * @param context El contexto de serialización.
     * @return Un {@link JsonElement} que contiene la fecha y hora formateada como String.
     */
    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(FORMATTER.format(src));
    }

    /**
     * Deserializa un {@link JsonElement} (que contiene una cadena de texto) a un objeto {@link LocalDateTime}.
     *
     * @param json El {@link JsonElement} a deserializar.
     * @param typeOfT El tipo del objeto destino.
     * @param context El contexto de deserialización.
     * @return Un objeto {@link LocalDateTime}.
     * @throws JsonParseException Si la cadena de texto en el JSON no puede ser parseada al formato esperado.
     */
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), FORMATTER);
    }
}
