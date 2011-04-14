package mats_tests;

import mats_model.*;
import junit.framework.TestCase;
import java.util.*;

public class MATSTaskStateTest extends TestCase {

	MATSTaskState state;
	Date cDate;
	Date cdDate;
	
	protected void setUp() throws Exception {
		
		super.setUp();
		state = new MATSTaskState();
		
		Calendar c = Calendar.getInstance();
		cDate =  c.getTime();
		cdDate = new Date(cDate.getYear(), cDate.getMonth(), cDate.getDate());
		
	}

	protected void tearDown() throws Exception {
		
		super.tearDown();
	}

	public void testMATSTaskState() {
		
	}

	public void testChangeState() throws Exception {

		setUp();
		
		int ongoing = 0;
		int overdue = 1;
		int completed = 2;
		int deleted = 3;
		
		assertEquals(ongoing, state.getCurrentState());
		
		assertFalse(state.changeState(ongoing, new Date(108, 2, 1)));
		
		//Due date = March 6, 2008.  Note that if this test is launched on a date later than
		//the due date, then this test will not work! So change the due date to a later date.
		assertFalse(state.changeState(overdue, cdDate));
		
		//Due date = March 1, 2008.
		assertTrue(state.changeState(overdue, new Date(108, 2, 1)));
		
		assertFalse(state.changeState(overdue, null));
		
		assertFalse(state.changeState(ongoing, null));
		
		assertTrue(state.changeState(completed, null));
		
		assertFalse(state.changeState(overdue, null));
		
		setUp();
		
		assertEquals(ongoing, state.getCurrentState());
		
		assertTrue(state.changeState(deleted, new Date(108, 2, 1)));
		
		assertFalse(state.changeState(completed, null));
		
		tearDown();
	}

	public void testGetCurrentDate() throws Exception {
		
		setUp();
		String currentDate;
		

		currentDate = cdDate.toString();
		 
		 
	
		assertTrue(currentDate.equals(state.getCurrentDate().toString()));
		
		tearDown();
	}

	public void testGetCurrentState() throws Exception {
		
		setUp();
		
		int ongoing = 0;
		int overdue = 1;
		int completed = 2;
		int deleted = 3;
		
		assertEquals(ongoing, state.getCurrentState());
		
		if (state.changeState(overdue, new Date(108, 2, 1))) {
			
			assertEquals(overdue, state.getCurrentState());
		}
		
		if (state.changeState(completed, null)) {
			
			assertEquals(completed, state.getCurrentState());
		}
		
		if (state.changeState(deleted, null)) {
			
			assertEquals(deleted, state.getCurrentState());
		}
		
		tearDown();		
	}

	public void testToString() throws Exception {
		
		setUp();
		
		String ongoing = "ONGOING";
		String overdue = "OVERDUE";
		String completed = "COMPLETED";
		String deleted = "DELETED";
		
		int ongoingState = 0;
		int overdueState = 1;
		int completedState = 2;
		int deletedState = 3;
		
		assertEquals(ongoing, state.toString());
		
		if (state.changeState(overdueState, new Date(108, 2, 1))) {
			
			assertEquals(overdue, state.toString());
		}
		
		if (state.changeState(completedState, null)) {
			
			assertEquals(completed, state.toString());
		}
		
		tearDown();		
	}

}
