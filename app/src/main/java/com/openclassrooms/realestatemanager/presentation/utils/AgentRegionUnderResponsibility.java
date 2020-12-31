package com.openclassrooms.realestatemanager.presentation.utils;

import com.openclassrooms.realestatemanager.R;

public enum AgentRegionUnderResponsibility {

    PACA(R.string.PACA),
    HERAULT(R.string.OCCITANIE),
    AUVERGNE_RHONE_ALPES(R.string.AUVERGNE_RHONE_ALPES);

    public final int label;

    AgentRegionUnderResponsibility(int label){this.label = label;}
}
