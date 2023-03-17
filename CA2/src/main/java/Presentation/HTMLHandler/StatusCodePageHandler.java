package Presentation.HTMLHandler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.File;

public class StatusCodePageHandler implements Handler {
    private String code;
    public StatusCodePageHandler(String code){
        this.code = code;
    }
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        ctx.status(Integer.valueOf(code));
        Document doc = Jsoup.parse(new File("src/main/resources/Templates/" + code + ".html"), "UTF-8");
        ctx.contentType("text/html");
        ctx.result(doc.toString());
    }
}


