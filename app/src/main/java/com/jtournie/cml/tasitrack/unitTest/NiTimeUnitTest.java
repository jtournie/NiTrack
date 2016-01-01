package com.jtournie.cml.tasitrack.unitTest;

import android.test.InstrumentationTestCase;
import com.jtournie.cml.tasitrack.NiTime;

/**
 * Created by jtournie on 19/12/15.
 */
public class NiTimeUnitTest extends InstrumentationTestCase
{
    public void test_isSmaller() throws Exception
    {
        NiTime niTime1 = new NiTime();
        NiTime niTime2 = new NiTime();
        int iReturnCodeExpected;
        int iReturnCode;

        niTime1.Hour = 10;
        niTime1.Minute = 00;

        niTime2.Hour = 12;
        niTime2.Minute = 00;

        iReturnCodeExpected = 1;

        iReturnCode = niTime1.isSmaller( niTime2);
        assertEquals( iReturnCodeExpected, iReturnCode);


        niTime1.Hour = 12;
        niTime1.Minute = 00;

        niTime2.Hour = 12;
        niTime2.Minute = 00;

        iReturnCodeExpected = 0;

        iReturnCode = niTime1.isSmaller( niTime2);
        assertEquals( iReturnCodeExpected, iReturnCode);



    }
}
