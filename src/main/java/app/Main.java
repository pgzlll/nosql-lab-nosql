
package app;

import static spark.Spark.*;

import app.model.Student;
import com.google.gson.Gson;
import app.store.*;
import app.StudentGenerator;

public class Main {

    public static void main(String[] args) {
        port(8080);
        Gson gson = new Gson();

        RedisStore.init();
        HazelcastStore.init();
        MongoStore.init();

        for (int i = 1; i <= 10000; i++) {
            Student s = StudentGenerator.generate(i);
            RedisStore.put(s.getOgrenciNo(), s);
            HazelcastStore.put(s.getOgrenciNo(), s);
            MongoStore.put(s.getOgrenciNo(), s);
        }

        get("/nosql-lab-rd/:id", (req, res) ->
                gson.toJson(RedisStore.get(req.params(":id"))));

        get("/nosql-lab-hz/:id", (req, res) ->
                gson.toJson(HazelcastStore.get(req.params(":id"))));

        get("/nosql-lab-mon/:id", (req, res) ->
                gson.toJson(MongoStore.get(req.params(":id"))));

        get("/perfomance", (req, res) -> {
            res.type("text/plain");
            return performTest();
        });
    }
    public static String performTest(){
        long start, end;
        StringBuilder sb = new StringBuilder();

        // redis
        start = System.nanoTime();
        for (int i=1; i<=10000; i++){
            String id = "2025" + String.format("%06d", i);
            RedisStore.get(id);
        }
        end = System.nanoTime();
        sb.append("Redis 10.000 sorgu suresi: ").append((end-start)/1_000_000).append("ms\n");

        // mongodb
        for (int i=1; i<=10000; i++){
            String id = "2025" + String.format("%06d", i);
            MongoStore.get(id);
        }
        end = System.nanoTime();
        sb.append("MongoDB 10.000 sorgu suresi: ").append((end-start)/1_000_000).append("ms\n");

        // hazelcast
        for(int i=1; i<=10000; i++){
            String id = "2025" + String.format("%06d", i);
            HazelcastStore.get(id);
        }
        end = System.nanoTime();
        sb.append("Hazelcast 10.000 sorgu suresi: ").append((end-start)/1_000_000).append("ms");

        return sb.toString();
    }
}
