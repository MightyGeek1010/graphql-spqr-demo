package com.virak.spqrdemo.controller;

import com.virak.spqrdemo.repository.FoodRepository;
import com.virak.spqrdemo.service.FoodService;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLException;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.value.jackson.JacksonValueMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class GraphQLController {

    private final GraphQL graphQL;

    @Autowired
    public GraphQLController(FoodService foodService) {
        GraphQLSchema schema = new GraphQLSchemaGenerator().withResolverBuilders(
                // Resolve by annotations
                new AnnotatedResolverBuilder())
                .withOperationsFromSingleton(foodService, FoodService.class)
                .withValueMapperFactory(new JacksonValueMapperFactory()).generate();
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,Object> graphql(@RequestBody Map<String, String> request, HttpServletRequest raw)
            throws GraphQLException {
        ExecutionResult executionResult = graphQL.execute(ExecutionInput.newExecutionInput().query(request.get("query"))
                .operationName(request.get("operationName")).context(raw).build());

        return executionResult.toSpecification();
    }
}
