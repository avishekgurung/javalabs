package test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import kafka.KafkaProcessor;

import java.util.Random;

public class VertxWebServer extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new VertxWebServer());

  }

  @Override public void start() throws Exception {
    KafkaProcessor kafkaProcessor = new KafkaProcessor();
    Router router = Router.router(vertx);
    router.route().handler(routingContext -> {
      String word = getWord();
      kafkaProcessor.send(word);
      routingContext.response().putHeader("content-type", "text/html").end(word);
    });
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }


  private String getWord() {
    Random random = new Random();
    String sentence = "Supposedly there are over one million words in the English Language. We trimmed some fat to take away really odd words and determiners. Then we grabbed the most popular words and built this word randomizer. Just keep clicking generate—chances are you won't find a repeat!";
    String arr[] = sentence.split(" ");
    return arr[random.nextInt(arr.length)];
  }

}
