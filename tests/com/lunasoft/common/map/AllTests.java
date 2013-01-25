package com.lunasoft.common.map;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	NorthSouthNoWrapHexMapTest.class,
	NorthSouthWithWrapHexMapTest.class
})
public class AllTests {
}
