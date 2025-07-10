
package app;

import static spark.Spark.*;

import app.model.Student;
import com.google.gson.Gson;
import app.store.*;
import app.util.StudentGenerator;

public class Main {

    public static void main(String[] args) {
        port(8080);
        Gson gson = new Gson();

        RedisStore.init();
        HazelcastStore.init();
        MongoStore.init();

        for (int i = 1; i<=10000; i++){
            Student s = StudentGenerator.generate(i);
            RedisStore.put(s.getOgrenciNo(),s);
            HazelcastStore.put(s.getOgrenciNo(),s);
            MongoStore.put(s.getOgrenciNo(),s);
        }

        get("/nosql-lab-rd/ogrenci_no=:id", (req, res) ->
            gson.toJson(RedisStore.get(req.params(":id"))));

        get("/nosql-lab-hz/ogrenci_no=:id", (req, res) ->
            gson.toJson(HazelcastStore.get(req.params(":id"))));

        get("/nosql-lab-mon/ogrenci_no=:id", (req, res) ->
            gson.toJson(MongoStore.get(req.params(":id"))));

    }
}
