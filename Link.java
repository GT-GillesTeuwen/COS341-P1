public class Link {
    private String toReach;
    private State toGo;
    
    public Link(String toReach,State toGo) {
      this.toReach=toReach;
      this.toGo=toGo;
    }
  
    public String getToReach() {
      return toReach;
    }
  
    public State getToGo() {
      return toGo;
    }
    
    
  }