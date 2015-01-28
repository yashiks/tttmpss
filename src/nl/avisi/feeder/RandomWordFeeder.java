package nl.avisi.feeder;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Map;
import java.util.Random;


public final class RandomWordFeeder extends BaseRichSpout {

	private static final String[] USERNAMES = new String[]{
        "Google", "Apple", "Microsoft", "AMAZON",  "YAHOO", "FACEBOOK", "TWITTER"};
    private static final long serialVersionUID = 5617224155226323658L;
    private SpoutOutputCollector collector;
    private Random random;
    @Override
    public final void open(final Map map, final TopologyContext topologyContext, final SpoutOutputCollector collector) {
        this.collector = collector;
        this.random = new Random();

    }

    @Override
    public final void nextTuple() {
        Utils.sleep(1500);
        int cnt=random.nextInt(USERNAMES.length);
        
        collector.emit(new Values(random.nextInt(90),random.nextInt(90),cnt+","+USERNAMES[cnt]));
    }

    @Override
    public final void declareOutputFields(final OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("x","y","z"));
    }
}
