import EasyTask.PrimeBitCounter;

void main() {

    PrimeBitCounter primeNumbersGenerator = new PrimeBitCounter();

    int[] res = primeNumbersGenerator.countPrimeBits(127);
    IO.println("Number " + res[1] + " has " + res[0] + " ones");

}
