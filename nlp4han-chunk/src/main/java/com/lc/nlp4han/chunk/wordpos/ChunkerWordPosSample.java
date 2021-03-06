package com.lc.nlp4han.chunk.wordpos;

import java.util.ArrayList;
import java.util.List;

import com.lc.nlp4han.chunk.AbstractChunkAnalysisSample;
import com.lc.nlp4han.chunk.Chunk;

/**
 * 基于词和词性的组块分析样本类
 */
public class ChunkerWordPosSample extends AbstractChunkAnalysisSample
{

	/**
	 * 构造方法
	 * 
	 * @param words
	 *            词语数组
	 * @param poses
	 *            词语对应的词性数组
	 * @param chunkTags
	 *            词语组块标记数组
	 */
	public ChunkerWordPosSample(String[] words, String[] poses, String[] chunkTags)
	{
		super(words, chunkTags, poses);
	}

	/**
	 * 构造方法
	 * 
	 * @param words
	 *            词语序列
	 * @param poses
	 *            词语对应的词性序列
	 * @param chunkTags
	 *            词语组块标记序列
	 */
	public ChunkerWordPosSample(List<String> words, List<String> poses, List<String> chunkTags)
	{
		super(words, chunkTags, poses.toArray(new String[poses.size()]));
	}

	public Chunk[] toChunk()
	{
		if (scheme.equals("BIEOS"))
			return toChunkFromBIEOS();
		else if (scheme.equals("BIEO"))
			return toChunkFromBIEO();
		else
			return toChunkFromBIO();
	}

	private Chunk[] toChunkFromBIEOS()
	{
		List<Chunk> chunks = new ArrayList<>();
		int start = 0;
		boolean isChunk = false;
		String type = null;

		for (int i = 0; i < tags.size(); i++)
		{
			if (tags.get(i).equals("O") || tags.get(i).split("_")[1].equals("B"))
			{
				if (isChunk)
					chunks.add(new Chunk(type, join(tokens, additionalContext, start, i), start, i - 1));

				isChunk = false;
				if (!tags.get(i).equals("O"))
				{
					start = i;
					isChunk = true;
					type = tags.get(i).split("_")[0];
				}
			}
			else if (tags.get(i).split("_")[1].equals("S"))
			{
				if (isChunk)
					chunks.add(new Chunk(type, join(tokens, additionalContext, start, i), start, i - 1));

				type = tags.get(i).split("_")[0];
				chunks.add(new Chunk(type, tokens.get(i) + "/" + additionalContext[i], i, i - 1));
				isChunk = false;
			}
		}

		if (isChunk)
			chunks.add(new Chunk(type, join(tokens, additionalContext, start, tags.size()), start, tags.size() - 1));

		return chunks.toArray(new Chunk[chunks.size()]);
	}

	private Chunk[] toChunkFromBIEO()
	{
		List<Chunk> chunks = new ArrayList<>();
		int start = 0;
		boolean isChunk = false;
		String type = null;

		for (int i = 0; i < tags.size(); i++)
		{
			if (tags.get(i).equals("O") || tags.get(i).split("_")[1].equals("B"))
			{
				if (isChunk)
					chunks.add(new Chunk(type, join(tokens, additionalContext, start, i), start, i - 1));

				isChunk = false;
				if (!tags.get(i).equals("O"))
				{
					start = i;
					isChunk = true;
					type = tags.get(i).split("_")[0];
				}
			}
		}

		if (isChunk)
			chunks.add(new Chunk(type, join(tokens, additionalContext, start, tags.size()), start, tags.size() - 1));

		return chunks.toArray(new Chunk[chunks.size()]);
	}

	private Chunk[] toChunkFromBIO()
	{
		List<Chunk> chunks = new ArrayList<>();
		int start = 0;
		boolean isChunk = false;
		String type = null;

		for (int i = 0; i < tags.size(); i++)
		{
			if (tags.get(i).equals("O") || tags.get(i).split("_")[1].equals("B"))
			{
				if (isChunk)
					chunks.add(new Chunk(type, join(tokens, additionalContext, start, i), start, i - 1));

				isChunk = false;
				if (!tags.get(i).equals("O"))
				{
					start = i;
					isChunk = true;
					type = tags.get(i).split("_")[0];
				}
			}
		}

		if (isChunk)
			chunks.add(new Chunk(type, join(tokens, additionalContext, start, tags.size()), start, tags.size() - 1));

		return chunks.toArray(new Chunk[chunks.size()]);
	}

	/**
	 * 拼接字符串word/pos word/pos ...
	 * 
	 * @param words
	 *            带拼接的词组
	 * @param poses
	 *            词组对应的词性
	 * @param start
	 *            拼接的开始位置
	 * @param end
	 *            拼接的结束位置
	 * @return 拼接后的字符串
	 */
	private List<String> join(List<String> words, Object[] poses, int start, int end)
	{
		List<String> string = new ArrayList<>();
		for (int i = start; i < end; i++)
			string.add(words.get(i) + "/" + poses[i]);

		return string;
	}

	@Override
	public String toString()
	{
		String string = "";

		Chunk[] chunks = toChunk();
		int j = 0;
		for (int i = 0; i < tokens.size(); i++)
		{
			if (j < chunks.length && i >= chunks[j].getStart() && i <= chunks[j].getEnd())
			{
				if (i == chunks[j].getEnd())
					string += chunks[j++] + "  ";
			}
			else
				string += tokens.get(i) + "/" + additionalContext[i] + "  ";
		}

		return string.trim();
	}
}
