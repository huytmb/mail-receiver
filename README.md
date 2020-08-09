Mail Receiver -  Using Spring Boot with Spring Integration
=======

## Description
This project demonstrates how to periodically read emails from mail server (support IMAP) and process them.

## Development

To start your application in the dev profile, run:

    ./mvnw

## Configuration

application.yml
```yaml
mail:
  imap:
    ssl: true
    host: imap.gmail.com
    port: 993
    username: your-email%40gmail.com
    password: your-password
```

## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/maven-plugin/reference/html/)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring Integration](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-integration)