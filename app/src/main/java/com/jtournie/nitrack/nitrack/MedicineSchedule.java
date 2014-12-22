package com.jtournie.nitrack.nitrack;

/**
 * Created by jtournie on 22/11/14.
 */
public class MedicineSchedule {
    private NiTime inTakeTimeAM;
    private NiTime inTakeTimePM;

    final int STATE_EATING = 1;
    final int STATE_FEASTING_PRE_MEDICINE = 2;
    final int STATE_FEASTING_POST_MEDICINE = 3;

    public MedicineSchedule(User currentUser)
    {
        inTakeTimeAM = new NiTime();
        this.inTakeTimeAM = currentUser.getIntakeTimeAM();

        inTakeTimePM = new NiTime();
        this.inTakeTimePM = currentUser.getIntakeTimePM();
    }

    public NiTime getRemainingTimeStopEating()
    {
        NiTime startEatingTime = getRemainingTimeGeneric(-2);
        return startEatingTime;
    }

    public NiTime getRemainingTimeStartEating()
    {
        NiTime startEatingTime = getRemainingTimeGeneric(1);
        return startEatingTime;
    }

    public NiTime getRemainingTimeTakeMedicine()
    {
        NiTime takeMedicineTime = getRemainingTimeGeneric(0);
        return takeMedicineTime;
    }

    private NiTime getRemainingTimeGeneric( int hour)
    {
        NiTime genericActionRemainingTime;

        NiTime genericActionRemainingTimeAM = inTakeTimeAM.getRemainingTime(hour);
        NiTime genericActionRemainingTimePM = inTakeTimePM.getRemainingTime(hour);

        if (genericActionRemainingTimeAM.isSmaller(genericActionRemainingTimePM) == 1)
        {
            genericActionRemainingTime = genericActionRemainingTimeAM;
        } else
        {
            genericActionRemainingTime = genericActionRemainingTimePM;
        }

        return genericActionRemainingTime;
    }

    public int getState()
    {
        int state = STATE_EATING;

        NiTime niTimeStopEating = getRemainingTimeStopEating();
        NiTime niTimeTakeMedicine = getRemainingTimeTakeMedicine();
        NiTime niTimeStartEating = getRemainingTimeStartEating();

        if ( niTimeStopEating.isSmaller(niTimeTakeMedicine) == 1 && niTimeStopEating.isSmaller(niTimeStartEating) == 1)
        {
            state = STATE_EATING;
        }

        if ( niTimeTakeMedicine.isSmaller(niTimeStopEating) == 1 && niTimeTakeMedicine.isSmaller(niTimeStartEating) == 1)
        {
            state = STATE_FEASTING_PRE_MEDICINE;
        }

        if ( niTimeStartEating.isSmaller(niTimeStopEating) == 1 && niTimeStartEating.isSmaller(niTimeTakeMedicine) == 1)
        {
            state = STATE_FEASTING_POST_MEDICINE;
        }

        return state;
    }

    public void setInTakeTimeAM(NiTime inTakeHour) {
        this.inTakeTimeAM = inTakeHour;
    }
}
