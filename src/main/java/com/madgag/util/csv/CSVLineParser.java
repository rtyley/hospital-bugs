package com.madgag.util.csv;

import java.util.List;

public interface CSVLineParser<T> {
	T parse(List<String> values);
}
