import HardTask.CustomCompilation;
import HardTask.TestModule;

void main() throws IOException {

    Path compilationDir = Path.of("C:\\Курсач\\Project\\Java_Lab_1\\out\\production\\Java_Lab_1\\HardTask");
    Path targetFileDir = Path.of("C:\\Курсач\\Project\\Java_Lab_1\\src\\HardTask");
    Path targetFile = Path.of("TestModule.java");

    CustomCompilation customCompilation = new CustomCompilation(compilationDir, targetFileDir, targetFile);
    customCompilation.watchFile();

    TestModule testModule = new TestModule();
    IO.println(testModule);
}
