package ist.meic.pa.FunctionalProfiler;

public class Test1{
  public static void test(Counter c1, Counter c2) {
    System.out.println(String.format("%s %s", c1.value(), c2.value()));
  }
  public static void main(String[] args){
    Counter ic = new ImperativeCounter(0);
    test(ic, ic.advance());
    System.out.println("Hi");
  }
}
