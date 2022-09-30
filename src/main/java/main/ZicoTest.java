package main;

import crawler.SemanticCrawler;
import impl.SemanticCrawlerImpl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class ZicoTest {
    public static void main(String[] args) {
        SemanticCrawler semanticCrawler = new SemanticCrawlerImpl();
        Model model = ModelFactory.createDefaultModel();

        semanticCrawler.search(model, "http://dbpedia.org/resource/Zico");

        model.write(System.out);
    }
}
