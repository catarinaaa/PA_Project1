package ist.meic.pa.FunctionalProfiler;

public class ImperativeCounter implements Counter {
  int i;
  ImperativeCounter(int start) {
      i = start;
      System.out.println(i);
    }
  public int value() {
    return i;
  }
  public Counter advance() {
    i = i+1;
    return this;
  }
}
