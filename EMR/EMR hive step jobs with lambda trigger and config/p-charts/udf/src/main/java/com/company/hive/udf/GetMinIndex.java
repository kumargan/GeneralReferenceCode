package com.company.hive.udf;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.*;

/*
 * finds index of minimum value in the list.
 */
@Description(name = "GetMinIndex", value = "_FUNC(int) - computes min index", extended = "select col1, GetMinIndex(value) from table group by col1;")
public class GetMinIndex extends UDAF {
  // Define Logging
  static final Log LOG = LogFactory.getLog(GetMinIndex.class.getName());
  public static class GetMinIndexEvaluator implements UDAFEvaluator {
    /**
     * Use Column class to serialize intermediate computation
     * This is our groupByColumn
     */
    public static class Column {    
      List<Integer> list = new ArrayList<>();
    }
    
    private Column col;
    public GetMinIndexEvaluator() {
      super();
      init();
    }
    // A - Initalize evaluator - indicating that no values have been
    // aggregated yet.
    public void init() {
      LOG.debug("Initialize evaluator");
      col = new Column();
    }
    // B- Iterate every time there is a new value to be aggregated
    public boolean iterate(int value) throws HiveException {
      LOG.debug("Iterating over each value for aggregation");
      if (col == null)
        throw new HiveException("Item is not initialized");
      col.list.add(value);
      return true;
    }
    // C - Called when Hive wants partially aggregated results.
    public Column terminatePartial() {
      LOG.debug("Return partially aggregated results");
      return col;
    }
    // D - Called when Hive decides to combine one partial aggregation with another
    public boolean merge(Column other) {
      LOG.debug("merging by combining partial aggregation");
      if(other == null) {
        return true;
      }
      if(other.list.size()>0)
        col.list.addAll(other.list);
      return true; 
    }
    // E - Called when the final result of the aggregation needed.
    public int terminate(){
      LOG.debug("At the end of last record of the group - returning final result"); 
      int index = col.list.indexOf(Collections.min(col.list));
      return index==-1?0:index;
    }
  }
}