package com.hospitalbugs.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventSetTest {
	public static class Ball {
		private final Integer start;
		private final Integer end;

		public Ball(Integer start, Integer end) {
			this.start = start;
			this.end = end;
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName()+"["+start+"-"+end+"]";
		}
	}


	EventSet<Integer, Ball> significantInstants;
	
	@Before
	public void setUp() {
		significantInstants = new EventSet<Integer, Ball>(
				new IntervalTypeAdaptor<Integer, Ball>() {
					public SimpleInterval<Integer> getIntervalFor(Ball ball) {
						return new SimpleInterval<Integer>(ball.start,ball.end);
					}
		});
	}
	
	@Test
	public void shouldNotReturnAnEventMoreThanOnce() {
		Ball ball = new Ball(1, 4);
		
		significantInstants.add(ball);
		
		Collection<Ball> balls = significantInstants.getSignificantIntervalsDuring(0, 5);
		assertThat(balls.size(), equalTo(1));
		assertThat(balls, hasItem(ball));
	}
	
	
	@Test
	public void shouldReturnASignificantIntervalWhichStartsAndEndsOutsideOfTheRequestedBounds() {
		Ball ball = new Ball(1, 4);
		
		significantInstants.add(ball);
		
		assertThat(significantInstants.getSignificantIntervalsDuring(2,3), hasItem(ball));
	}
	
	@Test
	public void shouldNotReturnEventsEitherSideOfTheRequestedBounds() {
		Ball lowBall = new Ball(1, 2), middleBall = new Ball(11, 12), highBall = new Ball(21, 22);
		
		significantInstants.add(lowBall);
		significantInstants.add(middleBall);
		significantInstants.add(highBall);
		
		assertThat(significantInstants.getSignificantIntervalsDuring(10,20), not(hasItem(lowBall)));
	}
	
	@Test
	public void shouldOverrideOtherIntervals() throws Exception {
		Ball ballA = new Ball(1, 4);
		
		significantInstants.add(ballA);
		
		Ball ballB = new Ball(0,3);
		
		significantInstants.overrideWith(ballB);
		
		Ball ballC = new Ball(1,2);
		significantInstants.overrideWith(ballC);

		assertThat(significantInstants.getSignificantIntervalAt(1), equalTo(ballC));
	}
	
	@Test
	public void shouldGetLatestSignificantIntervalStartingAtOrBefore() {
		Ball ballB = new Ball(2,5);
		significantInstants.add(ballB);
		
		assertThat(significantInstants.getLatestSignificantIntervalStartingAtOrBefore(1), nullValue());
		assertThat(significantInstants.getLatestSignificantIntervalStartingAtOrBefore(2), equalTo(ballB));
		assertThat(significantInstants.getLatestSignificantIntervalStartingAtOrBefore(3), equalTo(ballB));
		assertThat(significantInstants.getLatestSignificantIntervalStartingAtOrBefore(6), equalTo(ballB));
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
