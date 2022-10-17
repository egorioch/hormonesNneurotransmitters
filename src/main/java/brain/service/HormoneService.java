package brain.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Service
public class HormoneService {

    public Map<Integer, Path> readFiles() {
        HashMap<Integer, Path> tempMap = new HashMap<>();
        AtomicReference<Integer> i = new AtomicReference<>(0);
        try (Stream<Path> paths = Files.walk(Paths.get("/home/egor/Документы/java/java Intellij/hormonesNneurotransmitters/src/main/resources/litera"))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(value -> tempMap.put(i.getAndSet(i.get() + 1), value.getFileName()));
        } catch (IOException exception) {
            exception.getStackTrace();
        }

        return tempMap;
    }

    public void outFiles(Map<Integer, Path> map){
        map.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
