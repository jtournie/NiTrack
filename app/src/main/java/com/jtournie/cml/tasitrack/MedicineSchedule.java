package com.jtournie.cml.tasitrack;

import android.content.Context;

/**
 * Created by jtournie on 22/11/14.
 */
public class MedicineSchedule {
    private NiTime inTakeTimeAM;
    private NiTime inTakeTimePM;
    private NiTime inTakeTimeOnce;
    private TasitrackPreferences tasitrackPreferences;

    final int STATE_EATING = 1;
    final int STATE_FEASTING_PRE_MEDICINE = 2;
    final int STATE_FEASTING_POST_MEDICINE = 3;
    final int STATE_SNACK = 4;

    public MedicineSchedule(User currentUser)
    {
        initMedicineSchedule(currentUser);

        tasitrackPreferences = new TasitrackPreferences();
    }

    public MedicineSchedule(User currentUser, Context context)
    {
        initMedicineSchedule(currentUser);

        tasitrackPreferences = new TasitrackPreferences(context);
    }

    private void initMedicineSchedule( User currentUser)
    {
        inTakeTimeAM = new NiTime();
        this.inTakeTimeAM = currentUser.getIntakeTimeAM();

        inTakeTimePM = new NiTime();
        this.inTakeTimePM = currentUser.getIntakeTimePM();

        inTakeTimeOnce = new NiTime();
        this.inTakeTimeOnce = currentUser.getIntakeTimeOnce();
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
        NiTime takeMedicineTime;

        if ( tasitrackPreferences.dosage == 1)
        {
            takeMedicineTime = inTakeTimeOnce.getRemainingTime(0);
        } else
        {
            takeMedicineTime = getRemainingTimeGeneric(0);
        }

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


        //If we are on dosage once a day it as if we are always feasting before medicine time
        if (tasitrackPreferences.dosage == 1)
        {
            return STATE_FEASTING_PRE_MEDICINE;
        }


        if ( niTimeStopEating.isSmaller(niTimeTakeMedicine) == 1 && niTimeStopEating.isSmaller(niTimeStartEating) == 1)
        {
            state = STATE_EATING;
        }

        //TODO: make the snack time a user setting
        if ( niTimeStopEating.Hour == 0 && niTimeStopEating.Minute <= 30)
        {
            state = STATE_SNACK;
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
