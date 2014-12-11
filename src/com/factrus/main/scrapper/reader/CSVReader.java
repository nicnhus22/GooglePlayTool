package com.factrus.main.scrapper.reader;

import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.factrus.main.scrapper.interfaces.IReader;
import com.factrus.main.scrapper.model.Data;

/**
 * 
 * @author edmond_j
 *
 */
public class CSVReader implements IReader
{
	private BufferedReader _buffer;
	private String[] _columnHeaders;

	@Override
	public void openLocation(String location) throws FileNotFoundException
	{
		_buffer = new BufferedReader(new FileReader(location));
	}
	
	@Override
	public Iterator<Data> getData()
	{
		ArrayList<Data> output = new ArrayList<Data>();
		String line;

		try
		{
			_columnHeaders = _buffer.readLine().split(";");
			
			for (int i = 0; i < _columnHeaders.length; ++i)
			{
				_columnHeaders[i] = _columnHeaders[i].substring(1, _columnHeaders[i].length() - 1);
			}
			
			while ((line = _buffer.readLine()) != null)
			{
				output.add(parseCSVLine(line));
			}
		}
		catch (Exception e)
		{}
		return output.listIterator();
	}
	
	@Override
	public void closeLocation()
	{
		try
		{
			_buffer.close();
		}
		catch (Exception e)
		{}
		_buffer = null;
		_columnHeaders = null;
	}
	
	private Data parseCSVLine(String line)
	{
		Data output = new Data();
		String[] values = line.split(";");
		
		for (int i = 0; i < values.length && i < _columnHeaders.length; ++i)
		{
			values[i] = values[i].substring(1, values[i].length() - 1);
			if (_columnHeaders[i].compareTo("Url") == 0)
				output.Url = values[i];
			else
				output.setAttribute(_columnHeaders[i], values[i]);
		}
		return output;
	}
}