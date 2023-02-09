package transact.beans;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CacheBean {
  Float ltp;
  
  
  //implement using reflection
  public Object[] getData() {
    Object[] data = new Object[1];
    data[0]=ltp;
    
    return data;
  }
}
