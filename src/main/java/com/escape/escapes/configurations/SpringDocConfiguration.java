package com.escape.escapes.configurations;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@OpenAPIDefinition
@Configuration
public class SpringDocConfiguration {
    @Bean
    public OpenAPI baseOpenAPI() {
        ApiResponse forbiddenError = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value("{\"code\" : 403, \"Status\" : \"Forbidden\", \"Message\" : \"Forbidden\"")))
        ).description("""
                A 403 Forbidden Error occurs when a web server forbids you from accessing the page you’re trying to open 
                in your browser. Most of the time, there’s not much you can do. But sometimes, the problem 
                might be on your end. Here are some things you can try.
                """);


        ApiResponse badRequest = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value("{\"code\" : 400, \"Status\" : \"Bad Request\", \"Message\" : \"Bad Request\"")))
        ).description("""
                The request could not be understood by the server due to malformed syntax. The client SHOULD NOT repeat the request without modifications.""");

        ApiResponse notFound = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value("{\"code\" : 404, \"Status\" : \"Not Found\", \"Message\" : \"Not Found\"")))
        ).description("""
                The server has not found anything matching the Request-URI. 
                No indication is given of whether the condition is temporary or permanent. 
                The 410 (Gone) status code should be used if the server knows, through some 
                internally configurable mechanism, that an old resource is permanently 
                unavailable and has no forwarding address. This status code is commonly used 
                when the server does not wish to reveal exactly why the request has been refused, 
                or when no other response is applicable.""");

        ApiResponse internalServerErrorAPI = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value("{\"code\" : 500, \"Status\" : \"Internal Server Error\", \"Message\" : \"Internal Server Error\"")))
        ).description("""
                The server encountered an unexpected condition which prevented it from fulfilling the request.""");

        Components components = new Components();
        components.addResponses("badRequest",badRequest);
        components.addResponses("internalServerErrorAPI",internalServerErrorAPI);
        components.addResponses("notFound",notFound);
        components.addResponses("forbiddenError",forbiddenError);

        return new OpenAPI()
                .components(components)
                .info(new Info().title("Escapes Documentation")
                        .version("1.0.0").description("""
                                This is the swagger documentation of the Khissa Api. This has been made using OpenApi 3.0.
                                Steps to Execute the API:
                                    1. Ask any existing ADMIN to add your data into the database.
                                    2. Use the \" \\authenticate\" end point. The endpoint will return a jwt token.
                                    3. Add the token in the authorise section in the top-right corner.
                                    4. Once you click on the authorize button, you will be able to access all the endpoints."""));

    }
}
