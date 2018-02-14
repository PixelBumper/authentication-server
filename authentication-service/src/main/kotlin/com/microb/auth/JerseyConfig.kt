package com.microb.auth

import com.microb.auth.jersey.api.AccountApi
import com.microb.auth.jersey.mappers.AllExceptionMapper
import com.microb.auth.jersey.mappers.WebApplicationExceptionMapper
import io.swagger.v3.jaxrs2.SwaggerSerializers
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.ServerProperties
import org.glassfish.jersey.server.TracingConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.stereotype.Component
import javax.annotation.security.PermitAll
import javax.ws.rs.ApplicationPath


@Component
@ApplicationPath("api") // remap jersey path to not hog the error page of spring boot
@PermitAll
class JerseyConfig : ResourceConfig() {

    @Autowired
    private lateinit var beanFactory: AutowireCapableBeanFactory

    init {
        configureJersey()

        registerEndpoints()
        configureSwagger()
    }

    private final fun configureJersey() {
        // disable moxy
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, true)
        property(ServerProperties.TRACING, TracingConfig.ALL.name)

        // Register JacksonFeature.
        register(JacksonFeature::class.java)
    }

    private final fun configureSwagger() {
        register(OpenApiResource::class.java)

    }

    private final fun registerEndpoints() {

        packages(true, AccountApi::class.java.`package`.name)
        register(AccountApi::class.java)
        register(AllExceptionMapper::class.java)
        register(WebApplicationExceptionMapper::class.java)


    }


}