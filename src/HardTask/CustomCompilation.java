package HardTask;

import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomCompilation {

    private final Path compilationDir;
    private final Path targetFileDir;
    private final Path targetFile;
    private final static String targetPackage = "HardTask.";

    public CustomCompilation(Path compilationDir, Path targetFileDir, Path targetFile){
        this.compilationDir = compilationDir;
        this.targetFileDir = targetFileDir;
        this.targetFile = targetFile;
    }

    public void watchFile() throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        targetFileDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> watchFileLoop(watchService));
        executor.shutdown();

    }

    private void watchFileLoop(WatchService watchService){
        try {
            while(!Thread.currentThread().isInterrupted()){
                WatchKey key;
                key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path absoluteFilePath = targetFileDir.resolve(pathEvent.context());
                    Path absoluteTargetPath = targetFileDir.resolve(targetFile);

                    if (absoluteFilePath.toString().equals(absoluteTargetPath.toString())){
                        compile();
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void compile(){
        ToolProvider.getSystemJavaCompiler().run(null, null, null, "-d", compilationDir.toString(), targetFileDir.resolve(targetFile).toString());
        load();
    }

    private void load(){
        String className = targetFile.toString();
        String qualifiedClassName = targetPackage.concat(className.substring(0, className.lastIndexOf('.')));
        CustomClassLoader classLoader = new CustomClassLoader(compilationDir.toString(), ClassLoader.getPlatformClassLoader());

        try {
            Class<?> myClass = classLoader.loadClass(qualifiedClassName);

            Object obj = myClass.getDeclaredConstructor().newInstance();
            IO.println(obj);

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
