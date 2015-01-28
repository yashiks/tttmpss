package nl.java.runner;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.contrib.jms.JmsProvider;
import backtype.storm.contrib.jms.bolt.JmsBolt;
import backtype.storm.topology.TopologyBuilder;
import nl.avisi.feeder.RandomWordFeeder;
import nl.avisi.jms.SpringJmsProvider;
import nl.avisi.processor.WordCounterBolt;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

public final class Runner {
	public static final void main(final String[] args) throws Exception {
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		final JmsProvider jmsQueueProvider = new SpringJmsProvider(
				applicationContext, "jmsConnectionFactory", "notificationQueue");

		final TopologyBuilder topologyBuilder = new TopologyBuilder();

		final JmsBolt jmsBolt = new JmsBolt();
		jmsBolt.setJmsProvider(jmsQueueProvider);
		jmsBolt.setJmsMessageProducer((session, input) -> {
			// final String json = "{\"word\":\"" + input.getString(0) +
			// "\", \"count\":" + String.valueOf(input.getInteger(1)) + "}";
			final String json = "{\"uid\":\"" + input.getString(0)
					+ "\", \"x\":\"" + input.getInteger(1)
					+ "\", \"y\":\"" + input.getInteger(2)
					+ "\", \"username\":\"" + input.getString(3) + "\"}";
			return session.createTextMessage(json);
		});

		topologyBuilder.setSpout("wordGenerator", new RandomWordFeeder());
		topologyBuilder.setBolt("counter", new WordCounterBolt())
				.shuffleGrouping("wordGenerator");
		topologyBuilder.setBolt("jmsBolt", jmsBolt).shuffleGrouping("counter");

		final Config config = new Config();
		config.setDebug(true);

		/*final LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("map-analysis", config,
				topologyBuilder.createTopology());*/
		
		
		if (args.length > 0) {
			config.setNumWorkers(1);

			StormSubmitter.submitTopology(args[0], config,
					topologyBuilder.createTopology());
		} else {

			

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("mapi", config, topologyBuilder.createTopology());
			//Utils.sleep(60000);
			//cluster.killTopology("storm-jms-example");
			//cluster.shutdown();
		}
		
	}
}
