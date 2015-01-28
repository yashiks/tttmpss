package nl.avisi.processor;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

/**
 * @author robbreuk
 */
public final class WordCounterBolt extends BaseBasicBolt {

    private static final long serialVersionUID = -5809759391597965718L;
  //  private final Map<String, Integer> counts = new HashMap<String, Integer>();
 
    
    @Override
    public final void execute(final Tuple tuple, final BasicOutputCollector collector) {
        final Integer xaxis = tuple.getInteger(0);
        final Integer yaxis = tuple.getInteger(1);
        final String zaxis = tuple.getString(2);

        String data[]=zaxis.split(",");
    
        collector.emit(new Values(data[0],xaxis, yaxis,data[1]));
    }

    @Override
    public final void declareOutputFields(final OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("uid", "x","y","username"));
    }
}