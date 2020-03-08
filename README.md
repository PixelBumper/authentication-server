# authentication-server
A playground authentication server to experiment with kotlin, spring boot, swagger and jwts

## Goals 

- exploring how kotlin and spring boot work in conjunction
- figuring out the quirks of using Jersey for building apis in spring boot. This includes the following
    - how to setup resource classes to work with spring boot
    - how are errors handled
    - securing Jersey endpoints with spring security
- experimenting with spring AOP and AspectJ

## Technologies used

- Jersey for building the api
- swagger-jaxrs2 to document the api with OpenApi
- swagger-ui to allow to explore the OpenApi docs interactively 
- Spring Data JPA and QueryDSL to access the database
- Spring Security and jwts for securing the endpoints
- Spring AOP and AspectJ to be able to use annotations like @Transactional on private methods

## Findings

### Kotlin and JPA

For JPA to work correctly Entity classes need a constructor with no arguments.
To satisfy this constraint one could define default values for any mandatory parameter or provide a secondary no-arg constructor that calls the primary constructor with predefined default values.
The downside is that it is not always possible to provide sensible defaults.
A better solution is to use the [kotlin-jpa](https://kotlinlang.org/docs/reference/compiler-plugins.html#jpa-support) compiler plugin, which adds a no argument constructor to all JPA entity classes automatically.
While also being more convenient, it allows to still write constructors that enforce setting all mandatory values.
 
### Kotlin and Spring AOP

Kotlin has classes and their members final by default, which makes it inconvenient to use frameworks and libraries such as Spring AOP that require classes to be open.
To solve this, without having to open all classes where required, there is once again a convenient [kotlin-spring](https://kotlinlang.org/docs/reference/compiler-plugins.html#spring-support) compiler plugin

TODO
- document spring boot and jersey interaction fidings
