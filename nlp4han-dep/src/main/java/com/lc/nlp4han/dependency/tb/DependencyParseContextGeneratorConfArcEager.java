package com.lc.nlp4han.dependency.tb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DependencyParseContextGeneratorConfArcEager extends DependencyParseContextGenerator
{
	/**
	 * 无参构造
	 * 
	 * @throws IOException
	 *             IO异常
	 */
	public DependencyParseContextGeneratorConfArcEager() throws IOException
	{
		Properties featureConf = new Properties();
		InputStream featureStream = DependencyParseContextGeneratorConfArcEager.class.getClassLoader()
				.getResourceAsStream("com/lc/nlp4han/dependency/tbfeature.properties");
		featureConf.load(featureStream);

		init(featureConf);
	}

	/**
	 * 有参构造
	 * 
	 * @param config
	 *            配置文件
	 */
	public DependencyParseContextGeneratorConfArcEager(Properties config)
	{
		init(config);
	}

	public void init(Properties config)
	{
		super.init(config);
		// 动态特征
		s1_h_wset = config.getProperty("feature.s1_h_w", "false").equals("true");
		s1_h_tset = config.getProperty("feature.s1_h_t", "false").equals("true");
		pre_action_1set = config.getProperty("feature.pre_action_1", "false").equals("true");
		pre_action_2set = config.getProperty("feature.pre_action_2", "false").equals("true");
	}

	@Override
	public String[] getContext(int index, String[] wordpos, String[] priorDecisions, Object[] additionalContext)
	{
		ConfigurationArcEager conf = new ConfigurationArcEager();
		conf.generateConfByActions(wordpos, priorDecisions);
		return getContext(conf, priorDecisions, additionalContext);
	}

}
