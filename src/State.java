import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class State {
  private String name;
  private boolean isFinal;
  private boolean isInitial;

  public State(String name, boolean isFinal, boolean isInitial) {
    this.name = name;
    this.isFinal = isFinal;
    this.isInitial = isInitial;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  public void setFinal(boolean isFinal) {
    this.isFinal = isFinal;
  }

  public void setInitial(boolean isInitial) {
    this.isInitial = isInitial;
  }

  public String getName() {
    return name;
  }

  public boolean isFinal() {
    return isFinal;
  }

  public boolean isInitial() {
    return isInitial;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    State s = (State) o;
    return name.equals(s.name);
  }

}