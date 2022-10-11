package impl;

import crawler.SemanticCrawler;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashSet;
import java.util.Set;

public class SemanticCrawlerImpl implements SemanticCrawler {
    CharsetEncoder charsetEncoder = Charset.forName("ISO-8859-1").newEncoder();

    @Override
    public void search(Model graph, String resourceURI) {
        if (graph == null) throw new IllegalArgumentException("Model can't be null");

        Resource initialResource = ResourceFactory.createResource(resourceURI);
        Set<Resource> navigatedResources = new HashSet<>();

        this.lookForSameAsResources(graph, initialResource, navigatedResources);
    }

    private void lookForSameAsResources(Model graph, Resource resource, Set<Resource> navigatedResources){
        if(!navigatedResources.contains(resource) && charsetEncoder.canEncode(resource.getURI())) {
            try {
                Model auxModel = ModelFactory.createDefaultModel();
                auxModel.read(resource.getURI());
                navigatedResources.add(resource);
                ExtendedIterator<Statement> stmtIterator = auxModel.listStatements(resource, OWL.sameAs, (RDFNode) null).andThen(auxModel.listStatements((Resource) null, OWL.sameAs, resource));

                System.out.println("Crawling into resource " + resource.getURI());

                graph.add(auxModel);

                stmtIterator.forEach(statement -> {
                    if (!navigatedResources.contains(statement.getResource())) {
                        this.lookForSameAsResources(graph, statement.getResource(), navigatedResources);
                    }
                });
            }catch (Exception exception){
                System.out.println("Error acessing resource " + resource.getURI() + ". Skipping.");
                navigatedResources.add(resource);
            }
        }
    }
}
