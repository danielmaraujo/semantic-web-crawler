package impl;

import crawler.SemanticCrawler;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.OWL;

import java.util.HashSet;
import java.util.Set;

public class SemanticCrawlerImpl implements SemanticCrawler {
    @Override
    public void search(Model graph, String resourceURI) {
        Resource initialResource = ResourceFactory.createResource(resourceURI);
        Set<Resource> navigatedResources = new HashSet<>();

        this.lookForSameAsResources(initialResource, graph, navigatedResources);
    }

    private void lookForSameAsResources(Resource resource, Model graph, Set<Resource> navigatedResources){
        if(!navigatedResources.contains(resource)) {
            try {
                Model auxModel = ModelFactory.createDefaultModel();
                auxModel.read(resource.getURI());
                StmtIterator stmtIterator = auxModel.listStatements((Resource) null, OWL.sameAs, (RDFNode) null);
                navigatedResources.add(resource);
                System.out.println("Searching into resource " +  resource.getURI());
                graph.add(auxModel);

                stmtIterator.forEach(statement -> {
                    if (!navigatedResources.contains(statement.getResource())) {
                        this.lookForSameAsResources(statement.getResource(), graph, navigatedResources);
                    }
                });
            }catch (Exception exception){

            }
        }
    }
}
