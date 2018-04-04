package com.graphql.neptune;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {
    private static final CustomerRepository customerRepository;
    private static final SegmentRepository segmentRepository;

    static {
        Cluster.Builder builder = Cluster.build();
        builder.addContactPoint("offergraphpoc-cluster.cluster-c1cqn0xh64pt.us-east-1-beta.rds.amazonaws.com");
        builder.minConnectionPoolSize(10);
        builder.maxConnectionPoolSize(500);
        builder.port(8182);

        Cluster cluster = builder.create();
        GraphTraversalSource g = EmptyGraph.instance().traversal().withRemote(DriverRemoteConnection.using(cluster));

        customerRepository = new CustomerRepository(g);
        segmentRepository = new SegmentRepository(g);
    }

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(
                        new Query(customerRepository, segmentRepository),
                        new Mutation(customerRepository, segmentRepository))
                .build()
                .makeExecutableSchema();
    }
}

