package com.hospitalbugs.model;

import static com.madgag.intervals.SimpleInterval.interval;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.madgag.testsupport.matchers.IsMap;

@RunWith(MockitoJUnitRunner.class)
public class EventMapTest {

	EventMap<Integer, String> significantInstants;
	
	@Before
	public void setUp() {
		significantInstants = new EventMap<Integer, String>();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowPartiallyOverlappingIntervals() {
		significantInstants.put(interval(1, 4), "foo");
		significantInstants.put(interval(2, 6), "bar");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowEngulfingIntervals() {
		significantInstants.put(interval(1, 4), "foo");
		significantInstants.put(interval(0, 6), "bar");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowSubIntervals() {
		significantInstants.put(interval(1, 4), "foo");
		significantInstants.put(interval(2, 3), "bar");
	}
	
	@Test
	public void shouldNotReturnAnEventMoreThanOnce() {
		significantInstants.put(interval(1, 4), "Fred");
		
		Collection<String> balls = significantInstants.getEventsDuring(0, 5);
		assertThat(balls.size(), equalTo(1));
		assertThat(balls, hasItem("Fred"));
	}
	
	@Test
	public void shouldReturnASignificantIntervalWhichStartsAndEndsOutsideOfTheRequestedBounds() {
		significantInstants.put(interval(1, 4), "Fred");
		
		assertThat(significantInstants.subMapForEventsDuring(2,3), IsMap.containingOnly(interval(1, 4), "Fred"));
	}

	@Test
	public void shouldNotReturnEventsOutsideOfBoundsContainingNoEvents() {
		significantInstants.put(interval(1, 2), "foo");
		significantInstants.put(interval(21, 22), "bar");
		
		assertThat(significantInstants.subMapForEventsDuring(10,20).isEmpty(), equalTo(true));
		assertThat(significantInstants.subMapForEventsDuring(2,21).isEmpty(), equalTo(true));
		
	}
	
	@Test
	public void shouldNotReturnEventsDistantlyEitherSideOfTheRequestedBounds() {
		significantInstants.put(interval(1, 2),"foo");
		significantInstants.put(interval(11, 12),"bar");
		significantInstants.put(interval(21, 22),"baz");
		
		Collection<String> found = significantInstants.getEventsDuring(10,20);
		assertThat(found, hasItem("bar"));
		assertThat(found.size(), equalTo(1));
		
	}
	
	@Test
	public void shouldNotReturnEventsAbuttingTheRequestedBounds() {
		significantInstants.put(interval(1, 2),"foo");
		significantInstants.put(interval(2, 3),"bar");
		significantInstants.put(interval(3, 4),"baz");
		
		Collection<String> found = significantInstants.getEventsDuring(2,3);
		assertThat(found, hasItem("bar"));
		assertThat(found.toString(), found.size(), equalTo(1));
	}
	
	@Test
	public void shouldOverrideOtherIntervals() throws Exception {
		significantInstants.put( interval(1, 4),"foo" );
		significantInstants.put( interval(10, 20),"apple" );

		significantInstants.overrideWith(interval(0, 3),"bar" );
		
		significantInstants.overrideWith(interval(1, 2),"baz");

		Collection<String> values = significantInstants.values();
		assertThat(values, hasItems("apple","baz"));
		assertThat(values.size(), equalTo(2));
	}
	
	@Test
	public void shouldGetLatestSignificantIntervalStartingAtOrBefore() {
		significantInstants.put(interval(2, 5) ,"foo");
		
		assertThat(significantInstants.getLatestSignificantIntervalStartingAtOrBefore(1), nullValue());
		assertThat(significantInstants.getLatestSignificantIntervalStartingAtOrBefore(2), equalTo("foo"));
		assertThat(significantInstants.getLatestSignificantIntervalStartingAtOrBefore(3), equalTo("foo"));
		assertThat(significantInstants.getLatestSignificantIntervalStartingAtOrBefore(6), equalTo("foo"));
	}
	

//	@Test
//	public void shouldCountOccurences() {
//		LogInterval firstInterval = new LogInterval(standardSeconds(1),new LogInstant(3000));
//		LogInterval middleInterval = new LogInterval(standardSeconds(2),new LogInstant(6500));
//		LogInterval lastInterval = new LogInterval(standardSeconds(1),new LogInstant(9000));
//		significantInstants.add(new SignificantInterval(null, firstInterval));
//		significantInstants.add(new SignificantInterval(null, middleInterval));
//		significantInstants.add(new SignificantInterval(null, lastInterval));
//		
//		assertThat(significantInstants.countOccurencesDuring(new LogInterval(standardSeconds(3),new LogInstant(7000))), equalTo(1));
//		assertThat(significantInstants.countOccurencesDuring(new LogInterval(standardSeconds(1),new LogInstant(6000))), equalTo(1));
//		assertThat(significantInstants.countOccurencesDuring(firstInterval), equalTo(1));
//		assertThat(significantInstants.countOccurencesDuring(middleInterval), equalTo(1));
//		assertThat(significantInstants.countOccurencesDuring(lastInterval), equalTo(1));
//		assertThat(significantInstants.countOccurencesDuring(new LogInterval(firstInterval.getEnd(),middleInterval.getStart())), equalTo(0));
//		assertThat(significantInstants.countOccurencesDuring(new LogInterval(standardSeconds(8),new LogInstant(9500))), equalTo(3));
//		assertThat(significantInstants.countOccurencesDuring(new LogInterval(standardSeconds(7),new LogInstant(8500))), equalTo(3));
//		assertThat(significantInstants.countOccurencesDuring(new LogInterval(standardSeconds(1),new LogInstant(6000))), equalTo(1));
//	}
}
