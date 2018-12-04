package com.microb.auth

import com.microb.auth.jersey.api.AccountApi
import com.microb.auth.jersey.api.TokenApi
import com.microb.auth.jersey.mappers.AllExceptionMapper
import com.microb.auth.jersey.mappers.ValidationExceptionMapper
import com.microb.auth.jersey.mappers.WebApplicationExceptionMapper
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.ServerProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.stereotype.Component
import javax.inject.Singleton
import javax.ws.rs.ApplicationPath
import javax.ws.rs.ext.ExceptionMapper

const val JERSEY_BASE_PATH = "/api"

@Component
@ApplicationPath(JERSEY_BASE_PATH) // remap jersey path to not hog the error page of spring boot
class JerseyConfig : ResourceConfig() {

    @Autowired
    private lateinit var beanFactory: AutowireCapableBeanFactory

    init {
        configureJersey()

        registerEndpoints()
        configureSwagger()
    }

    private final fun configureJersey() {

        register(object : AbstractBinder() {
            override fun configure() {
                bind(ValidationExceptionMapper::class.java)
                        .to(ExceptionMapper::class.java)
                        .`in`(Singleton::class.java)
                        .ranked(10)
            }

        })

        // ensure non 200 responses are nor redirected to an error page
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true)
        // disable moxy
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, true)
//        property(ServerProperties.TRACING, TracingConfig.ALL.name)

        // Register JacksonFeature.
        register(JacksonFeature::class.java)
    }

    private final fun configureSwagger() {
        register(OpenApiResource::class.java)

    }

    private final fun registerEndpoints() {

        register(TokenApi::class.java)
        register(AccountApi::class.java)
        register(AllExceptionMapper::class.java)
        register(WebApplicationExceptionMapper::class.java)


    }


}
