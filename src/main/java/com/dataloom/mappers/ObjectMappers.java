package com.dataloom.mappers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Consumer;

public final class ObjectMappers {
    public enum Mapper {
        YAML,
        SMILE,
        JSON
    }

    // TODO: Add options that for configuring serialization types supported.

    private static final Map<Mapper, ObjectMapper> mappers = Maps.newEnumMap( Mapper.class );

    static {
        mappers.put( Mapper.YAML, createYamlMapper() );
        mappers.put( Mapper.SMILE, createSmileMapper() );
        mappers.put( Mapper.JSON, createJsonMapper() );
    }

    private ObjectMappers() {}

    protected static ObjectMapper createYamlMapper() {
        return configureMapper( new ObjectMapper( new YAMLFactory() ) );
    }

    protected static ObjectMapper createSmileMapper() {
        return configureMapper( new ObjectMapper( new SmileFactory() ) );
    }

    protected static ObjectMapper createJsonMapper() {
        return configureMapper( new ObjectMapper() );
    }

    protected static ObjectMapper configureMapper( ObjectMapper mapper ) {
        mapper.registerModule( new AfterburnerModule() );
        mapper.registerModule( new GuavaModule() );
        mapper.registerModule( new JavaTimeModule() );
        mapper.registerModule( new Jdk8Module() );
        mapper.registerModule( new JodaModule() );
        mapper.registerModule( new KotlinModule() );
        mapper.configure( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
        mapper.disable( DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE );
        return mapper;
    }

    public static ObjectMapper getYamlMapper() {
        return ObjectMappers.getMapper( Mapper.YAML );
    }

    public static ObjectMapper getSmileMapper() {
        return ObjectMappers.getMapper( Mapper.SMILE );
    }

    public static ObjectMapper getJsonMapper() {
        return ObjectMappers.getMapper( Mapper.JSON );
    }

    public static ObjectMapper newJsonMapper() {
        return createJsonMapper();
    }

    public static ObjectMapper getMapper( Mapper type ) {
        return mappers.get( type );
    }

    public static void foreach( Consumer<ObjectMapper> object ) {
        mappers.values().forEach( object );
    }
}
