package com.jtournie.nitrack.nitrack;

/**
 * Created by jtournie on 22/11/14.
 */
public class MedicineSchedule {
    private NiTime inTakeTime;

    final int STATE_EATING = 1;
    final int STATE_FEASTING_PRE_MEDICINE = 2;
    final int STATE_FEASTING_POST_MEDICINE = 3;

    public MedicineSchedule(User currentUser)
    {
        inTakeTime = new NiTime();
        this.inTakeTime = currentUser.getIntakeTime();
    }

    public NiTime getRemainingTimeStopEating()
    {
        NiTime stopEatingTime = inTakeTime.getRemainingTime(-2);
        return stopEatingTime;
    }

    public NiTime getRemainingTimeStartEating()
    {
        NiTime startEatingTime = inTakeTime.getRemainingTime(1);
        return startEatingTime;
    }

    public NiTime getRemainingTimeTakeMedicine()
    {
        NiTime takeMedicineTime = inTakeTime.getRemainingTime(0);
        return takeMedicineTime;
    }

    public int getState()
    {
        int state = STATE_EATING;

        NiTime niTimeStopEating = getRemainingTimeStopEating();
        NiTime niTimeTakeMedicine = getRemainingTimeTakeMedicine();
        NiTime niTimeStartEating = getRemainingTimeStartEating();

        if ( niTimeStopEating.IsTimeBefore == true) {
            state = STATE_EATING;
            return state;
        }

        if ( niTimeTakeMedicine.IsTimeBefore == true)
        {
            state = STATE_FEASTING_PRE_MEDICINE;
            return state;
        }

        if ( niTimeStartEating.IsTimeBefore == true)
        {
            state = STATE_FEASTING_POST_MEDICINE;
            return state;
        }


        return state;
    }

    public void setInTakeTime(NiTime inTakeHour) {
        this.inTakeTime = inTakeHour;
    }
}
